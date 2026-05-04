package com.example.fe.feature.step.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.step.component.StepCard
import com.example.fe.navigation.Routes
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Blue
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted

@Composable
fun StepSelectionScreen(
    topicId: Long = 1,
    topicName: String = "해시",
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopBar(
                title = topicName,
                subtitle = "학습 단계를 선택하세요",
                showBackIcon = true,
                showHomeIcon = true,
                onBackClick = onNavigateBack,
                onHomeClick = { onNavigate(Routes.HOME) }
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
            StepCard(
                title = "개념 학습",
                description = "슬라이드 형식으로 기초 지식 학습",
                backgroundColor = BgSurface,
                icon = Icons.Outlined.LibraryBooks,
                iconTint = Blue,
                onClick = { onNavigate(Routes.detailList(topicId, topicName, "concept")) }
            )
            StepCard(
                title = "응용 학습",
                description = "빈칸 채우기와 Parsons 문제",
                backgroundColor = BgSurface,
                icon = Icons.Outlined.Lightbulb,
                iconTint = Success,
                onClick = { onNavigate(Routes.detailList(topicId, topicName, "application")) }
            )
            StepCard(
                title = "문제 학습",
                description = "코드 에디터로 실제 문제 해결",
                backgroundColor = BgSurface,
                icon = Icons.Outlined.Code,
                iconTint = Error,
                onClick = { onNavigate(Routes.detailList(topicId, topicName, "problem")) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgSurface)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💡 원하는 단계부터 시작할 수 있습니다",
                    fontSize = 14.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun StepSelectionScreenPreview() {
    StepSelectionScreen(topicId = 1, topicName = "해시", onNavigate = {}, onNavigateBack = {})
}
