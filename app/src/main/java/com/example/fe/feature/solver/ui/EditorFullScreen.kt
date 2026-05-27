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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import android.app.Activity
import android.view.WindowManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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

    var wasRunning by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isRunning) {
        if (wasRunning && !uiState.isRunning) {
            if (uiState.runResult != null) {
                viewModel.pendingNavigationTarget = "RESULT"
                onGoSubmit()
            }
        }
        wasRunning = uiState.isRunning
    }

    var wasSubmitting by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isSubmitting) {
        if (wasSubmitting && !uiState.isSubmitting) {
            if (uiState.submitResult != null) {
                viewModel.pendingNavigationTarget = "MAIN"
                onGoSubmit()
            }
        }
        wasSubmitting = uiState.isSubmitting
    }

    val activity = LocalContext.current as? Activity
    DisposableEffect(Unit) {
        val prev = activity?.window?.attributes?.softInputMode
            ?: WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        @Suppress("DEPRECATION")
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        onDispose {
            @Suppress("DEPRECATION")
            activity?.window?.setSoftInputMode(prev)
        }
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

                // 드래그 핸들 + 실행/제출 버튼 행
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 드래그 핸들 (패널 접기/펼치기)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
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

                    // 실행 버튼
                    IconButton(
                        onClick = { if (!uiState.isRunning) viewModel.runCode() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.isRunning) BgElevated else Primary)
                    ) {
                        if (uiState.isRunning) {
                            CircularProgressIndicator(
                                color = Primary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "실행",
                                tint = BgPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    // 제출 버튼
                    IconButton(
                        onClick = { if (!uiState.isSubmitting) viewModel.submitCode() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.isSubmitting) BgElevated else Primary)
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                color = Primary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "제출",
                                tint = BgPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = bottomBarExpanded,
                    enter = expandVertically(animationSpec = tween(180)),
                    exit = shrinkVertically(animationSpec = tween(180))
                ) {
                    SmartKeyboardPanel(
                        onInsert = { insert -> codeEditor?.insertText(insert, insert.length) },
                        language = uiState.language,
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
