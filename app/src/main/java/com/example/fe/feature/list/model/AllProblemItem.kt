package com.example.fe.feature.list.model

data class AllProblemItem(
    val problemId: Long,
    val title: String,
    val difficulty: Difficulty,
    val bookmarkCount: Int,
    val isBookmarked: Boolean = false
)