package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.fe.common.TopBar
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.SmartKeyboardPanel

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
    LaunchedEffect(problemId) { viewModel.loadProblemDetail(problemId) }

    val uiState by viewModel.uiState.collectAsState()
    val titleToShow = uiState.problemDetail?.title.orEmpty()
    val codeFromVm = uiState.code

    var tfv by remember {
        mutableStateOf(TextFieldValue(codeFromVm, selection = TextRange(codeFromVm.length)))
    }

    LaunchedEffect(codeFromVm) {
        if (codeFromVm != tfv.text) {
            tfv = tfv.copy(text = codeFromVm, selection = TextRange(codeFromVm.length))
        }
    }

    var editorFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
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
                if (!isKeyboardVisible && !editorFocused) {
                    StepIndicator(total = 3, current = 2)
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
                        color = Color(0xFF8A94A6),
                        fontWeight = FontWeight.Medium
                    )
                }

                SolveTabBarForEditor(onGoProblem = onGoProblem, onGoSubmit = onGoSubmit)
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE2E8F0))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF0E1627),
                        border = BorderStroke(1.dp, Color(0xFF1C2A44))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
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
                                    tint = Color(0xFFE6EDF7),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            BasicTextField(
                                value = tfv,
                                onValueChange = { newValue ->
                                    tfv = newValue
                                    viewModel.updateCode(newValue.text)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { editorFocused = it.isFocused },
                                textStyle = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = Color(0xFFE6EDF7),
                                    lineHeight = 20.sp
                                ),
                                cursorBrush = SolidColor(Color.White),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.None,
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.None
                                )
                            )
                        }
                    }

                    if (!isKeyboardVisible && !editorFocused) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.runCode(); onGoSubmit() },
                            enabled = !uiState.isRunning,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CC8B8))
                        ) {
                            Text("▷ 실행", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { viewModel.updateCode(uiState.problemDetail?.initialCode ?: "") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Text("초기화", color = Color.Black)
                        }
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = editorFocused || isKeyboardVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding(),
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(150)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(150))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column {
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    SmartKeyboardPanel(
                        onInsert = { insert ->
                            val updated = insertIntoTextFieldValue(tfv, insert)
                            tfv = updated
                            viewModel.updateCode(updated.text)
                            focusRequester.requestFocus()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            // ✅ 패널이 떠보이면 padding을 0에 가깝게
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private fun insertIntoTextFieldValue(value: TextFieldValue, insert: String): TextFieldValue {
    val text = value.text
    val start = value.selection.start.coerceIn(0, text.length)
    val end = value.selection.end.coerceIn(0, text.length)
    val newText = text.substring(0, start) + insert + text.substring(end)
    val newCursor = start + insert.length
    return value.copy(text = newText, selection = TextRange(newCursor))
}

@Composable
private fun SolveTabBarForEditor(onGoProblem: () -> Unit, onGoSubmit: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEFF2F6))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SolveTabChipStatic("문제", Icons.Filled.Description, selected = false, onClick = onGoProblem)
        SolveTabChipStatic("에디터", Icons.Filled.Edit, selected = true, onClick = {})
        SolveTabChipStatic("제출", Icons.Filled.CheckCircle, selected = false, onClick = onGoSubmit)
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
        color = if (selected) Color.White else Color.Transparent,
        modifier = Modifier
            .weight(1f)
            .height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, label, tint = if (selected) Color.Black else Color.Gray, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, color = if (selected) Color.Black else Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun StepIndicator(total: Int, current: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
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
                    .background(if (active) Color(0xFF4C83FF) else Color(0xFFD7E2FF))
            )
            Spacer(Modifier.width(6.dp))
        }
    }
}