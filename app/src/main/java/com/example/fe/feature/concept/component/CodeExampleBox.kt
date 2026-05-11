package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.highlight
import com.example.fe.common.parseCodeFence
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.TextMuted

@Composable
fun CodeExampleBox(code: String) {
    val (language, cleanCode) = remember(code) { parseCodeFence(code) }
    val highlighted = remember(cleanCode, language) { highlight(cleanCode, language) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (language.isNotBlank()) {
            Text(text = language, fontSize = 11.sp, color = TextMuted)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BgPrimary)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = highlighted,
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily.Monospace,
                softWrap = false
            )
        }
    }
}
