package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Cyan
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun ConceptDetailBox(text: String) {
    Column {
        Text(
            text = "상세 설명",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BgSurface)
                .padding(20.dp)
        ) {
            Markdown(
                content = text,
                colors = markdownColor(
                    text = TextPrimary,
                    codeText = Cyan,
                    codeBackground = BgElevated,
                    dividerColor = BgDivider
                ),
                typography = markdownTypography(
                    h3 = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    paragraph = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    ),
                    code = MaterialTheme.typography.bodySmall.copy(
                        color = Cyan,
                        fontSize = 13.sp
                    )
                )
            )
        }
    }
}
