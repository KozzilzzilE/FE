package com.example.fe.feature.solver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary

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
            .background(CodeBgDark, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        // 헤더: 제목 + PASS/FAIL
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                color = TextPrimary,
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
                    color = if (passed == true) Success else Error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        lines.forEach { line ->
            val textColor = when {
                line.contains("✓") || line.contains("passed", ignoreCase = true) -> Success
                line.contains("fail", ignoreCase = true) || line.contains("error", ignoreCase = true) -> Error
                line.startsWith("$") -> TextMuted
                else -> TextPrimary
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
