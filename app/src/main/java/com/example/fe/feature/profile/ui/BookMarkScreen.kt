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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.ProblemListCard
import com.example.fe.common.TopBar
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.ui.AllProblemItem
import com.example.fe.feature.profile.BookmarkUiState
import com.example.fe.feature.profile.BookmarkViewModel

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel,
    onBackClick: () -> Unit,
    onProblemClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBookmarks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F8))
    ) {
        TopBar(
            title = "내가 찜한 문제",
            showBackIcon = true,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is BookmarkUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BookmarkUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is BookmarkUiState.Success -> {
                    if (state.bookmarks.isEmpty()) {
                        Text(
                            text = "찜한 문제가 없습니다.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = state.bookmarks,
                                key = { it.problemId }
                            ) { bookmarkItem ->
                                val difficultyEnum = when (bookmarkItem.difficulty) {
                                    "EASY" -> Difficulty.EASY
                                    "MEDIUM", "NORMAL" -> Difficulty.MEDIUM
                                    "HARD" -> Difficulty.HARD
                                    else -> Difficulty.EASY
                                }
                                val problem = AllProblemItem(
                                    problemId = bookmarkItem.problemId,
                                    title = bookmarkItem.title,
                                    difficulty = difficultyEnum,
                                    bookmarkCount = bookmarkItem.bookmarkCount,
                                    isBookmarked = true
                                )

                                ProblemListCard(
                                    problem = problem,
                                    onClick = { onProblemClick(problem.problemId) },
                                    onBookmarkClick = {
                                        viewModel.removeBookmark(problem.problemId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
