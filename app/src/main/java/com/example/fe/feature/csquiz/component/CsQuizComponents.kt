package com.example.fe.feature.csquiz.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.csquiz.model.CsQuizQuestion
import com.example.fe.ui.theme.*

/**
 * 퀴즈 진행률 표시 (n / total + 퍼센트 바)
 */
@Composable
fun QuizProgressSection(current: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$current / $total",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(current.toFloat() / total * 100).toInt()}%",
                color = Primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { current.toFloat() / total },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(99.dp)),
            color = Primary,
            trackColor = BgElevated
        )
    }
}

/**
 * 문제 표시 카드
 */
@Composable
fun QuestionCard(question: CsQuizQuestion) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 180.dp),
        shape = RoundedCornerShape(20.dp),
        color = BgSurface
    ) {
        Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * O / X 선택 버튼
 */
@Composable
fun OxButtons(
    enabled: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { onSelect(true) },
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x2022C55E),
                contentColor = Success,
                disabledContainerColor = BgElevated,
                disabledContentColor = TextMuted
            )
        ) {
            Text("O", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = { onSelect(false) },
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x20FB2C36),
                contentColor = Error,
                disabledContainerColor = BgElevated,
                disabledContentColor = TextMuted
            )
        ) {
            Text("X", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * 정답/오답 피드백 카드
 */
@Composable
fun FeedbackCard(isCorrect: Boolean, explanation: String) {
    val bgColor = if (isCorrect) Color(0x2022C55E) else Color(0x20FB2C36)
    val borderColor = if (isCorrect) Success else Error
    val label = if (isCorrect) "✓ 정답!" else "✗ 오답"
    val labelColor = if (isCorrect) Success else Error

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                color = labelColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = explanation,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
