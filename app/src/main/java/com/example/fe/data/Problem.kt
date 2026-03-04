package com.example.fe.data
// 테스트용

enum class Difficulty(val label: String) {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움")
}

data class Problem(
    val id: Int,
    val title: String,
    val difficulty: Difficulty,
    val isSolved: Boolean = false
)
