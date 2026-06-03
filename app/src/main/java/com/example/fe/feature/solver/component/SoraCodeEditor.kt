package com.example.fe.feature.solver.component

import android.content.Context
import android.graphics.Typeface
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.SelectionChangeEvent
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

    // Tracks the last code value sent TO the ViewModel from this editor.
    // AtomicReference (not MutableState) so that setting it in subscribeEvent does NOT
    // trigger recomposition — which would cause update{} to run while the ViewModel still
    // holds the old code, making currentCode != lastCodeRef true and calling setText().
    val lastCodeRef = remember { java.util.concurrent.atomic.AtomicReference(code) }

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

                // 오버스크롤 완전 제거.
                // setEdgeEffectColor(투명) 만으로는 Android 12+ 의 stretch 오버스크롤
                // (콘텐츠가 물리적으로 늘어나 어두운 배경이 검게 보임)을 막지 못한다.
                // sora 는 EdgeEffect 필드(edgeEffectVertical/Horizontal)를 직접 그리므로,
                // 아무 동작도 하지 않는 EdgeEffect 로 두 필드를 교체해 글로우/stretch 를 원천 차단한다.
                overScrollMode = android.view.View.OVER_SCROLL_NEVER
                props.overScrollEnabled = false
                runCatching {
                    val editor = this
                    for (fieldName in listOf("edgeEffectVertical", "edgeEffectHorizontal")) {
                        CodeEditor::class.java.getDeclaredField(fieldName).apply {
                            isAccessible = true
                            set(editor, NoOverScrollEdgeEffect(context))
                        }
                    }
                }

                setEditorLanguage(JavaLanguage())
                setText(currentCode)

                // 코드 변경 → ViewModel 동기화.
                // 괄호 자동완성( (^) )과 중괄호 Enter 확장( {\n  ^\n} )은 sora-editor 내장 기능
                // (JavaLanguage 의 DefaultSymbolPairs + BraceHandler, props.symbolPairAutoCompletion=true)
                // 에 위임한다. 과거의 수동 ContentChangeEvent 핸들러는 내장 기능과 충돌하여
                // 닫는 괄호 중복 삽입 / 커서 튐을 유발했으므로 제거했다.
                subscribeEvent<ContentChangeEvent> { _, _ ->
                    val newCode = text.toString()
                    if (newCode != currentCode) {
                        lastCodeRef.set(newCode)
                        currentOnCodeChange(newCode)
                    }
                }

                // 커서 이동 시 부드럽게 화면 중앙 정렬
                // 타이핑으로 인한 변경(isSelected=false, 한 글자씩 이동)은 Sora가 자체 처리
                // 탭으로 커서 점프할 때만 수동으로 중앙 스크롤
                var lastScrollLine = -1
                subscribeEvent<SelectionChangeEvent> { event, _ ->
                    if (event.isSelected) return@subscribeEvent
                    val targetLine = cursor.leftLine
                    if (kotlin.math.abs(targetLine - lastScrollLine) < 2) return@subscribeEvent
                    lastScrollLine = targetLine
                    post {
                        try {
                            val lineHeightPx = rowHeight.toFloat()
                            val cursorY = targetLine * lineHeightPx
                            val visibleTop = scrollY.toFloat()
                            val visibleBottom = scrollY + height.toFloat()
                            // 커서가 화면 밖이거나 상단/하단 20% 안에 있으면 중앙으로 이동
                            if (cursorY < visibleTop + height * 0.2f || cursorY > visibleBottom - height * 0.2f) {
                                val targetScrollY = (cursorY - height * 0.35f).coerceAtLeast(0f).toInt()
                                android.animation.ObjectAnimator.ofInt(this, "scrollY", scrollY, targetScrollY)
                                    .apply {
                                        duration = 120
                                        interpolator = android.view.animation.DecelerateInterpolator()
                                        start()
                                    }
                            }
                        } catch (_: Exception) {}
                    }
                }

                setOnFocusChangeListener { _, hasFocus ->
                    currentOnFocusChange(hasFocus)
                    if (hasFocus) {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                    }
                }

                setOnClickListener {
                    requestFocus()
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }

                editorInstance = this
                onEditorReady(this)
            }
        },
        update = {}
    )

    // 외부에서 코드가 통째로 교체된 경우(언어 변경 / 서버 임시저장 코드 로드)에만 setText 한다.
    // 사용자의 타이핑으로 인한 변경은 에디터가 이미 화면에 반영하고 있으므로 건너뛴다.
    //  - code == 에디터 현재 내용  → 이미 동일, 무시
    //  - code == lastCodeRef       → 에디터가 직접 내보낸(중간) 값, 무시
    // 위 두 가드로 과거의 delay(300) 휴리스틱이 타이핑 중 잘못 setText 하여
    // 커서를 1번 줄로 튕기던 현상을 제거한다. 진짜 외부 교체일 때만 커서 위치를 보존하며 갱신.
    LaunchedEffect(code) {
        val editor = editorInstance ?: return@LaunchedEffect
        val current = editor.text.toString()
        if (code == current || code == lastCodeRef.get()) return@LaunchedEffect

        val oldLine = editor.cursor.leftLine
        val oldCol = editor.cursor.leftColumn
        editor.setText(code)
        lastCodeRef.set(code)
        runCatching {
            val ln = oldLine.coerceIn(0, (editor.text.lineCount - 1).coerceAtLeast(0))
            val cc = oldCol.coerceIn(0, editor.text.getColumnCount(ln))
            editor.setSelection(ln, cc)
        }
    }

    // 스마트 키보드 입력(SharedFlow 경로 — SolveScreen 등에서 사용)
    LaunchedEffect(insertTextEvent) {
        insertTextEvent?.collect { input ->
            editorInstance?.smartInsert(input)
        }
    }
}

/**
 * 스마트 키보드 등으로 텍스트를 삽입할 때 사용하는 공통 입력 함수.
 *
 * CodeEditor.insertText() 는 IME(commitText) 경로가 아니라서 sora 내장 괄호 페어링이
 * 동작하지 않는다. 그래서 여는 괄호/따옴표는 여기서 직접 짝을 함께 넣고 커서를 가운데에 둔다 → ( ^ )
 * 일반 키보드(IME)는 sora 내장 DefaultSymbolPairs 가 동일하게 처리하므로, 두 경로의 동작이 통일된다.
 */
fun CodeEditor.smartInsert(input: String) {
    val line = cursor.leftLine
    val col = cursor.leftColumn

    val closing = when (input) {
        "(" -> ")"
        "{" -> "}"
        "[" -> "]"
        "\"" -> "\""
        "'" -> "'"
        else -> null
    }

    if (closing != null) {
        text.insert(line, col, input + closing)
        runCatching { setSelection(line, col + 1) }
    } else {
        text.insert(line, col, input)
        val newlineCount = input.count { it == '\n' }
        val newLine = line + newlineCount
        val newCol = if (newlineCount > 0) input.substringAfterLast('\n').length else col + input.length
        runCatching { setSelection(newLine, newCol) }
    }
}

/**
 * 어떤 입력에도 반응하지 않는 EdgeEffect.
 * sora 의 오버스크롤(글로우 / Android 12+ stretch)을 완전히 비활성화하기 위해
 * CodeEditor 의 edgeEffectVertical / edgeEffectHorizontal 필드에 주입한다.
 */
private class NoOverScrollEdgeEffect(context: Context) : android.widget.EdgeEffect(context) {
    override fun onPull(deltaDistance: Float) {}
    override fun onPull(deltaDistance: Float, displacement: Float) {}
    override fun onPullDistance(deltaDistance: Float, displacement: Float): Float = 0f
    override fun onAbsorb(velocity: Int) {}
    override fun onRelease() {}
    override fun draw(canvas: android.graphics.Canvas): Boolean = false
    override fun isFinished(): Boolean = true
    override fun getDistance(): Float = 0f
}

fun CodeEditor.moveCursor(left: Boolean) {
    val line = cursor.leftLine
    val col = cursor.leftColumn

    val newLine: Int
    val newCol: Int
    try {
        if (left) {
            when {
                col > 0 -> { newLine = line; newCol = col - 1 }
                line > 0 -> { newLine = line - 1; newCol = text.getColumnCount(line - 1) }
                else -> return
            }
        } else {
            val lineLen = text.getColumnCount(line)
            when {
                col < lineLen -> { newLine = line; newCol = col + 1 }
                line < text.lineCount - 1 -> { newLine = line + 1; newCol = 0 }
                else -> return
            }
        }
    } catch (_: Exception) { return }

    try { setSelection(newLine, newCol) } catch (_: Exception) { return }

    // Sora Editor가 커서를 화면에 보이게 스크롤한 뒤, 커서가 뷰포트 40% 지점에 오도록 재조정
    post {
        try {
            val scaledDensity = resources.displayMetrics.scaledDensity
            val charWidthPx  = 16f * scaledDensity * 0.6f
            val lineHeightPx = 16f * scaledDensity * 1.8f  // 텍스트 크기 × 줄 간격

            // 가로: 커서가 뷰포트 40% 지점
            val targetX = (newCol * charWidthPx - width * 0.4f).coerceAtLeast(0f).toInt()

            // 세로: 커서가 뷰포트 상단 20% 지점 (커서 위 여백 확보)
            val targetY = (newLine * lineHeightPx - height * 0.2f).coerceAtLeast(0f).toInt()

            scrollTo(targetX, targetY)
        } catch (_: Exception) {}
    }
}
