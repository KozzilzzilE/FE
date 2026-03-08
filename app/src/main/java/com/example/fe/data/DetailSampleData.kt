package com.example.fe.data

val sampleConcepts = listOf(
    Concept(id = 1, title = "개념학습 1", difficulty = Difficulty.EASY, isCompleted = true),
    Concept(id = 2, title = "개념학습 2", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Concept(id = 3, title = "개념학습 3", difficulty = Difficulty.HARD, isCompleted = false)
)

val sampleApplications = listOf(
    Application(id = 1, title = "응용학습 1", difficulty = Difficulty.EASY, isCompleted = true),
    Application(id = 2, title = "응용학습 2", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Application(id = 3, title = "응용학습 3", difficulty = Difficulty.HARD, isCompleted = false)
)

val sampleProblems = listOf(
    Problem(id = 1L, title = "두 수의 합", difficulty = Difficulty.EASY, isCompleted = false),
    Problem(id = 2L, title = "완주하지 못한 선수", difficulty = Difficulty.EASY, isCompleted = false),
    Problem(id = 3L, title = "전화번호 목록", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Problem(id = 4L, title = "의상", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Problem(id = 5L, title = "베스트앨범", difficulty = Difficulty.HARD, isCompleted = false)
)
