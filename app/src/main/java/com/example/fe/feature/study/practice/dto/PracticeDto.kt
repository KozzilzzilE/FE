package com.example.fe.feature.study.practice.dto

data class QuizResponseDto(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<QuizItemDto>
)

data class QuizItemDto(
    val exerciseId: Long,
    val orderNo: Int,
    val title: String,
    val description: String,
    val codeTemplate: String,
    val blanks: List<BlankDto>
)

//빈칸
data class BlankDto(
    val content: String,
    val answer: Int
)

// 퍼킨스