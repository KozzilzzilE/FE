package com.example.fe.navigation

import android.net.Uri

object Routes {
    // Auth 영역
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val SOCIAL_SIGNUP = "social_signup"

    // Bottom / Top level
    const val HOME = "home"
    const val TOPIC = "topic" // (기존 problem_list 대체)
    const val STEP = "step"
    const val PROBLEM = "problem"
    const val MY = "my"

    // Args
    const val PROBLEM_ID = "problemId"
    const val TOPIC_ID = "topicId"
    const val TOPIC_NAME = "topicName"

    // Patterns
    const val STEP_ROUTE = "step/{$TOPIC_ID}/{$TOPIC_NAME}"
    const val SOLVE_ROUTE = "solve/{$PROBLEM_ID}"
    const val EDITOR_ROUTE = "editor/{$PROBLEM_ID}"
    const val EDITOR_FULL_ROUTE = "editor_full/{$PROBLEM_ID}"

    // Builders
    fun step(topicId: Long, topicName: String) = "step/$topicId/$topicName"
    fun solve(problemId: Long) = "solve/$problemId"
    fun editor(problemId: Long) = "editor/$problemId"
    fun editorFull(problemId: Long) = "editor_full/$problemId"
}
