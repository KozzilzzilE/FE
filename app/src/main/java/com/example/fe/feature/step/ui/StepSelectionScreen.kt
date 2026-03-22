package com.example.fe.feature.step.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.step.component.StepCard
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StepSelectionScreen(
    topicId: Long = 1,
    topicName: String = "해시",
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = topicName,
                subtitle = "학습 단계를 선택하세요",
                showBackIcon = true,
                showHomeIcon = true,
                onBackClick = onNavigateBack,
                onHomeClick = { onNavigate(com.example.fe.navigation.Routes.HOME) }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 개념 학습 카드
            StepCard(
                title = "개념 학습",
                description = "슬라이드 형식으로 기초 지식 학습",
                backgroundColor = Color(0xFFE3EDFA), // 연파랑
                icon = Icons.Outlined.LibraryBooks,
                iconTint = Color(0xFF4A90E2),
                onClick = {
                    onNavigate(com.example.fe.navigation.Routes.detailList(topicId, topicName, "concept"))
                }
            )

            // 응용 학습 카드
            StepCard(
                title = "응용 학습",
                description = "빈칸 채우기와 Parsons 문제",
                backgroundColor = Color(0xFFE6F5F1), // 연초록
                icon = Icons.Outlined.Lightbulb,
                iconTint = Color(0xFF26B685),
                onClick = {
                    onNavigate(com.example.fe.navigation.Routes.detailList(topicId, topicName, "application"))
                }
            )

            // 문제 학습 카드
            StepCard(
                title = "문제 학습",
                description = "코드 에디터로 실제 문제 해결",
                backgroundColor = Color(0xFFFDECEA), // 연분홍
                icon = Icons.Outlined.Code,
                iconTint = Color(0xFFE25B50),
                onClick = {
                    onNavigate(com.example.fe.navigation.Routes.detailList(topicId, topicName, "problem"))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 안내 패널
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F8FB))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💡 원하는 단계부터 시작할 수 있습니다",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepSelectionScreenPreview() {
    StepSelectionScreen(
        topicId = 1,
        topicName = "해시",
        onNavigate = {},
        onNavigateBack = {}
    )
}

