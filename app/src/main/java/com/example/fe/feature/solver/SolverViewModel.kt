package com.example.fe.feature.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.fe.common.TokenManager
import com.example.fe.feature.solver.data.SolverRepository
import com.example.fe.feature.solver.model.SolverUiState
import com.example.fe.feature.solver.model.SubmissionRecord
import com.example.fe.feature.solver.model.SubmitResult
import com.example.fe.feature.solver.model.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolverViewModel(
    private val repository: SolverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SolverUiState(
            code = "",
            testCases = emptyList(),
            submissions = emptyList()
        )
    )
    val uiState: StateFlow<SolverUiState> = _uiState.asStateFlow()

    private val started = SharingStarted.WhileSubscribed(5_000)

    val code: StateFlow<String> =
        uiState.map { it.code }
            .stateIn(viewModelScope, started, _uiState.value.code)

    val testCases: StateFlow<List<TestCase>> =
        uiState.map { it.testCases }
            .stateIn(viewModelScope, started, _uiState.value.testCases)

    val isRunning: StateFlow<Boolean> =
        uiState.map { it.isRunning }
            .stateIn(viewModelScope, started, _uiState.value.isRunning)

    val executionResult: StateFlow<List<String>?> =
        uiState.map { it.runResult?.terminalLines }
            .stateIn(viewModelScope, started, _uiState.value.runResult?.terminalLines)

    val submissions: StateFlow<List<SubmissionRecord>> =
        uiState.map { it.submissions }
            .stateIn(viewModelScope, started, _uiState.value.submissions)

    /**
     * 문제 상세 조회
     */
    fun loadProblemDetail(problemId: Long, language: String? = null, difficultyLabel: String? = null) {
        val oldState = _uiState.value
        val preservedDifficulty = difficultyLabel ?: if (oldState.problemId == problemId) oldState.problemDetail?.difficultyLabel else null

        viewModelScope.launch {
            // 1. 사용할 언어 결정 (전달받은 값 -> 기존 상태 값 -> 선호 언어)
            val finalLanguage = language 
                ?: oldState.language.takeIf { it.isNotBlank() } 
                ?: TokenManager.getPreferredLanguage() // 새로 추가될 메서드
                ?: "JAVA"

            _uiState.update {
                it.copy(
                    problemId = problemId,
                    language = finalLanguage,
                    isLoadingProblem = true
                )
            }

            try {
                val token = TokenManager.getAccessToken()
                    ?: throw Exception("로그인 토큰이 없습니다.")

                // 문제 상세 정보 로드
                val detail = repository.loadProblemDetail(token, problemId, finalLanguage, preservedDifficulty)

                // 서버에서 임시 저장된 코드 가져오기 (실패해도 문제 로드는 계속 진행)
                val serverDraft = try {
                    repository.loadDraftCode(token, problemId, finalLanguage)
                } catch (e: Exception) {
                    Log.e("SolverViewModel", "임시 코드 로드 실패", e)
                    null
                }

                _uiState.update { state ->
                    val isLanguageChanged = oldState.language != finalLanguage && oldState.language.isNotBlank()

                    state.copy(
                        isLoadingProblem = false,
                        problemDetail = detail,
                        testCases = detail.testCases,
                        code = serverDraft ?: if (isLanguageChanged || state.code.isBlank() || state.code == detail.initialCode) {
                            detail.initialCode
                        } else {
                            state.code
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingProblem = false,
                        errorToast = "문제 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 북마크(찜) 토글
     */
    fun toggleBookmark() {
        val problemDetail = _uiState.value.problemDetail ?: return
        val currentState = problemDetail.isBookmarked
        val currentCount = problemDetail.bookmarkCount
        val newCount = if (currentState) maxOf(0, currentCount - 1) else currentCount + 1

        // 낙관적 UI 업데이트 (isBookmarked + bookmarkCount 동시)
        _uiState.update {
            it.copy(
                problemDetail = problemDetail.copy(
                    isBookmarked = !currentState,
                    bookmarkCount = newCount
                )
            )
        }

        viewModelScope.launch {
            try {
                val token = TokenManager.getAccessToken() ?: ""
                val resultBookmarked = repository.toggleBookmark(token, _uiState.value.problemId, currentState)

                // 서버 결과와 다를 때만 isBookmarked 보정 (count는 낙관적 값 유지)
                _uiState.update {
                    it.copy(
                        problemDetail = it.problemDetail?.copy(isBookmarked = resultBookmarked)
                    )
                }
            } catch (e: Exception) {
                Log.e("SolverViewModel", "북마크 토글 실패", e)
                // 실패 시 원래 상태 + count 롤백
                _uiState.update {
                    it.copy(
                        problemDetail = it.problemDetail?.copy(
                            isBookmarked = currentState,
                            bookmarkCount = currentCount
                        ),
                        errorToast = "찜하기 변경에 실패했습니다."
                    )
                }
            }
        }
    }


    /**
     * 코드 수정 (임시 저장)
     */
    fun updateCode(newCode: String) {
        _uiState.update { it.copy(code = newCode) }
    }

    /**
     * 임시 저장
     */
    fun saveDraft() {
        val state = _uiState.value

        viewModelScope.launch {
            if (state.problemId == 0L) return@launch

            // 서버 API로 저장
            try {
                val token = TokenManager.getAccessToken() ?: ""
                repository.saveDraftCode(
                    token = token,
                    problemId = state.problemId,
                    language = state.language,
                    code = state.code
                )
                Log.d("SolverViewModel", "서버 임시 저장 성공")
            } catch (e: Exception) {
                Log.e("SolverViewModel", "서버 임시 저장 실패", e)
            }
        }
    }

    /**
     * 코드 실행
     */
    fun runCode() {
        val state = _uiState.value

        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRunning = true,
                    runResult = null
                )
            }

            try {
                val token = TokenManager.getAccessToken()
                    ?: throw Exception("로그인 토큰이 없습니다.")

                val runToken = repository.runCode(
                    token = token,
                    problemId = state.problemId,
                    code = state.code,
                    language = state.language
                )

                pollRunResult(
                    accessToken = token,
                    runToken = runToken
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRunning = false,
                        errorToast = "코드 실행 실패: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 실행 결과 polling
     */
    private suspend fun pollRunResult(
        accessToken: String,
        runToken: String
    ) {
        repeat(15) {
            val result = repository.getRunResult(accessToken, runToken)

            _uiState.update {
                it.copy(runResult = result)
            }

            val currentStatus = result.errorMessage

            if (currentStatus == "In Queue" || currentStatus == "Processing") {
                delay(1500)
                return@repeat
            }

            _uiState.update {
                it.copy(isRunning = false)
            }
            return
        }

        _uiState.update {
            it.copy(
                isRunning = false,
                errorToast = "실행 결과 조회 시간이 초과되었습니다."
            )
        }
    }

    /**
     * 실행 결과 초기화
     */
    fun clearRunResult() {
        _uiState.update { it.copy(runResult = null) }
    }

    /**
     * 코드 제출 / 채점
     */
    fun submitCode() {
        val state = _uiState.value

        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    submitResult = null
                )
            }

            try {
                val token = TokenManager.getAccessToken()
                    ?: throw Exception("로그인 토큰이 없습니다.")

                /**
                 * repository.submitCode() 는
                 * historyId + submissionId 를 함께 반환
                 */
                val submitInfo = repository.submitCode(
                    token = token,
                    problemId = state.problemId,
                    code = state.code,
                    language = state.language
                )

                pollSubmitResult(
                    accessToken = token,
                    historyId = submitInfo.historyId,
                    language = state.language
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorToast = "코드 제출 실패: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 제출 결과 polling
     */
    private suspend fun pollSubmitResult(
        accessToken: String,
        historyId: Long,
        language: String
    ) {
        repeat(15) {
            val (isCorrect, status) = repository.getSubmissionResult(
                token = accessToken,
                historyId = historyId
            )

            if (status == "PROCESSING") {
                delay(1500)
                return@repeat
            }

            val submitResult = SubmitResult(
                isCorrect = isCorrect,
                runtimeMs = null,
                errorMessage = if (isCorrect) null else status
            )

            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    submitResult = submitResult
                )
            }

            loadSubmissionHistory(_uiState.value.problemId)
            return
        }

        _uiState.update {
            it.copy(
                isSubmitting = false,
                errorToast = "채점 결과 조회 시간이 초과되었습니다."
            )
        }
    }

    /**
     * 제출 기록 조회
     */
    fun loadSubmissionHistory(problemId: Long = _uiState.value.problemId) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getAccessToken()
                    ?: throw Exception("로그인 토큰이 없습니다.")

                val histories = repository.loadSubmissionHistory(
                    token = token,
                    problemId = problemId
                )

                _uiState.update {
                    it.copy(submissions = histories)
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorToast = "제출 기록 조회 실패: ${e.message}")
                }
            }
        }
    }

    /**
     * 모범 답안 조회
     */
    fun loadSolution(
        problemId: Long = _uiState.value.problemId,
        language: String = _uiState.value.language
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingSolution = true,
                    solution = null
                )
            }

            try {
                val token = TokenManager.getAccessToken()
                    ?: throw Exception("로그인 토큰이 없습니다.")

                val loadedSolution = repository.loadSolution(token, problemId, language)

                _uiState.update {
                    it.copy(
                        isLoadingSolution = false,
                        solution = loadedSolution
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingSolution = false,
                        errorToast = "모범 답안 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 테스트케이스 추가
     */
    fun addTestCase() {
        val nextId = (_uiState.value.testCases.maxOfOrNull { it.id } ?: 0L) + 1L
        _uiState.update {
            it.copy(testCases = it.testCases + TestCase(id = nextId))
        }
    }

    /**
     * 테스트케이스 삭제
     */
    fun removeTestCase(id: Long) {
        _uiState.update {
            it.copy(testCases = it.testCases.filterNot { tc -> tc.id == id })
        }
    }

    /**
     * 테스트케이스 수정
     */
    fun updateTestCase(id: Long, input: String? = null, output: String? = null) {
        _uiState.update { state ->
            state.copy(
                testCases = state.testCases.map { tc ->
                    if (tc.id == id) {
                        tc.copy(
                            input = input ?: tc.input,
                            expectedOutput = output ?: tc.expectedOutput
                        )
                    } else {
                        tc
                    }
                }
            )
        }
    }

    /**
     * 에러 토스트 초기화
     */
    fun clearErrorToast() {
        _uiState.update { it.copy(errorToast = null) }
    }
}