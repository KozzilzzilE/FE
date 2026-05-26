package com.example.fe.feature.solver.model

data class SubmissionRecord(
    val historyId: Long,
    val date: String,
    val language: String,
    val result: String,
    val isCorrect: Boolean,
    val sourceCode: String
)
