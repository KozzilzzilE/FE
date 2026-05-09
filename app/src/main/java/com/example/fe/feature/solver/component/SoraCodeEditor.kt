package com.example.fe.feature.solver.component

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.langs.java.JavaLanguage
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import io.github.rosemoe.sora.widget.schemes.SchemeDarcula
import io.github.rosemoe.sora.widget.subscribeEvent

@Composable
fun SoraCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    language: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    onEditorReady: (CodeEditor) -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {},
    insertTextEvent: kotlinx.coroutines.flow.SharedFlow<String>? = null
) {
    val bgColor = CodeBgDark.toArgb()
    val primaryColor = Primary.toArgb()
    val textColor = TextPrimary.toArgb()

    val currentOnCodeChange by rememberUpdatedState(onCodeChange)
    val currentCode by rememberUpdatedState(code)
    val currentOnFocusChange by rememberUpdatedState(onFocusChange)
    
    var editorInstance by remember { mutableStateOf<CodeEditor?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            CodeEditor(context).apply {
                val jetbrainsMono = runCatching {
                    Typeface.createFromAsset(context.assets, "fonts/JetBrainsMono-Regular.ttf")
                }.getOrElse { Typeface.MONOSPACE }
                typefaceText = jetbrainsMono
                setTextSize(16f)
                isWordwrap = false

                val scheme = SchemeDarcula()
                scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, bgColor)
                scheme.setColor(EditorColorScheme.TEXT_NORMAL, textColor)
                scheme.setColor(EditorColorScheme.SELECTION_HANDLE, primaryColor)
                scheme.setColor(EditorColorScheme.SELECTION_INSERT, primaryColor)
                colorScheme = scheme

                setEditorLanguage(JavaLanguage())
                setText(currentCode)

                // 코드 변경 → ViewModel 동기화
                subscribeEvent<ContentChangeEvent> { _, _ ->
                    val newCode = text.toString()
                    if (newCode != currentCode) {
                        currentOnCodeChange(newCode)
                    }
                }

                // 스마트 중괄호 Enter: { + Enter → {\n    |\n}
                var processingSmartBrace = false
                subscribeEvent<ContentChangeEvent> { event, _ ->
                    if (processingSmartBrace) return@subscribeEvent
                    if (event.action != ContentChangeEvent.ACTION_INSERT) return@subscribeEvent
                    if (!event.changedText.toString().startsWith("\n")) return@subscribeEvent
                    if (event.changeStart.line + 1 >= text.lineCount) return@subscribeEvent

                    val insertedLine = event.changeStart.line
                    val lineBefore = text.getLine(insertedLine).toString()
                    val lineAfter  = text.getLine(insertedLine + 1).toString()

                    val indent      = lineBefore.takeWhile { it == ' ' || it == '\t' }
                    val innerIndent = "$indent    "

                    // Case A: {|} → \n 삽입 후 lineBefore="{", lineAfter="}"
                    val caseA = lineBefore.trimEnd().endsWith("{") &&
                                lineAfter.trimStart().startsWith("}")
                    // Case B: {}| → \n 삽입 후 lineBefore="{}", lineAfter=""
                    val caseB = !caseA && lineBefore.trimEnd().endsWith("{}")

                    if (!caseA && !caseB) return@subscribeEvent

                    post {
                        if (processingSmartBrace) return@post
                        processingSmartBrace = true
                        try {
                            if (caseA) {
                                // } 의 들여쓰기가 부족하면 맞춤
                                val closingIndent = lineAfter.takeWhile { it == ' ' || it == '\t' }
                                if (closingIndent.length < indent.length) {
                                    if (closingIndent.isNotEmpty()) {
                                        text.delete(insertedLine + 1, 0,
                                                    insertedLine + 1, closingIndent.length)
                                    }
                                    if (indent.isNotEmpty()) {
                                        text.insert(insertedLine + 1, 0, indent)
                                    }
                                }
                                // } 앞에 빈 들여쓰기 줄 삽입
                                text.insert(insertedLine + 1, 0, "$innerIndent\n")
                                setSelection(insertedLine + 1, innerIndent.length)
                            } else {
                                // lineBefore 끝 } 제거 후 새 두 줄 삽입
                                val braceCol = lineBefore.trimEnd().length - 1
                                text.delete(insertedLine, braceCol, insertedLine, braceCol + 1)
                                text.insert(insertedLine + 1, 0, "$innerIndent\n$indent}")
                                setSelection(insertedLine + 1, innerIndent.length)
                            }
                        } finally {
                            processingSmartBrace = false
                        }
                    }
                }

                setOnFocusChangeListener { _, hasFocus ->
                    currentOnFocusChange(hasFocus)
                }

                editorInstance = this
                onEditorReady(this)
            }
        },
        update = { editor ->
            if (editor.text.toString() != currentCode) {
                val line = editor.cursor.leftLine
                val col  = editor.cursor.leftColumn
                editor.setText(currentCode)
                try { editor.setSelection(line, col) } catch (_: Exception) {}
            }
        }
    )

    LaunchedEffect(insertTextEvent) {
        insertTextEvent?.collect { text ->
            val editor = editorInstance
            if (editor != null) {
                val line = editor.cursor.leftLine
                val col = editor.cursor.leftColumn
                editor.text.insert(line, col, text)
                try { editor.setSelection(line, col + text.length) } catch (_: Exception) {}
            }
        }
    }
}
