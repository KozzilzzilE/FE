package com.example.fe.feature.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.api.RetrofitClient
import com.example.fe.data.dto.TopicResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TopicUiState {
    object Loading : TopicUiState()
    data class Success(val topics: List<TopicResult>) : TopicUiState()
    data class Error(val message: String) : TopicUiState()
}

class TopicViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    init {
        fetchTopics()
    }

    fun fetchTopics() {
        viewModelScope.launch {
            _uiState.value = TopicUiState.Loading
            try {
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    _uiState.value = TopicUiState.Error("로그인 토큰이 없습니다.")
                    return@launch
                }

                val response = RetrofitClient.instance.getTopics("Bearer $token")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("TopicViewModel", "토픽 목록 로드 성공: code=${body?.code}, message=${body?.message}")
                    if (body != null && body.isSuccess && body.result != null) {
                        _uiState.value = TopicUiState.Success(body.result.topics) // 토픽 목록을 성공 상태로 업데이트
                    } else {
                        _uiState.value = TopicUiState.Error("데이터를 불러올 수 없습니다")
                    }
                } else {
                    Log.e("TopicViewModel", "API 실패: ${response.code()}")
                    _uiState.value = TopicUiState.Error("")
                }
            } catch (e: Exception) {
                Log.e("TopicViewModel", "예외 발생", e)
                _uiState.value = TopicUiState.Error("")
            }
        }
    }
}
