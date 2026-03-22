package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 개념 학습 화면의 상세 설명 박스 (연하늘색 파스텔 배경)
 */
@Composable
fun ConceptDetailBox(text: String) {
    Column {
        Text(
            text = "상세 설명",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7A828A),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F8FF))
                .padding(20.dp)
        ) {
            Text(
                text = text,
                fontSize = 15.sp,
                color = Color(0xFF333333),
                lineHeight = 24.sp
            )
        }
    }
}
