package com.example.fe.feature.aireview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fe.api.ApiService
import com.example.fe.common.TokenManager
import com.example.fe.feature.aireview.model.SubmissionEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubmissionViewModel(private val apiService: ApiService) : ViewModel() {

    private val _entries = MutableStateFlow<List<SubmissionEntry>>(emptyList())
    val entries: StateFlow<List<SubmissionEntry>> = _entries.asStateFlow()

    private val _selectedEntry = MutableStateFlow<SubmissionEntry?>(null)
    val selectedEntry: StateFlow<SubmissionEntry?> = _selectedEntry.asStateFlow()

    private val _aiReview = MutableStateFlow<com.example.fe.data.dto.AiCodeReviewResultDto?>(null)
    val aiReview: StateFlow<com.example.fe.data.dto.AiCodeReviewResultDto?> = _aiReview.asStateFlow()

    private val _isReviewLoading = MutableStateFlow(false)
    val isReviewLoading: StateFlow<Boolean> = _isReviewLoading.asStateFlow()

    init {
        loadRecentHistories()
    }

    fun loadRecentHistories() {
        viewModelScope.launch {
            val token = TokenManager.getAccessToken()
            if (token.isNullOrEmpty()) return@launch

            try {
                val response = apiService.getRecentHistories("Bearer $token")
                if (response.isSuccessful) {
                    val histories = response.body()?.result ?: emptyList()
                    _entries.value = histories.map { history ->
                        SubmissionEntry(
                            historyId = history.historyId,
                            problemTitle = history.title,
                            language = history.language,
                            date = history.createdAt,
                            isCorrect = history.status == "Accepted",
                            sourceCode = history.sourceCode
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("SubmissionViewModel", "Failed to load histories", e)
            }
        }
    }

    fun selectEntry(historyId: Long) {
        _selectedEntry.value = _entries.value.find { it.historyId == historyId }
        // 선택 시 해당 기록의 AI 리뷰도 함께 불러옵니다.
        loadAiReview(historyId)
    }

    fun loadAiReview(historyId: Long) {
        viewModelScope.launch {
            _isReviewLoading.value = true
            val token = TokenManager.getAccessToken()
            if (token.isNullOrEmpty()) {
                _isReviewLoading.value = false
                return@launch
            }

            try {
                val response = apiService.getAiReview("Bearer $token", historyId)
                if (response.isSuccessful) {
                    _aiReview.value = response.body()?.result
                } else {
                    _aiReview.value = null
                }
            } catch (e: Exception) {
                android.util.Log.e("SubmissionViewModel", "Failed to load AI review", e)
                _aiReview.value = null
            } finally {
                _isReviewLoading.value = false
            }
        }
    }

    fun requestAiReview(historyId: Long) {
        viewModelScope.launch {
            _isReviewLoading.value = true
            val token = TokenManager.getAccessToken()
            if (token.isNullOrEmpty()) {
                _isReviewLoading.value = false
                return@launch
            }

            try {
                val response = apiService.requestAiReview("Bearer $token", historyId)
                if (response.isSuccessful) {
                    _aiReview.value = response.body()?.result
                }
            } catch (e: Exception) {
                android.util.Log.e("SubmissionViewModel", "Failed to request AI review", e)
            } finally {
                _isReviewLoading.value = false
            }
        }
    }
}

class SubmissionViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubmissionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubmissionViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
