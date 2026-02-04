package com.example.fe.feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.CommonTopAppBar
import com.example.fe.common.ProblemCard
import com.example.fe.common.bottomNavItems
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem

@Composable
fun ProblemListScreen(
    problems: List<Problem>,
    onProblemClick: (Problem) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = "알고리즘 학습",
                canNavigateBack = true
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
                .background(Color(0xFFF7F9FB)) // Light background
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(problems) { problem ->
                    ProblemCard(
                        problem = problem,
                        onClick = { onProblemClick(problem) }
                    )
                }
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
        onNavigate = {}
    )
}
