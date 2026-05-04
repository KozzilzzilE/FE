package com.example.fe.feature.list.model

data class Problem(
    override val id: Long,
    override val title: String,
    override val difficulty: Difficulty,
    override val isCompleted: Boolean = false,
    val bookmarkCount: Int = 0,
    val isBookmarked: Boolean = false
) : DetailItem
