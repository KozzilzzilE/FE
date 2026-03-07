package com.example.fe.feature.study

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
                val response = RetrofitClient.instance.getTopics()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        val resultList = body.result ?: emptyList()
                        _uiState.value = TopicUiState.Success(resultList)
                    } else {
                        Log.e("TopicViewModel", "서버 비즈니스 오류: ${body?.message}")
                        // UI에 노출하지 않음
                        _uiState.value = TopicUiState.Error("")
                    }
                } else {
                    Log.e("TopicViewModel", "네트워크 에러 코드: ${response.code()}")
                    _uiState.value = TopicUiState.Error("")
                }
            } catch (e: Exception) {
                Log.e("TopicViewModel", "예외 발생", e)
                _uiState.value = TopicUiState.Error("")
            }
        }
    }
}
