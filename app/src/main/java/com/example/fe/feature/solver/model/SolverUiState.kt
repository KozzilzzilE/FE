package com.example.fe.feature.solver.model

data class SolverUiState(
    val problemId: Long = 0L,
    val language: String = "JAVA",

    val isLoadingProblem: Boolean = false,
    val problemDetail: ProblemDetail? = null,

    val code: String = "",

    val testCases: List<TestCase> = emptyList(),

    val isRunning: Boolean = false,
    val runResult: RunResult? = null,

    val isSubmitting: Boolean = false,
    val submitResult: SubmitResult? = null,
    val submissions: List<SubmissionRecord> = emptyList(),

    val isLoadingSolution: Boolean = false,
    val solution: SolutionDetail? = null,

    val errorToast: String? = null
)
