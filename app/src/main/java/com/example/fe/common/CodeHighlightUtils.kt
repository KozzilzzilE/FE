package com.example.fe.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

fun highlight(code: String, languageHint: String): AnnotatedString {
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

fun parseCodeFence(raw: String): Pair<String, String> {
    val lines = raw.trim().lines()
    if (lines.isEmpty()) return Pair("", raw)
    val firstLine = lines.first().trim()
    if (!firstLine.startsWith("```") && !firstLine.startsWith("~~~")) return Pair("", raw)
    val language = firstLine.trimStart('`', '~').trim()
    val endIdx = if (lines.last().trim() == "```" || lines.last().trim() == "~~~") lines.size - 1 else lines.size
    return Pair(language, lines.subList(1, endIdx).joinToString("\n"))
}
