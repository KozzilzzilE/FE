package com.example.fe.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeSection(
    userName: String,
    modifier: Modifier = Modifier
) {
    // 랜덤 환영 문구 리스트
    val welcomeMessages = listOf(
        "오늘도 코딩 정복해볼까요? 💪",
        "꾸준함이 곧 실력입니다. 열공하세요! ✨",
        "반가워요! 오늘은 어떤 문제를 풀어볼까요? 🧐",
        "실력이 쑥쑥 늘어날 준비 되셨나요? 🔥",
        "작은 코드 한 줄이 큰 변화의 시작입니다. 🌱"
    )

    // 리콤포지션 시에도 문구가 고정되도록 remember 사용 (화면 진입 시 결정)
    val selectedMessage = remember { welcomeMessages.random() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        // 유저 이름 (강조)
        Text(
            text = userName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1F2937)
        )
        
        // 환영 문구 (랜덤)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color(0xFF4A90E2), 
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )) {
                        append(selectedMessage.take(selectedMessage.length - 2)) // 이모지 제외 텍스트
                    }
                    withStyle(style = SpanStyle(fontSize = 24.sp)) {
                        append(selectedMessage.takeLast(2)) // 이모지 부분
                    }
                },
                lineHeight = 34.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeSectionPreview() {
    WelcomeSection(userName = "김개발")
}
