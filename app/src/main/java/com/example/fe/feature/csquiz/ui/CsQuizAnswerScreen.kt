package com.example.fe.feature.csquiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*

@Composable
fun CsQuizAnswerScreen(
    score: Int,
    total: Int,
    onRetry: () -> Unit = {},
    onNextQuiz: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val percentage = if (total > 0) score.toFloat() / total else 0f
    val resultEmoji = when {
        percentage >= 0.8f -> "🏆"
        percentage >= 0.6f -> "👍"
        percentage >= 0.4f -> "📚"
        else -> "💪"
    }
    val resultMessage = when {
        percentage >= 0.8f -> "훌륭해요! CS 지식이 탄탄하네요."
        percentage >= 0.6f -> "좋아요! 조금만 더 공부하면 완벽해요."
        percentage >= 0.4f -> "기초를 다시 복습해 보세요."
        else -> "개념을 처음부터 차근차근 학습해보세요!"
    }
    val scoreColor = when {
        percentage >= 0.8f -> Success
        percentage >= 0.6f -> Primary
        else -> Error
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 이모지
        Text(text = resultEmoji, fontSize = 72.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "퀴즈 완료!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = resultMessage,
            fontSize = 15.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 점수 카드
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = BgSurface
        ) {
            Column(
                modifier = Modifier.padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$score",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
                Text(
                    text = "/ $total 문제 정답",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 점수 바
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(8.dp)
                        .background(BgElevated, RoundedCornerShape(99.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage)
                            .background(scoreColor, RoundedCornerShape(99.dp))
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(percentage * 100).toInt()}점",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 버튼
        Button(
            onClick = onNextQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = BgPrimary
            )
        ) {
            Text("다음 문제 풀기", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BgDivider)
        ) {
            Text("다시 풀기", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BgDivider)
        ) {
            Text("홈으로", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }
    }
}
