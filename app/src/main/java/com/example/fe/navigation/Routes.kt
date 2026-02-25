package com.example.fe.navigation

import android.net.Uri

object Routes {
    // Bottom / Top level
    const val HOME = "home"
    const val STUDY = "study"
    const val PROBLEM = "problem"
    const val MY = "my"

    // Args
    const val PROBLEM_ID = "problemId"

    // Patterns
    const val SOLVE_ROUTE = "solve/{$PROBLEM_ID}"
    const val EDITOR_ROUTE = "editor/{$PROBLEM_ID}"
    const val EDITOR_FULL_ROUTE = "editor_full/{$PROBLEM_ID}"

    // Builders
    fun solve(problemId: Long) = "solve/$problemId"
    fun editor(problemId: Long) = "editor/$problemId"
    fun editorFull(problemId: Long) = "editor_full/$problemId"
}
