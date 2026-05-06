package com.example.fe.feature.csquiz.model

/**
 * CS 퀴즈 O/X 문제 데이터 모델
 */
data class CsQuizQuestion(
    val id: Int,
    val question: String,
    val answer: Boolean,          // true = O, false = X
    val explanation: String
)
