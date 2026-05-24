package com.example.fe.feature.solver.model

data class SubmitResult(
    val isCorrect: Boolean,
    val statusLabel: String,
    val runtimeMs: Int? = null,
    val errorMessage: String? = null
)
