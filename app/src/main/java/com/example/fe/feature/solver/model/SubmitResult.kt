package com.example.fe.feature.solver.model

data class SubmitResult(
    val isCorrect: Boolean,
    val runtimeMs: Int?,
    val errorMessage: String?
)