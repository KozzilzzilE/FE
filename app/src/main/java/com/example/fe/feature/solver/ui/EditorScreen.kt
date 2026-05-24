package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.fe.common.TopBar
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.SmartKeyboardPanel
import com.example.fe.feature.solver.component.SoraCodeEditor
import com.example.fe.ui.theme.*

@Composable
fun EditorScreen(
    problemId: Long,
    viewModel: SolverViewModel,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onGoProblem: () -> Unit = {},
    onGoSubmit: () -> Unit = {},
    onFullscreenClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val titleToShow = uiState.problemDetail?.title.orEmpty()

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

    var codeEditor by remember { mutableStateOf<io.github.rosemoe.sora.widget.CodeEditor?>(null) }
    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) onFullscreenClick()
    }

    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Scaffold(
            containerColor = BgPrimary,
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopBar(
                    title = "문제 풀이",
                    showHomeIcon = true,
                    onBackClick = onBack,
                    onHomeClick = onHome
                )
            },
            bottomBar = {
                if (!isKeyboardVisible) {
                    StepIndicatorEditor(total = 3, current = 2)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (titleToShow.isNotBlank()) {
                    Text(
                        text = titleToShow,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                SolveTabBarForEditor(
                    onGoProblem = onGoProblem,
                    onGoSubmit = onGoSubmit,
                    onRun = { viewModel.runCode() },
                    onSubmit = { viewModel.submitCode() },
                    isSubmitting = uiState.isSubmitting,
                    isRunning = uiState.isRunning
                )
                HorizontalDivider(thickness = 1.dp, color = BgDivider)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    // 에디터 영역 — Surface 대신 Box+background 사용 (AndroidView 클리핑 문제 방지)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(CodeBgDark, RoundedCornerShape(18.dp))
                            .border(1.dp, BgDivider, RoundedCornerShape(18.dp))
                    ) {
                        // 전체화면 버튼
                        IconButton(
                            onClick = onFullscreenClick,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .zIndex(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.OpenInFull,
                                contentDescription = "전체화면",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        SoraCodeEditor(
                            code = uiState.code,
                            onCodeChange = { viewModel.updateCode(it) },
                            language = uiState.language.ifBlank { "JAVA" },
                            onEditorReady = { editor -> codeEditor = editor }
                        )
                    }

                    if (!isKeyboardVisible) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.runCode() },
                            enabled = !uiState.isRunning,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("▷ 실행", fontWeight = FontWeight.Bold, color = BgPrimary)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { viewModel.updateCode(uiState.problemDetail?.initialCode ?: "") },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BgDivider)
                        ) {
                            Text("초기화", color = TextPrimary)
                        }
                    }
                }
            }
        }

        // 키보드가 올라왔을 때 스마트 키보드 패널 표시
        AnimatedVisibility(
            visible = isKeyboardVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding(),
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(150)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(150))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BgSurface,
                shadowElevation = 16.dp
            ) {
                Column {
                    HorizontalDivider(color = BgDivider)
                    SmartKeyboardPanel(
                        onInsert = { insert -> codeEditor?.insertText(insert, insert.length) },
                        language = uiState.language,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SolveTabBarForEditor(
    onGoProblem: () -> Unit,
    onGoSubmit: () -> Unit,
    onRun: () -> Unit,
    onSubmit: () -> Unit,
    isRunning: Boolean,
    isSubmitting: Boolean
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 탭 칩 영역
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(BgElevated)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SolveTabChipStatic("문제", Icons.Filled.Description, selected = false, onClick = onGoProblem)
            SolveTabChipStatic("에디터", Icons.Filled.Edit, selected = true, onClick = {})
            SolveTabChipStatic("제출", Icons.Filled.CheckCircle, selected = false, onClick = onGoSubmit)
        }

        // 실행 버튼
        IconButton(
            onClick = { if (!isRunning) onRun() },
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isRunning) BgElevated else Primary)
        ) {
            if (isRunning) {
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

        // 제출 버튼
        IconButton(
            onClick = { if (!isSubmitting) onSubmit() },
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSubmitting) BgElevated else Primary)
        ) {
            if (isSubmitting) {
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
}

@Composable
private fun RowScope.SolveTabChipStatic(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) BgSurface else Color.Transparent,
        modifier = Modifier.weight(1f).height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon, label,
                tint = if (selected) TextPrimary else TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(label, color = if (selected) TextPrimary else TextMuted, fontSize = 14.sp)
        }
    }
}

@Composable
private fun StepIndicatorEditor(total: Int, current: Int) {
    Row(
        Modifier.fillMaxWidth().background(BgPrimary).padding(vertical = 10.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        for (i in 1..total) {
            val active = (i == current)
            Box(
                Modifier
                    .height(6.dp)
                    .width(if (active) 28.dp else 6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (active) Primary else BgElevated)
            )
            Spacer(Modifier.width(6.dp))
        }
    }
}
