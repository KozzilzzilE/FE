package com.example.fe.feature.solver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExecutionTerminal(
    title: String,
    passed: Boolean?,
    lines: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0E1627), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        // 헤더: 제목 + PASS/FAIL
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            val badgeText = when (passed) {
                true -> "PASS"
                false -> "FAIL"
                null -> ""
            }
            if (badgeText.isNotEmpty()) {
                Text(
                    text = badgeText,
                    color = if (passed == true) Color(0xFF72C6B4) else Color(0xFFF04438),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        lines.forEach { line ->
            val textColor = when {
                line.contains("✓") || line.contains("passed", ignoreCase = true) -> Color(0xFF72C6B4)
                line.contains("fail", ignoreCase = true) || line.contains("error", ignoreCase = true) -> Color(0xFFF04438)
                line.startsWith("$") -> Color(0xFF94A3B8)
                else -> Color.White
            }

            Text(
                text = line,
                color = textColor,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}
