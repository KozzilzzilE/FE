package com.example.fe.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.ProblemCard
import com.example.fe.common.bottomNavItems
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem

@Composable
fun ProblemListScreen(
    problems: List<Problem>, // 문제 리스트
    onProblemClick: (Problem) -> Unit, // 클릭이벤트
    onNavigate: (String) -> Unit, // 네비게이션
    onBackClick: () -> Unit // 뒤로 가기
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "알고리즘 학습",
                showBackIcon = false,
                onBackClick = onBackClick,
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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FB)) // Light background
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            problems.forEach { problem ->
                ProblemCard(
                    problem = problem,
                    onClick = { onProblemClick(problem) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProblemListScreenPreview() {
    val sampleProblems = listOf(
        Problem(1, "두 수의 합", Difficulty.EASY, false),
        Problem(2, "스택 구현하기", Difficulty.MEDIUM, true),
        Problem(3, "큐 활용하기", Difficulty.MEDIUM, false),
        Problem(4, "힙 정렬", Difficulty.HARD, false),
        Problem(5, "DFS 탐색", Difficulty.HARD, true),
    )
    
    ProblemListScreen(
        problems = sampleProblems,
        onProblemClick = {},
        onNavigate = {},
        onBackClick = {}
    )
}
