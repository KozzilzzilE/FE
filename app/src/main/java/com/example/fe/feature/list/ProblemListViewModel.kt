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

sealed class ProblemUiState {
    object Loading : ProblemUiState()
    data class Success(val problems: List<ProblemResult>) : ProblemUiState()
    data class Error(val message: String) : ProblemUiState()
}

class ProblemListViewModel(private val repository: ProblemRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<ProblemUiState>(ProblemUiState.Loading)
    val uiState: StateFlow<ProblemUiState> = _uiState.asStateFlow()

    fun loadProblems(topicId: Long) {
        viewModelScope.launch {
            if (_uiState.value !is ProblemUiState.Success) {
                _uiState.value = ProblemUiState.Loading
            }
            try {
                // [MOCK] Interceptor를 사용하므로 더 이상 개별 Mock 플래그는 필요 없습니다.
                // 토큰이 없더라도 MockInterceptor가 처리해 줄 것이므로 기본적인 토큰 획득 로직만 남깁니다.
                val token = TokenManager.getAccessToken() ?: "mock_token_for_dev"
                
                if (token == null) { // 실제 서버 연동 시에는 이 체크가 필요하겠지만, 현재는 가짜 토큰을 사용합니다.
                    _uiState.value = ProblemUiState.Error("로그인 토큰이 없습니다.")
                    return@launch
                }

                val response = repository.getTopicProblems(token, topicId)
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("ProblemListViewModel", "문제 목록 로드 성공: code=${body?.code}, message=${body?.message}")
                    if (body != null && body.isSuccess && body.result != null) {
                        _uiState.value = ProblemUiState.Success(body.result.problems)
                    } else {
                        _uiState.value = ProblemUiState.Error(body?.message ?: "데이터를 불러올 수 없습니다")
                    }
                } else {
                    Log.e("ProblemListViewModel", "API 실패: ${response.code()}")
                    _uiState.value = ProblemUiState.Error("서버 응답 오류")
                }
            } catch (e: Exception) {
                Log.e("ProblemListViewModel", "예외 발생", e)
                _uiState.value = ProblemUiState.Error("네트워크 오류")
            }
        }
    }

    fun toggleBookmark(problemId: Long, isCurrentlyBookmarked: Boolean) {
        val currentState = _uiState.value
        if (currentState !is ProblemUiState.Success) return

        viewModelScope.launch {
            try {
                val token = TokenManager.getAccessToken() ?: "mock_token_for_dev"
                
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

                val resultBookmarked = repository.toggleBookmark(token, problemId, isCurrentlyBookmarked)
                
                // 결과 보정
                val verifiedProblems = optimisticProblems.map {
                    if (it.problemId == problemId) {
                        it.copy(
                            isBookmark = resultBookmarked,
                            bookmarkCount = currentState.problems.find { p -> p.problemId == problemId }?.bookmarkCount?.plus(if (resultBookmarked) 1 else if (isCurrentlyBookmarked) -1 else 0) ?: 0
                        )
                    } else it
                }
                _uiState.value = ProblemUiState.Success(verifiedProblems)
            } catch (e: Exception) {
                // 실패 시 원래 상태로 복구
                _uiState.value = currentState
            }
        }
    }
}
