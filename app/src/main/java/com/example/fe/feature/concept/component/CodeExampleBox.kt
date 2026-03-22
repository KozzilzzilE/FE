package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 개념 학습 화면의 예제 코드 블록 (네이비 배경 + 모노스페이스 폰트)
 */
@Composable
fun CodeExampleBox(code: String) {
    Column {
        Text(
            text = "예제 코드",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7A828A),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E232E))
                .padding(20.dp)
        ) {
            Text(
                text = code,
                fontSize = 14.sp,
                color = Color(0xFFE2E8F0),
                lineHeight = 22.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
