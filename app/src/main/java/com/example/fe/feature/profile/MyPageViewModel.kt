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

                val contributionData = result?.totalSolvedDetails
                    ?.mapNotNull { detail ->
                        runCatching { java.time.LocalDate.parse(detail.date) to detail.count }.getOrNull()
                    }
                    ?.toMap() ?: emptyMap()

                val today = java.time.LocalDate.now()
                var streakCount = 0
                var checkDate = today
                if ((contributionData[today] ?: 0) == 0) {
                    checkDate = today.minusDays(1)
                }
                while ((contributionData[checkDate] ?: 0) > 0) {
                    streakCount++
                    checkDate = checkDate.minusDays(1)
                }

                val totalStudyDays = contributionData.count { it.value > 0 }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = result?.nickname ?: "사용자",
                    languageName = finalLanguage,
                    languageOptions = languageList,
                    stat = com.example.fe.feature.profile.model.ProfileStat(
                        streak = streakCount.toString(),
                        solved = (result?.thisMonthSolvedCount ?: 0).toString(),
                        studyDays = totalStudyDays.toString()
                    ),
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

    fun updateProfile(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                val response = repository.updateNickname(name)

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    userName = response.result?.nickname ?: name,
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