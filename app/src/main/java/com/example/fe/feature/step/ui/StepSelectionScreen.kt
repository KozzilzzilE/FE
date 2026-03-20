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
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StepSelectionScreen(
    topicId: Long = 1,
    topicName: String = "해시",
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = topicName,
                subtitle = "학습 단계를 선택하세요",
                showBackIcon = false, // 사용자의 로직에 따라 뒤로가기 숨김 처리
                showHomeIcon = false, // 홈 버튼도 숨김
                onBackClick = { },
                onHomeClick = { }
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

@Composable
fun StepCard(
    title: String,
    description: String,
    backgroundColor: Color,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 아이콘 영역 (하얀 박스 배경)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 중앙 텍스트 컨텐츠
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            // 우측 화살표 아이콘
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "이동",
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepSelectionScreenPreview() {
    StepSelectionScreen(
        topicId = 1,
        topicName = "해시",
        onNavigate = {}
    )
}
