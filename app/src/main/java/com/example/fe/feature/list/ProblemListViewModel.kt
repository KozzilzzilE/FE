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
            _uiState.value = ProblemUiState.Loading
            try {
                val isMock = com.example.fe.common.MockConfig.USE_MOCK_PROBLEM
                val token = TokenManager.getAccessToken() ?: if (isMock) "mock_token" else null
                
                if (token == null) {
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
}
