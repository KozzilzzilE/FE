package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

// 응용학습 조회 API 응답 구조
data class PracticeResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PracticeResultDto?
)

// result 내부 구조
data class PracticeResultDto(
    @SerializedName("count") val count: Int,
    @SerializedName("appliedExercises") val appliedExercises: List<QuizItemDto>
)

// 개별 응용 문제 데이터
data class QuizItemDto(
    @SerializedName("exerciseId") val exerciseId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("codeTemplate") val codeTemplate: String,
    @SerializedName("appliedCompleted") val appliedCompleted: Boolean,
    @SerializedName("totalBlanks") val totalBlanks: Int,
    @SerializedName("blanks") val blanks: List<BlankDto>?
)

// 빈칸 데이터
data class BlankDto(
    @SerializedName("content") val content: String,
    @SerializedName("answer") val answer: Int?
)

// 응용 문제 완료 API 응답
data class PracticeCompletionResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PracticeCompletionResultDto?
)

// 완료 처리 결과
data class PracticeCompletionResultDto(
    @SerializedName("exerciseId") val exerciseId: Long,
    @SerializedName("userName") val userName: String,
    @SerializedName("appliedCompleted") val appliedCompleted: Boolean
)
