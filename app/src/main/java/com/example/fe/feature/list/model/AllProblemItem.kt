package com.example.fe.feature.list.model

data class AllProblemItem(
    val problemId: Long,
    val title: String,
    val difficulty: Difficulty,
    val author: String,
    val bookmarkCount: Int
)