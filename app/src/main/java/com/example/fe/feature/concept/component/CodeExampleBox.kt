package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Cyan
import com.example.fe.ui.theme.TextMuted

@Composable
fun CodeExampleBox(code: String) {
    Column {
        Text(
            text = "예제 코드",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CodeBgDark)
                .padding(20.dp)
        ) {
            Text(
                text = code,
                fontSize = 14.sp,
                color = Cyan,
                lineHeight = 22.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
