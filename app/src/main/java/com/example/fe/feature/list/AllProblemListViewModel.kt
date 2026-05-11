package com.example.fe.feature.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.ProblemResult
import com.example.fe.feature.list.data.ProblemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 전체 문제 목록을 관리하는 ViewModel (페이지당 20개, 난이도 필터링 지원)
 */
class AllProblemListViewModel(private val repository: ProblemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProblemUiState>(ProblemUiState.Loading)
    val uiState: StateFlow<ProblemUiState> = _uiState.asStateFlow()

    // 현재 페이지 상태
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // 전체 페이지 수
    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    // 현재 선택된 난이도 필터 상태 (null이면 전체)
    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    val selectedDifficulty: StateFlow<String?> = _selectedDifficulty.asStateFlow()

    /**
     * 전체 문제 목록 로드
     * @param page 불러올 페이지 번호
     * @param difficulty 필터링할 난이도 (null, "EASY", "MEDIUM", "HARD")
     */
    fun loadAllProblems(page: Int = _currentPage.value, difficulty: String? = _selectedDifficulty.value) {
        viewModelScope.launch {
            _currentPage.value = page
            _selectedDifficulty.value = difficulty
            
            if (_uiState.value !is ProblemUiState.Success) {
                _uiState.value = ProblemUiState.Loading
            }

            try {
                val token = TokenManager.getAccessToken() 
                    ?: throw Exception("로그인 토큰이 없습니다.")
                
                Log.d("AllProblemListVM", "요청: page=${page - 1}, difficulty=$difficulty")
                val response = repository.getAllProblems(token, page - 1, difficulty)
                Log.d("AllProblemListVM", "응답 코드: ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("AllProblemListVM", "응답 바디: isSuccess=${body?.isSuccess}, resultNull=${body?.result == null}, 문제수=${body?.result?.problemList?.size}, totalPage=${body?.result?.totalPage}")
                    if (body != null && body.isSuccess && body.result != null) {
                        _uiState.value = ProblemUiState.Success(body.result.problemList)
                        _totalPages.value = body.result.totalPage
                    } else {
                        _uiState.value = ProblemUiState.Error(body?.message ?: "데이터를 불러올 수 없습니다.")
                    }
                } else {
                    Log.e("AllProblemListVM", "서버 오류: ${response.code()} ${response.errorBody()?.string()}")
                    _uiState.value = ProblemUiState.Error("서버 응답 오류 (${response.code()})")
                }
            } catch (e: Exception) {
                Log.e("AllProblemListVM", "데이터 로드 실패", e)
                _uiState.value = ProblemUiState.Error("네트워크 오류: ${e.message}")
            }
        }
    }

    /**
     * 찜하기 토글
     */
    fun toggleBookmark(problemId: Long, isCurrentlyBookmarked: Boolean) {
        val currentState = _uiState.value
        if (currentState !is ProblemUiState.Success) return

        viewModelScope.launch {
            // 낙관적 업데이트
            val optimisticProblems = currentState.problems.map {
                if (it.problemId == problemId) {
                    it.copy(
                        isBookmark = !isCurrentlyBookmarked,
                        bookmarkCount = (it.bookmarkCount ?: 0) + (if (isCurrentlyBookmarked) -1 else 1)
                    )
                } else it
            }
            _uiState.value = ProblemUiState.Success(optimisticProblems)

            val resultBookmarked = try {
                val token = TokenManager.getAccessToken() 
                    ?: throw Exception("로그인 토큰이 없습니다.")
                repository.toggleBookmark(token, problemId, isCurrentlyBookmarked)
            } catch (e: Exception) {
                Log.e("AllProblemListVM", "찜하기 실패: ${e.message}", e)
                null
            }

            if (resultBookmarked != null) {
                // 성공 시 결과 보정 (현재 상태에서 업데이트)
                val currentProblems = (_uiState.value as? ProblemUiState.Success)?.problems ?: optimisticProblems
                val verifiedProblems = currentProblems.map {
                    if (it.problemId == problemId) {
                        val originalItem = currentState.problems.find { p -> p.problemId == problemId }
                        val originalCount = originalItem?.bookmarkCount ?: 0
                        it.copy(
                            isBookmark = resultBookmarked,
                            bookmarkCount = originalCount + when {
                                resultBookmarked && !isCurrentlyBookmarked -> 1
                                !resultBookmarked && isCurrentlyBookmarked -> -1
                                else -> 0
                            }
                        )
                    } else it
                }
                _uiState.value = ProblemUiState.Success(verifiedProblems)
            } else {
                // 실패 시 롤백 (현재 상태에서 해당 아이템만 원래 상태로 복구)
                val currentProblems = (_uiState.value as? ProblemUiState.Success)?.problems ?: optimisticProblems
                val rolledBackProblems = currentProblems.map {
                    if (it.problemId == problemId) {
                        val originalItem = currentState.problems.find { p -> p.problemId == problemId }
                        it.copy(
                            isBookmark = isCurrentlyBookmarked,
                            bookmarkCount = originalItem?.bookmarkCount ?: 0
                        )
                    } else it
                }
                _uiState.value = ProblemUiState.Success(rolledBackProblems)
            }
        }
    }
}
