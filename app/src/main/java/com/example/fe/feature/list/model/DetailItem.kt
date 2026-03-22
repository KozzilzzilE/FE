package com.example.fe.feature.list.model

enum class Difficulty(val label: String) {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움")
}

interface DetailItem {
    val id: Long
    val title: String
    val difficulty: Difficulty
    val isCompleted: Boolean
}
