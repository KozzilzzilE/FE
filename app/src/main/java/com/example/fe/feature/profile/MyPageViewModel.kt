package com.example.fe.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.common.LanguagePreferenceManager
import com.example.fe.feature.profile.data.ProfileRepository
import com.example.fe.feature.profile.ui.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyPageViewModel(
    application: Application,
    private val repository: ProfileRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadMyPageAndLanguages()
    }

    fun loadMyPageAndLanguages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val myPageResponse = repository.getMyPageInfo()
                val languageList = repository.getLanguageList()

                val result = myPageResponse.result
                val savedLanguage = LanguagePreferenceManager.getLanguage(getApplication())
                val serverLanguage = result?.languageName.orEmpty()

                val finalLanguage = when {
                    savedLanguage.isNotBlank() -> savedLanguage
                    serverLanguage.isNotBlank() -> serverLanguage
                    else -> "JAVA"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = result?.nickname ?: "사용자",
                    languageName = finalLanguage,
                    languageOptions = languageList,
                    error = null
                )
            } catch (e: Exception) {
                val savedLanguage = LanguagePreferenceManager.getLanguage(getApplication())

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = _uiState.value.userName.ifBlank { "사용자" },
                    languageName = if (savedLanguage.isNotBlank()) savedLanguage else "JAVA",
                    error = e.message
                )
            }
        }
    }

    fun loadMyPageInfo() {
        loadMyPageAndLanguages()
    }

    fun loadLanguageList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val languages = repository.getLanguageList()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    languageOptions = languages,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun updatePreferredLanguage(language: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                repository.updateLanguage(language)
                LanguagePreferenceManager.saveLanguage(getApplication(), language)

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    languageName = language,
                    error = null
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }

    fun updateProfileTemp(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    userName = name,
                    error = null
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }
}