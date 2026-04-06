package com.example.fe.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.profile.data.ProfileRepository
import com.example.fe.feature.profile.ui.MyPageUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    fun loadMyPageInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val response = repository.getMyPageInfo()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = response.result.name,
                    languageName = response.result.languageName
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "마이페이지 정보 조회 실패"
                )
            }
        }
    }

    fun updateProfileTemp(
        name: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                delay(300)

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    userName = name
                )

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "프로필 수정 실패"
                )
            }
        }
    }

    fun updatePreferredLanguageTemp(
        languageName: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                delay(300)

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    languageName = languageName
                )

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "선호 언어 변경 실패"
                )
            }
        }
    }
}