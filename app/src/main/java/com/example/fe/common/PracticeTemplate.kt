package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PracticeTemplate(
    title: String,
    onNavigate: (String) -> Unit,
    explanation: String,
    codeSample: String? = null,
    problem: String? = null, // 빈 값을 넣을 수 있게
    answer: String
) {
    Scaffold(
        topBar = {
            TopBar(
                title = title,
                onBackClick = { /* TODO */ },
                onHomeClick = { onNavigate("home") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "study",
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PracticeTemplatePreview() {
    PracticeTemplate(
        title = "개념 학습",
        onNavigate = {},
        explanation = "스택은 LIFO 구조입니다.",
        answer = "스택"
    )
}
