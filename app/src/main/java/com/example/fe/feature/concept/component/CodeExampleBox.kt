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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.Cyan
import com.example.fe.ui.theme.TextMuted
import androidx.compose.ui.graphics.Color
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

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

internal fun parseCodeFence(raw: String): Pair<String, String> {
    val lines = raw.trim().lines()
    if (lines.isEmpty()) return Pair("", raw)
    val firstLine = lines.first().trim()
    if (!firstLine.startsWith("```") && !firstLine.startsWith("~~~")) return Pair("", raw)
    val language = firstLine.trimStart('`', '~').trim()
    val endIdx = if (lines.last().trim() == "```" || lines.last().trim() == "~~~") lines.size - 1 else lines.size
    return Pair(language, lines.subList(1, endIdx).joinToString("\n"))
}

internal fun highlight(code: String, languageHint: String): AnnotatedString {
    val syntaxLanguage = when (languageHint.lowercase()) {
        "java" -> SyntaxLanguage.JAVA
        "kotlin", "kt" -> SyntaxLanguage.KOTLIN
        "python", "py" -> SyntaxLanguage.PYTHON
        "javascript", "js" -> SyntaxLanguage.JAVASCRIPT
        "typescript", "ts" -> SyntaxLanguage.JAVASCRIPT
        "c" -> SyntaxLanguage.C
        "cpp", "c++" -> SyntaxLanguage.CPP
        "swift" -> SyntaxLanguage.SWIFT
        "go" -> SyntaxLanguage.GO
        "rust", "rs" -> SyntaxLanguage.RUST
        else -> SyntaxLanguage.DEFAULT
    }

    val highlights = Highlights.Builder()
        .code(code)
        .language(syntaxLanguage)
        .theme(SyntaxThemes.darcula())
        .build()

    return buildAnnotatedString {
        append(code)
        highlights.getHighlights()
            .filterIsInstance<ColorHighlight>()
            .forEach { h ->
                val r = (h.rgb shr 16) and 0xFF
                val g = (h.rgb shr 8) and 0xFF
                val b = h.rgb and 0xFF
                val luminance = (0.299f * r + 0.587f * g + 0.114f * b) / 255f
                if (luminance > 0.2f) {
                    addStyle(
                        style = SpanStyle(color = Color(r / 255f, g / 255f, b / 255f, 1f)),
                        start = h.location.start,
                        end = h.location.end
                    )
                }
            }
    }
}
