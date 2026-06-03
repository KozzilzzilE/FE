package com.example.fe.feature.solver.model

data class SubmitResult(
    val isCorrect: Boolean,
    val statusLabel: String,
    val runtimeMs: Long? = null,
    val errorMessage: String? = null,
    val isProcessing: Boolean = false,
    val progress: Double? = null
)
