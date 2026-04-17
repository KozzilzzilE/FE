package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.ProblemListCard
import com.example.fe.common.TopBar
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.ui.AllProblemItem

@Composable
fun FavoriteProblemsScreen(
    onBackClick: () -> Unit,
    onProblemClick: (Long) -> Unit
) {
    val favoriteProblems = remember {
        mutableStateListOf(
            AllProblemItem(
                problemId = 1L,
                title = "배열 두 배 만들기",
                difficulty = Difficulty.EASY,
                bookmarkCount = 120,
                isBookmarked = true
            ),
            AllProblemItem(
                problemId = 4L,
                title = "특정 문자 제거하기",
                difficulty = Difficulty.HARD,
                bookmarkCount = 45,
                isBookmarked = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F8))
    ) {
        TopBar(
            title = "내가 찜한 문제",
            showBackIcon = false,
            onBackClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = favoriteProblems,
                key = { it.problemId }
            ) { problem ->
                ProblemListCard(
                    problem = problem,
                    onClick = { onProblemClick(problem.problemId) },
                    onBookmarkClick = {
                        favoriteProblems.remove(problem)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteProblemsScreenPreview() {
    FavoriteProblemsScreen(
        onBackClick = {},
        onProblemClick = {}
    )
}