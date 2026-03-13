package com.example.fe.data

// sampleConcepts, sampleApplications는 각 Repository의 getMock*() 함수로 대체됨
// sampleProblems는 문제학습 목록 화면(NavGraph else 분기)에서 사용

val sampleProblems = listOf(
    Problem(id = 1L, title = "두 수의 합", difficulty = Difficulty.EASY, isCompleted = false),
    Problem(id = 2L, title = "완주하지 못한 선수", difficulty = Difficulty.EASY, isCompleted = false),
    Problem(id = 3L, title = "전화번호 목록", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Problem(id = 4L, title = "의상", difficulty = Difficulty.MEDIUM, isCompleted = false),
    Problem(id = 5L, title = "베스트앨범", difficulty = Difficulty.HARD, isCompleted = false)
)

