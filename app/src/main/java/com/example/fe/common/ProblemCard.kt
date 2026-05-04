package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.list.model.DetailItem
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.model.Problem
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

@Composable
fun DetailCard(
    item: DetailItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 번호 뱃지
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgElevated),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.id.toString(),
                    color = Primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.size(4.dp))
                DifficultyBadge(difficulty = item.difficulty)
            }

            Icon(
                imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = if (item.isCompleted) Primary else BgDivider,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY   -> Color(0x2022C55E)  // Success 15%
        Difficulty.MEDIUM -> Color(0x20F59E0B)  // Primary 15%
        Difficulty.HARD   -> Color(0x20FB2C36)  // Error 15%
    }
    val textColor = when (difficulty) {
        Difficulty.EASY   -> Success
        Difficulty.MEDIUM -> Primary
        Difficulty.HARD   -> Error
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = difficulty.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun DetailCardPreview() {
    Column(modifier = Modifier.background(com.example.fe.ui.theme.BgPrimary)) {
        DetailCard(item = Problem(1L, "두 수의 합", Difficulty.EASY, false), onClick = {})
        DetailCard(item = Problem(2L, "스택 구현하기", Difficulty.MEDIUM, true), onClick = {})
        DetailCard(item = Problem(3L, "힙 정렬", Difficulty.HARD, false), onClick = {})
    }
}
