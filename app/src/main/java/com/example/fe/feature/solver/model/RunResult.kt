package com.example.fe.feature.solver.model

data class RunResult(
    val passed: Boolean? = null,
    val runtimeMs: Long? = null,
    val errorMessage: String? = null,
    val rawOutput: String? = null,
    val terminalLines: List<String> = emptyList()
)
