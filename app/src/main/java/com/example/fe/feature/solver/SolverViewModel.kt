package com.example.fe.feature.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.solver.data.SolverRepository
import com.example.fe.feature.solver.model.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
            // 초기 더미 제출 기록
            submissions = listOf(
                SubmissionRecord("2026.01.21 14:29:00", "Java", "정답", true),
                SubmissionRecord("2026.01.21 14:28:00", "Java", "오답", false)
            )
        )
    )
    val uiState: StateFlow<SolverUiState> = _uiState.asStateFlow()

    private val started = SharingStarted.WhileSubscribed(5_000)

    val code: StateFlow<String> =
        uiState.map { it.code }.stateIn(viewModelScope, started, _uiState.value.code)

    val testCases: StateFlow<List<TestCase>> =
        uiState.map { it.testCases }.stateIn(viewModelScope, started, _uiState.value.testCases)

    val isRunning: StateFlow<Boolean> =
        uiState.map { it.isRunning }.stateIn(viewModelScope, started, _uiState.value.isRunning)

    val executionResult: StateFlow<List<String>?> =
        uiState.map { it.runResult?.terminalLines }
            .stateIn(viewModelScope, started, _uiState.value.runResult?.terminalLines)

    val submissions: StateFlow<List<SubmissionRecord>> =
        uiState.map { it.submissions }.stateIn(viewModelScope, started, _uiState.value.submissions)

    /** 문제 상세 조회 */
    fun loadProblemDetail(problemId: Long, language: String = _uiState.value.language) {
        viewModelScope.launch {
            _uiState.update { it.copy(problemId = problemId, language = language, isLoadingProblem = true) }

            try {
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    throw Exception("로그인 토큰이 없습니다.")
                }

                val detail = repository.loadProblemDetail(token, problemId, language)

                _uiState.update { state ->
                    state.copy(
                        isLoadingProblem = false,
                        problemDetail = detail,
                        testCases = detail.testCases,
                        // 초기 코드 주입: 빈 상태거나 기본 템플릿일 때만 덮어쓰기
                        code = if (state.code.isBlank() || state.code == detail.initialCode) detail.initialCode else state.code
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoadingProblem = false,
                        errorToast = "문제 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateCode(newCode: String) {
        _uiState.update { it.copy(code = newCode) }
    }

    /** 코드 실행 */
    fun runCode() {
        val state = _uiState.value
        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isRunning = true, runResult = null) }

            val runResultResponse = repository.runCode(state.problemId, state.code, state.language)

            _uiState.update {
                it.copy(
                    isRunning = false,
                    runResult = runResultResponse
                )
            }
        }
    }

    fun clearRunResult() {
        _uiState.update { it.copy(runResult = null) }
    }

    /** 코드 제출/채점 */
    fun submitCode() {
        val state = _uiState.value
        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitResult = null) }

            val (submitResult, newRecord) = repository.submitCode(state.problemId, state.code, state.language)

            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    submitResult = submitResult,
                    submissions = listOf(newRecord) + it.submissions
                )
            }
        }
    }

    fun loadSubmissionHistory(problemId: Long = _uiState.value.problemId) {
        viewModelScope.launch {
            repository.loadSubmissionHistory(problemId)
        }
    }

    /** 모범 답안 조회 */
    fun loadSolution(problemId: Long = _uiState.value.problemId, language: String = _uiState.value.language) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSolution = true, solution = null) }

            try {
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    throw Exception("로그인 토큰이 없습니다.")
                }

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

    // 테스트케이스 관리
    fun addTestCase() {
        val nextId = (_uiState.value.testCases.maxOfOrNull { it.id } ?: 0L) + 1L
        _uiState.update { it.copy(testCases = it.testCases + TestCase(id = nextId)) }
    }

    fun removeTestCase(id: Long) {
        _uiState.update { it.copy(testCases = it.testCases.filterNot { tc -> tc.id == id }) }
    }

    fun updateTestCase(id: Long, input: String? = null, output: String? = null) {
        _uiState.update { state ->
            state.copy(
                testCases = state.testCases.map { tc ->
                    if (tc.id == id) tc.copy(
                        input = input ?: tc.input,
                        expectedOutput = output ?: tc.expectedOutput
                    ) else tc
                }
            )
        }
    }

    fun clearErrorToast() {
        _uiState.update { it.copy(errorToast = null) }
    }
}


