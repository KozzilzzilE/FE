package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.SmartKeyboardPanel
import com.example.fe.feature.solver.component.SoraCodeEditor
import com.example.fe.ui.theme.*

@Composable
fun EditorFullScreen(
    problemId: Long,
    viewModel: SolverViewModel,
    onBack: () -> Unit = {},
    onGoSubmit: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var codeEditor by remember { mutableStateOf<io.github.rosemoe.sora.widget.CodeEditor?>(null) }
    var bottomBarExpanded by remember { mutableStateOf(true) }

    var wasSubmitting by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isSubmitting) {
        if (wasSubmitting && !uiState.isSubmitting) {
            if (uiState.submitResult != null) {
                onGoSubmit()
            }
        }
        wasSubmitting = uiState.isSubmitting
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CodeBgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
        ) {
            SoraCodeEditor(
                code = uiState.code,
                onCodeChange = { viewModel.updateCode(it) },
                language = uiState.language.ifBlank { "JAVA" },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onEditorReady = { editor -> codeEditor = editor }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSurface)
            ) {
                HorizontalDivider(color = BgDivider)

                // 드래그 핸들 — 탭하면 패널 접기/펼치기
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clickable { bottomBarExpanded = !bottomBarExpanded },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(TextMuted.copy(alpha = 0.4f))
                    )
                }

                AnimatedVisibility(
                    visible = bottomBarExpanded,
                    enter = expandVertically(animationSpec = tween(180)),
                    exit = shrinkVertically(animationSpec = tween(180))
                ) {
                    SmartKeyboardPanel(
                        onInsert = { insert -> codeEditor?.insertText(insert, insert.length) },
                        onRun = {
                            viewModel.pendingNavigationTarget = "RESULT"
                            viewModel.runCode()
                            onGoSubmit()
                        },
                        onSubmit = {
                            viewModel.submitCode()
                        },
                        isSubmitting = uiState.isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // 닫기 버튼 플로팅 (에디터 우상단)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(8.dp)
                .size(36.dp)
                .background(BgSurface.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                .zIndex(2f)
        ) {
            Icon(
                imageVector = Icons.Default.CloseFullscreen,
                contentDescription = "전체화면 닫기",
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
