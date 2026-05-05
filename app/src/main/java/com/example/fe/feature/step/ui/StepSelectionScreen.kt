package com.example.fe.feature.step.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.list.component.getTopicIcon
import com.example.fe.feature.step.component.StepCard
import com.example.fe.navigation.Routes
import com.example.fe.ui.theme.*

private val accentAmber  = Primary                // #F59E0B
private val accentSalmon = Color(0xFFFFBFB2)
private val accentYellow = Color(0xFFF2F3C7)

private val bgAmber  = Color(0x1AF59E0B)          // 10% amber
private val bgSalmon = Color(0x1AFFBFB2)
private val bgYellow = Color(0x1AF2F3C7)

@Composable
fun StepSelectionScreen(
    topicId: Long = 1,
    topicName: String = "해시",
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BgPrimary,
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
        ) {
            // 헤더: ← + 토픽 배지
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // sssTopicBadge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AF59E0B))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = getTopicIcon(topicName),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = topicName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }

            // 콘텐츠
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "학습 단계를 선택하세요",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "순서대로 학습하면 실력이 빠르게 향상돼요",
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(2.dp))

                StepCard(
                    stepNumber = "01",
                    title = "개념 학습",
                    description = "슬라이드 형식으로 기초 지식 학습",
                    accentColor = accentAmber,
                    accentBg = bgAmber,
                    onClick = { onNavigate(Routes.detailList(topicId, topicName, "concept")) }
                )
                StepCard(
                    stepNumber = "02",
                    title = "응용 학습",
                    description = "빈칸 채우기 문제로 알고리즘 연습",
                    accentColor = accentSalmon,
                    accentBg = bgSalmon,
                    onClick = { onNavigate(Routes.detailList(topicId, topicName, "application")) }
                )
                StepCard(
                    stepNumber = "03",
                    title = "문제 학습",
                    description = "코드 에디터로 실제 문제 해결",
                    accentColor = accentYellow,
                    accentBg = bgYellow,
                    onClick = { onNavigate(Routes.detailList(topicId, topicName, "problem")) }
                )
            }
        }
    }
}
