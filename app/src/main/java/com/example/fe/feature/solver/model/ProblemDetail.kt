package com.example.fe.feature.solver.model

data class ProblemDetail(
    val problemId: Long,
    val title: String,
    val difficultyLabel: String? = null,
    val description: String = "",
    val exampleInput: String = "",
    val exampleOutput: String = "",
    val constraints: List<String> = emptyList(),
    val initialCode: String = "",
    val testCases: List<TestCase> = emptyList(),
    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0
)
