package com.example.fe.feature.profile.ui

import com.example.fe.feature.profile.model.ProfileStat

data class MyPageUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val bio: String = "",
    val level: Int = 1,
    val stat: ProfileStat = ProfileStat(
        rank = "#142",
        streak = "14D",
        solved = "85"
    ),
    val error: String? = null
)