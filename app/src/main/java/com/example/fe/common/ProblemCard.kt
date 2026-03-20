package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem
import com.example.fe.data.DetailItem

@Composable
fun DetailCard(
    item: DetailItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth() // 가로로 꽉 채움
            .padding(horizontal = 16.dp, vertical = 8.dp), // 가로 16dp, 세로 8dp 여백
        shape = RoundedCornerShape(24.dp), // 모서리 둥글게
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ), // 카드 색상
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // 그림자
    ) {
        Row( // 문제 번호, 문제 이름, 난이도, 해결 여부를 가로로 배치
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 문제 번호
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEBF2FA)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.id.toString(), // 문제 번호
                    color = Color(0xFF4A90E2), // 파란색
                    fontSize = 20.sp, // 글자 크기
                    fontWeight = FontWeight.Bold // 글자 굵기
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 문제 제목 & 난이도 수직으로 배치
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text( // 문제 제목
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                
                Spacer(modifier = Modifier.size(4.dp))
                
                DifficultyBadge(difficulty = item.difficulty) // 난이도
            }

            // 문제 해결 여부 아이콘
            Icon(
                imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = if (item.isCompleted) Color(0xFF4A90E2) else Color(0xFFE1E2E4),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY -> Color(0xFFE6F7F5)
        Difficulty.MEDIUM -> Color(0xFFFFF7E6)
        Difficulty.HARD -> Color(0xFFFFECEB)
    }
    val textColor = when (difficulty) {
        Difficulty.EASY -> Color(0xFF38B2AC)
        Difficulty.MEDIUM -> Color(0xFFD69E2E)
        Difficulty.HARD -> Color(0xFFE53E3E)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
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

@Preview(showBackground = true)
@Composable
fun DetailCardPreview() {
    Column(modifier = Modifier.background(Color(0xFFF7F9FB))) {
        DetailCard(
            item = Problem(1L, "두 수의 합", Difficulty.EASY, false),
            onClick = {}
        )
        DetailCard(
            item = Problem(2L, "스택 구현하기", Difficulty.MEDIUM, true),
            onClick = {}
        )
        DetailCard(
            item = Problem(3L, "스택의 특징", Difficulty.HARD, true),
            onClick = {}
        )
    }
}
