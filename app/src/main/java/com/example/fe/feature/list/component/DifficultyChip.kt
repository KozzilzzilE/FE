package com.example.fe.feature.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DifficultyChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = CircleShape,
        color = if (selected) Color(0xFF7EA3F7) else Color.White,
        border = BorderStroke(
            1.dp,
            if (selected) Color(0xFF7EA3F7) else Color(0xFFE2E8F0)
        )
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF64748B),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DifficultyChipPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        DifficultyChip(
            text = "전체",
            selected = true,
            onClick = {}
        )
        DifficultyChip(
            text = "쉬움",
            selected = false,
            onClick = {}
        )
        DifficultyChip(
            text = "보통",
            selected = false,
            onClick = {}
        )
        DifficultyChip(
            text = "어려움",
            selected = false,
            onClick = {}
        )
    }
}