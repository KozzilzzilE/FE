package com.example.fe.feature.solver.model

data class SolverDraft(
    val problemId: Int,
    val language: String,
    val code: String,
    val savedAt: Long
)