package com.example.fe.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.BookmarkItem
import com.example.fe.feature.profile.data.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookmarkUiState {
    object Loading : BookmarkUiState()
    data class Success(val bookmarks: List<BookmarkItem>) : BookmarkUiState()
    data class Error(val message: String) : BookmarkUiState()
}

class BookmarkViewModel(private val repository: BookmarkRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    fun loadBookmarks() {
        viewModelScope.launch {
            _uiState.value = BookmarkUiState.Loading
            try {
                val token = TokenManager.getAccessToken() ?: ""
                val response = repository.getBookmarks(token)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        _uiState.value = BookmarkUiState.Success(body.result ?: emptyList())
                    } else {
                        _uiState.value = BookmarkUiState.Error(body?.message ?: "데이터를 불러올 수 없습니다.")
                    }
                } else {
                    _uiState.value = BookmarkUiState.Error("서버 응답 오류: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "예외 발생", e)
                _uiState.value = BookmarkUiState.Error("네트워크 오류가 발생했습니다.")
            }
        }
    }

    fun removeBookmark(problemId: Long) {
        val currentState = _uiState.value
        if (currentState !is BookmarkUiState.Success) return

        viewModelScope.launch {
            // 낙관적 업데이트
            val optimisticList = currentState.bookmarks.filter { it.problemId != problemId }
            _uiState.value = BookmarkUiState.Success(optimisticList)

            val success = try {
                val token = TokenManager.getAccessToken() ?: ""
                repository.deleteBookmark(token, problemId)
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "북마크 삭제 예외 발생", e)
                false
            }

            if (!success) {
                // 실패 시 롤백 (현재 상태에 실패한 아이템만 다시 추가)
                val currentBookmarks = (_uiState.value as? BookmarkUiState.Success)?.bookmarks ?: emptyList()
                val failedItem = currentState.bookmarks.find { it.problemId == problemId }
                if (failedItem != null && failedItem !in currentBookmarks) {
                    _uiState.value = BookmarkUiState.Success(currentBookmarks + failedItem)
                }
            }
        }
    }
}

class BookmarkViewModelFactory(private val repository: BookmarkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
