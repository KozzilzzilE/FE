package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.highlight
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun ConceptDetailBox(text: String) {
    val headingStyle = MaterialTheme.typography.titleMedium.copy(
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
    val paragraphStyle = MaterialTheme.typography.bodyMedium.copy(
        color = TextSecondary,
        fontSize = 13.sp,
        lineHeight = 20.sp
    )

    Markdown(
        content = text,
        colors = markdownColor(
            text = TextSecondary,
            codeText = androidx.compose.ui.graphics.Color.White,
            codeBackground = BgPrimary,
            dividerColor = BgElevated
        ),
        typography = markdownTypography(
            h1 = headingStyle.copy(fontSize = 18.sp),
            h2 = headingStyle.copy(fontSize = 16.sp),
            h3 = headingStyle.copy(fontSize = 14.sp),
            h4 = headingStyle.copy(fontSize = 13.sp),
            h5 = headingStyle.copy(fontSize = 13.sp),
            h6 = headingStyle.copy(fontSize = 13.sp),
            paragraph = paragraphStyle,
            code = MaterialTheme.typography.bodySmall.copy(
                color = androidx.compose.ui.graphics.Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            ),
            text = paragraphStyle
        ),
        components = markdownComponents(
            codeFence = { model ->
                MarkdownCodeFence(model.content, model.node) { code, lang ->
                    ConceptCodeBlock(code, lang ?: "")
                }
            },
            codeBlock = { model ->
                MarkdownCodeBlock(model.content, model.node) { code, lang ->
                    ConceptCodeBlock(code, lang ?: "")
                }
            }
        )
    )
}

@Composable
fun ConceptCodeBlock(code: String, language: String = "") {
    val highlighted = remember(code, language) { highlight(code, language) }
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
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 18.sp,
            softWrap = false
        )
    }
}
