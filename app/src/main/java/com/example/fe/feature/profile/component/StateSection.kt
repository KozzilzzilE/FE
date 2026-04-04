package com.example.fe.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatSection(
    rank: String,
    streak: String,
    solved: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            icon = "🏆",
            iconBg = Color(0xFFF9F2D9),
            label = "RANK",
            value = rank
        )
        StatItem(
            icon = "⚡",
            iconBg = Color(0xFFFBECEE),
            label = "STREAK",
            value = streak
        )
        StatItem(
            icon = "<>",
            iconBg = Color(0xFFEAF8F6),
            label = "SOLVED",
            value = solved
        )
    }
}

@Composable
private fun StatItem(
    icon: String,
    iconBg: Color,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(iconBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9CA3AF)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E2430)
        )
    }
}