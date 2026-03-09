package com.example.fe.feature.study.practice.dto

// 응용학습 조회 API 응답 구조
data class PracticeResponseDto(
    val isSuccess: Boolean,
    val code: String,
    val message: String,

    // 실제 데이터는 result 안에
    val result: PracticeResultDto?
)

// result 내부 구조
data class PracticeResultDto(
    val count: Int, // 응용 문제 개수
    val appliedExercises: List<QuizItemDto> // 응용 문제 리스트
)

// 개별 응용 문제 데이터
data class QuizItemDto(
    val exerciseId: Long, // 문제 ID (완료 API에서 사용됨)
    val title: String, // 문제 제목
    val description: String, // 문제 설명
    val codeTemplate: String, // 코드 템플릿 (빈칸 포함 코드)

    val appliedCompleted: Boolean, // 이미 풀었는지 여부 (완료 상태)

    val totalBlanks: Int, // 빈칸 개수

    // 언어에 따라 blanks가 null일 수 있음
    val blanks: List<BlankDto>?
)

// 빈칸 데이터
data class BlankDto(
    val content: String, // 선택지 내용
    val answer: Int // 정답 번호
)

// 응용 문제 완료 API 응답
data class PracticeCompletionResponseDto(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PracticeCompletionResultDto?
)

// 완료 처리 결과
data class PracticeCompletionResultDto(
    val exerciseId: Long, // 완료된 문제 ID
    val userName: String, // 사용자 이름
    val appliedCompleted: Boolean // 완료 여부 (true)
)