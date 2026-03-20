package com.example.fe.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicList(
    viewModel: TopicViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF1F5F9), // 약간 그레이톤/푸르스름한 배경
        topBar = {
            TopAppBar(
                title = { 
                    Text("알고리즘 학습", fontWeight = FontWeight.Bold, fontSize = 20.sp) 
                },
                navigationIcon = {
                    IconButton(onClick = {}) { // 뒤로가기 버튼 유지 (홈으로 가거나 동작 안함 등)
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "topic",
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is TopicUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF4A90E2)
                    )
                }
                is TopicUiState.Error -> {
                    // 에러 발생 시 UI 표출 안함(요구사항)
                }
                is TopicUiState.Success -> {
                    val topics = state.topics
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topics) { topic ->
                            TopicCard(
                                title = topic.displayName,
                                onClick = {
                                    // 추후 알고리즘 상세 문제 목록 등으로 넘어갈 임시 동작
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "이동",
                tint = Color.Gray
            )
        }
    }
}
