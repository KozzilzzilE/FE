package com.example.fe.feature.list.data

import com.example.fe.data.dto.ProblemResult

object ProblemDummyData {
    val problems: List<ProblemResult> = listOf(
        ProblemResult(1L,  "두 수의 합",           "EASY",   "쉬움",   241, false, false),
        ProblemResult(2L,  "올바른 괄호",           "MEDIUM", "보통",   187, false, false),
        ProblemResult(3L,  "프로세스",              "HARD",   "어려움",  98, false, true),
        ProblemResult(4L,  "특정 문자 제거하기",     "EASY",   "쉬움",   312, false, false),
        ProblemResult(5L,  "다음에 올 숫자",         "MEDIUM", "보통",   154, false, false),
        ProblemResult(6L,  "배열 두 배 만들기",      "EASY",   "쉬움",   278, false, true),
        ProblemResult(7L,  "최빈값 구하기",          "EASY",   "쉬움",   203, false, false),
        ProblemResult(8L,  "문자열 뒤집기",          "EASY",   "쉬움",   341, false, false),
        ProblemResult(9L,  "소수 만들기",            "MEDIUM", "보통",   129, false, false),
        ProblemResult(10L, "행렬의 덧셈",            "EASY",   "쉬움",   195, false, true),
        ProblemResult(11L, "정수 내림차순으로 배치",  "EASY",   "쉬움",   167, false, false),
        ProblemResult(12L, "하샤드 수",              "EASY",   "쉬움",   221, false, false),
        ProblemResult(13L, "두 큰수의 합",           "MEDIUM", "보통",   88,  false, false),
        ProblemResult(14L, "단어 변환",              "HARD",   "어려움",  63, false, false),
        ProblemResult(15L, "여행경로",               "HARD",   "어려움",  47, false, false),
    )
}
