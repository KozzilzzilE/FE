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
                // TODO: 백엔드 API가 준비되면 아래 응답 코드 블록 주석을 해제하세요.
                /*
                val response = RetrofitClient.instance.getTopics()
                if (response.isSuccessful) {
                    ...
                }
                */
                // [임시] API 대신 로컬 샘플 데이터 즉시 로드 (시각적 UI 확인용)
                val sampleData = com.example.fe.data.TopicSampleData.sampleTopics
                _uiState.value = TopicUiState.Success(sampleData)

            } catch (e: Exception) {
                Log.e("TopicViewModel", "예외 발생", e)
                _uiState.value = TopicUiState.Error("")
            }
        }
    }
}
