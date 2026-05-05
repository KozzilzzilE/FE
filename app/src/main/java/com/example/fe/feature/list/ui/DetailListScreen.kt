package com.example.fe.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.DetailCard
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.model.DetailItem
import com.example.fe.feature.list.model.Problem

@Composable
fun DetailListScreen(
    screenTitle: String, // 동적 타이틀 (예: 개념학습, 응용학습, 문제학습)
    items: List<DetailItem>, // 범용 리스트 (Concept, Application, Problem)
    onItemClick: (DetailItem) -> Unit, // 클릭 이벤트
    onBookmarkClick: ((DetailItem) -> Unit)? = null, // 북마크 클릭 이벤트
    onBackClick: () -> Unit, // 뒤로 가기 동작 연동
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = screenTitle,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(com.example.fe.ui.theme.BgPrimary)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        DetailCard(
                            item = item,
                            index = index + 1,
                            onClick = { onItemClick(item) },
                            onBookmarkClick = if (onBookmarkClick != null) { { onBookmarkClick(item) } } else null
                        )
                    }
                }
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailListScreenPreview() {
    val sampleItems = listOf(
        Problem(1L, "두 수의 합", Difficulty.EASY, false),
        Problem(2L, "스택 구현하기", Difficulty.MEDIUM, true),
        Problem(3L, "큐 활용하기", Difficulty.MEDIUM, false),
        Problem(4L, "힙 정렬", Difficulty.HARD, false),
        Problem(5L, "DFS 탐색", Difficulty.HARD, true),
    )
    
    DetailListScreen(
        screenTitle = "문제학습",
        items = sampleItems,
        onItemClick = {},
        onBackClick = {},
        onNavigate = {}
    )
}
