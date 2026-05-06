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
                val token = TokenManager.getAccessToken() ?: "mock_token_for_dev"
                
                // 한 페이지에 20개씩, 난이도 필터 적용하여 요청
                val response = repository.getAllProblems(token, page, difficulty)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess && body.result != null) {
                        _uiState.value = ProblemUiState.Success(body.result.problems)
                    } else {
                        _uiState.value = ProblemUiState.Error(body?.message ?: "데이터를 불러올 수 없습니다")
                    }
                } else {
                    _uiState.value = ProblemUiState.Error("서버 응답 오류 (${response.code()})")
                }
            } catch (e: Exception) {
                Log.e("AllProblemListVM", "예외 발생 — 더미 데이터 사용", e)
                val dummy = com.example.fe.feature.list.data.ProblemDummyData.problems
                val filtered = if (difficulty != null)
                    dummy.filter { it.difficulty.equals(difficulty, ignoreCase = true) }
                else dummy
                _uiState.value = ProblemUiState.Success(filtered)
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
            try {
                val token = TokenManager.getAccessToken() ?: "mock_token_for_dev"
                
                val optimisticProblems = currentState.problems.map {
                    if (it.problemId == problemId) {
                        it.copy(
                            isBookmark = !isCurrentlyBookmarked,
                            bookmarkCount = (it.bookmarkCount ?: 0) + (if (isCurrentlyBookmarked) -1 else 1)
                        )
                    } else it
                }
                _uiState.value = ProblemUiState.Success(optimisticProblems)

                val resultBookmarked = repository.toggleBookmark(token, problemId, isCurrentlyBookmarked)
                
                val verifiedProblems = optimisticProblems.map {
                    if (it.problemId == problemId) {
                        it.copy(isBookmark = resultBookmarked)
                    } else it
                }
                _uiState.value = ProblemUiState.Success(verifiedProblems)
                
            } catch (e: Exception) {
                _uiState.value = currentState
            }
        }
    }
}
