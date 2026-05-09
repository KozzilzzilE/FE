package com.example.fe.feature.profile.ui

import com.example.fe.data.dto.LanguageResult
import com.example.fe.feature.profile.model.ProfileStat

data class MyPageUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val userName: String = "",
    val languageName: String = "",
    val languageOptions: List<LanguageResult> = emptyList(),
    val level: Int = 1,
    val stat: ProfileStat = ProfileStat(
        streak = "0",
        solved = "0",
        studyDays = "0"
    ),
    val error: String? = null
)