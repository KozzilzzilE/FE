package com.example.fe.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

private val KeywordColor = Color(0xFFCC7832)
private val StringColor  = Color(0xFF6A8759)
private val CommentColor = Color(0xFF808080)
private val NumberColor  = Color(0xFF6897BB)

private val JAVA_KEYWORDS = setOf(
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
    "class", "const", "continue", "default", "do", "double", "else", "enum",
    "extends", "final", "finally", "float", "for", "goto", "if", "implements",
    "import", "instanceof", "int", "interface", "long", "native", "new", "null",
    "package", "private", "protected", "public", "return", "short", "static",
    "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
    "transient", "try", "var", "void", "volatile", "while", "true", "false",
    "String", "System", "println", "print"
)

private val KOTLIN_KEYWORDS = setOf(
    "abstract", "actual", "as", "break", "by", "catch", "class", "companion",
    "const", "constructor", "continue", "crossinline", "data", "delegate", "do",
    "dynamic", "else", "enum", "expect", "external", "false", "field", "file",
    "final", "finally", "for", "fun", "get", "if", "import", "in", "infix",
    "init", "inline", "inner", "interface", "internal", "is", "it", "lateinit",
    "noinline", "null", "object", "open", "operator", "out", "override", "package",
    "param", "private", "property", "protected", "public", "receiver", "reified",
    "return", "sealed", "set", "super", "suspend", "tailrec", "this", "throw",
    "true", "try", "typealias", "typeof", "val", "var", "vararg", "when", "where",
    "while", "String", "Int", "Long", "Double", "Float", "Boolean", "Unit", "Any"
)

private val PYTHON_KEYWORDS = setOf(
    "False", "None", "True", "and", "as", "assert", "async", "await", "break",
    "class", "continue", "def", "del", "elif", "else", "except", "finally",
    "for", "from", "global", "if", "import", "in", "is", "lambda", "nonlocal",
    "not", "or", "pass", "raise", "return", "try", "while", "with", "yield",
    "print", "len", "range", "self"
)

fun highlight(code: String, languageHint: String): AnnotatedString {
    val keywords = when (languageHint.lowercase()) {
        "kotlin", "kt" -> KOTLIN_KEYWORDS
        "python", "py" -> PYTHON_KEYWORDS
        else -> JAVA_KEYWORDS
    }

    // colorAt[i] = 해당 인덱스에 적용할 색상 (우선순위 높을수록 나중에 덮어씀)
    val colorAt = arrayOfNulls<Color>(code.length)

    // 숫자
    Regex("\\b\\d+\\.?\\d*\\b").findAll(code).forEach { m ->
        for (i in m.range) colorAt[i] = NumberColor
    }

    // 키워드
    keywords.forEach { kw ->
        Regex("\\b${Regex.escape(kw)}\\b").findAll(code).forEach { m ->
            for (i in m.range) colorAt[i] = KeywordColor
        }
    }

    // 문자열 (키워드보다 우선)
    Regex(""""[^"\n\\]*(?:\\.[^"\n\\]*)*"""").findAll(code).forEach { m ->
        for (i in m.range) colorAt[i] = StringColor
    }

    // 주석 (최우선)
    Regex("//[^\n]*").findAll(code).forEach { m ->
        for (i in m.range) colorAt[i] = CommentColor
    }

    // 연속된 같은 색상끼리 span 하나로 묶어서 AnnotatedString 생성
    return buildAnnotatedString {
        append(code)
        var i = 0
        while (i < code.length) {
            val color = colorAt[i]
            if (color != null) {
                var j = i + 1
                while (j < code.length && colorAt[j] == color) j++
                addStyle(SpanStyle(color = color), i, j)
                i = j
            } else {
                i++
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
