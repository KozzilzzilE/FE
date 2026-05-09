package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.TopBar
import com.example.fe.data.dto.BookmarkItem
import com.example.fe.feature.profile.BookmarkUiState
import com.example.fe.feature.profile.BookmarkViewModel
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary

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
            .background(BgPrimary)
    ) {
        TopBar(
            title = "내가 찜한 문제",
            showBackIcon = true,
            onBackClick = onBackClick
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is BookmarkUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }
                is BookmarkUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = TextMuted
                    )
                }
                is BookmarkUiState.Success -> {
                    if (state.bookmarks.isEmpty()) {
                        Text(
                            text = "찜한 문제가 없습니다.",
                            modifier = Modifier.align(Alignment.Center),
                            color = TextMuted
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.bookmarks,
                                key = { it.problemId }
                            ) { item ->
                                BookmarkCard(
                                    item = item,
                                    onClick = { onProblemClick(item.problemId) },
                                    onBookmarkClick = { viewModel.removeBookmark(item.problemId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkCard(
    item: BookmarkItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    val difficultyEnum = when (item.difficultyDisplayName) {
        "쉬움" -> com.example.fe.feature.list.model.Difficulty.EASY
        "보통", "중간" -> com.example.fe.feature.list.model.Difficulty.MEDIUM
        "어려움" -> com.example.fe.feature.list.model.Difficulty.HARD
        else -> when (item.difficulty) {
            "EASY"             -> com.example.fe.feature.list.model.Difficulty.EASY
            "HARD"             -> com.example.fe.feature.list.model.Difficulty.HARD
            else               -> com.example.fe.feature.list.model.Difficulty.MEDIUM
        }
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            com.example.fe.common.DifficultyBadge(difficulty = difficultyEnum)

            Text(
                text = item.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "찜 해제",
                        tint = Primary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.bookmarkCount}",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = BgElevated,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
