package com.example.fe.feature.solver

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Send
import androidx.compose.ui.graphics.vector.ImageVector

enum class SolveTab(val label: String, val icon: ImageVector) {
    PROBLEM("문제", Icons.Outlined.Description),
    EDITOR("에디터", Icons.Outlined.Code),
    SUBMIT("제출", Icons.Outlined.Send)
}