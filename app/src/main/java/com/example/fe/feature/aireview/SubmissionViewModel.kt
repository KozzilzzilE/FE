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
