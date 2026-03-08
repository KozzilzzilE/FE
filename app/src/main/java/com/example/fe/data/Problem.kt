package com.example.fe.data
// 테스트용

enum class Difficulty(val label: String) {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움")
}

data class Problem(
    override val id: Long,
    override val title: String,
    override val difficulty: Difficulty,
    override val isCompleted: Boolean = false
) : DetailItem
