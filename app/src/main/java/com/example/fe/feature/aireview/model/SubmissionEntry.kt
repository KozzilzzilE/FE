package com.example.fe.feature.aireview.model

data class SubmissionEntry(
    val historyId: Long,
    val problemTitle: String,
    val language: String,
    val date: String,
    val isCorrect: Boolean,
    val sourceCode: String
)
