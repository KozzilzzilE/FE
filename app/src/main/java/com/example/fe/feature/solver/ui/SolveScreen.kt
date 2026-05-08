package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.fe.feature.solver.SolveTab
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.DraftSaveButton
import com.example.fe.feature.solver.component.SmartKeyboardPanel
import com.example.fe.feature.solver.component.SubmitTabContent
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.TestCase
import com.example.fe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolveScreen(
    problemId: Long,
    viewModel: SolverViewModel,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onOpenEditorFull: (Long) -> Unit = {}
) {
    LaunchedEffect(problemId) {
        viewModel.loadProblemDetail(problemId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(SolveTab.PROBLEM) }

    val context = LocalContext.current
    LaunchedEffect(uiState.errorToast) {
        uiState.errorToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorToast()
        }
    }

    val codeFromVm = uiState.code
    var tfv by remember {
        mutableStateOf(TextFieldValue(text = codeFromVm, selection = TextRange(codeFromVm.length)))
    }

    LaunchedEffect(codeFromVm) {
        if (codeFromVm != tfv.text) {
            tfv = tfv.copy(text = codeFromVm, selection = TextRange(codeFromVm.length))
        }
    }

    var editorFocused by remember { mutableStateOf(false) }

    val executionLines = uiState.runResult?.terminalLines
    val testCases = uiState.testCases

    var selectedResultIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(testCases.size) {
        if (selectedResultIndex > testCases.lastIndex) selectedResultIndex = 0
    }

    val detail = uiState.problemDetail
    val titleToShow = detail?.title.orEmpty()
    val isBookmarked = detail?.isBookmarked == true
    val bookmarkCount = detail?.bookmarkCount ?: 0

    var dragSum by remember { mutableStateOf(0f) }
    val swipeThreshold = 80f

    fun goPrev() {
        selectedTab = when (selectedTab) {
            SolveTab.PROBLEM -> SolveTab.PROBLEM
            SolveTab.EDITOR -> SolveTab.PROBLEM
            SolveTab.SUBMIT -> SolveTab.EDITOR
        }
    }

    fun goNext() {
        selectedTab = when (selectedTab) {
            SolveTab.PROBLEM -> SolveTab.EDITOR
            SolveTab.EDITOR -> SolveTab.SUBMIT
            SolveTab.SUBMIT -> SolveTab.SUBMIT
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            containerColor = BgPrimary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 헤더
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgPrimary)
                        .statusBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로",
                                tint = TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = titleToShow.ifBlank { "문제 풀이" },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.toggleBookmark() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = "북마크",
                                    tint = if (isBookmarked) Primary else BgElevated,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "$bookmarkCount",
                                fontSize = 13.sp,
                                color = BgElevated
                            )
                        }
                    }
                }

                // 탭바
                SolveTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // 컨텐츠
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .pointerInput(selectedTab) {
                            detectHorizontalDragGestures(
                                onDragStart = { dragSum = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    dragSum += dragAmount
                                    change.consume()
                                },
                                onDragEnd = {
                                    when {
                                        dragSum > swipeThreshold -> goPrev()
                                        dragSum < -swipeThreshold -> goNext()
                                    }
                                    dragSum = 0f
                                }
                            )
                        }
                ) {
                    when (selectedTab) {
                        SolveTab.PROBLEM -> {
                            ProblemTab(
                                detail = detail,
                                isLoading = uiState.isLoadingProblem
                            )
                        }

                        SolveTab.EDITOR -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // edtStrip
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .background(BgSurface)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = uiState.language,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        DraftSaveButton(onClick = { viewModel.saveDraft() })
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = { onOpenEditorFull(problemId) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.OpenInFull,
                                                contentDescription = "전체화면",
                                                tint = TextSecondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                // 코드에디터 (남은 공간 전체)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(Color(0xFF1A1A1A))
                                ) {
                                    BasicTextField(
                                        value = tfv,
                                        onValueChange = { newValue ->
                                            tfv = newValue
                                            viewModel.updateCode(newValue.text)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                            .onFocusChanged { editorFocused = it.isFocused },
                                        textStyle = TextStyle(
                                            color = TextPrimary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 14.sp,
                                            lineHeight = 24.sp
                                        ),
                                        cursorBrush = SolidColor(Primary),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.None,
                                            autoCorrect = false,
                                            keyboardType = KeyboardType.Ascii,
                                            imeAction = ImeAction.None
                                        )
                                    )
                                }

                                // 하단 고정 버튼 (실행하기 + 제출하기)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(BgPrimary)
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.runCode()
                                            selectedTab = SolveTab.SUBMIT
                                        },
                                        enabled = !uiState.isRunning,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                    ) {
                                        if (uiState.isRunning) {
                                            CircularProgressIndicator(
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(16.dp),
                                                color = BgPrimary
                                            )
                                        } else {
                                            Text("▶  실행하기", color = BgPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            viewModel.submitCode()
                                            selectedTab = SolveTab.SUBMIT
                                        },
                                        enabled = !uiState.isSubmitting,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                    ) {
                                        Text("▶  제출하기", color = BgPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        SolveTab.SUBMIT -> {
                            SubmitTabContent(viewModel = viewModel)
                        }
                    }
                }
            }
        }

        // 스마트 키보드 (에디터 포커스 시)
        AnimatedVisibility(
            visible = (selectedTab == SolveTab.EDITOR) && editorFocused,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BgSurface,
                shadowElevation = 8.dp
            ) {
                SmartKeyboardPanel(
                    onInsert = { insert ->
                        val updated = insertIntoTextFieldValue(tfv, insert)
                        tfv = updated
                        viewModel.updateCode(updated.text)
                    },
                    onRun = {
                        viewModel.runCode()
                        selectedTab = SolveTab.SUBMIT
                    },
                    onSubmit = {
                        viewModel.submitCode()
                        selectedTab = SolveTab.SUBMIT
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SolveTabBar(
    selectedTab: SolveTab,
    onTabSelected: (SolveTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(BgPrimary)
            .border(BorderStroke(1.dp, BgElevated))
    ) {
        SolveTab.values().forEach { tab ->
            val selected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(tab) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = if (selected) Primary else TextMuted,
                    fontSize = 14.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
                if (selected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Primary)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProblemTab(
    detail: ProblemDetail?,
    isLoading: Boolean
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    if (detail == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("문제 정보를 불러오지 못했습니다.", color = TextMuted)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // probMeta: 난이도 태그
        if (!detail.difficultyLabel.isNullOrBlank() && detail.difficultyLabel != "-") {
            val diffLabelUpper = detail.difficultyLabel.uppercase()
            val diffText = when (diffLabelUpper) {
                "EASY" -> "쉬움"
                "MEDIUM", "NORMAL" -> "보통"
                "HARD" -> "어려움"
                else -> detail.difficultyLabel
            }
            val diffColor = when (diffLabelUpper) {
                "EASY" -> Success
                "MEDIUM", "NORMAL" -> Primary
                "HARD" -> Error
                else -> Error
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(diffText, color = diffColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // descCard
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = BgSurface,
            border = BorderStroke(1.dp, BgElevated)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("문제 설명", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(detail.description, fontSize = 13.sp, color = TextSecondary, lineHeight = 20.sp)
            }
        }

        HorizontalDivider(thickness = 1.dp, color = BgElevated)

        // ioCard
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = BgSurface,
            border = BorderStroke(1.dp, BgElevated)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("입출력 예시", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CodeBgDark)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("입력", color = Primary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(detail.exampleInput, color = TextPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("출력", color = Primary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(detail.exampleOutput, color = TextPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // constCard
        if (detail.constraints.isNotEmpty()) {
            HorizontalDivider(thickness = 1.dp, color = BgElevated)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgElevated)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("제약 조건", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        detail.constraints.forEach {
                            Text("• $it", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TestCaseSelectorBar(
    count: Int,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { idx ->
            val isSelected = idx == selectedIndex
            Surface(
                modifier = Modifier.size(width = 44.dp, height = 36.dp),
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) Primary else BgSurface,
                border = if (isSelected) null else BorderStroke(1.dp, BgDivider),
                onClick = { onSelect(idx) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${idx + 1}",
                        color = if (isSelected) BgPrimary else TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
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
    val newText = buildString(text.length + insert.length) {
        append(text.substring(0, start))
        append(insert)
        append(text.substring(end))
    }
    val newCursor = start + insert.length
    return value.copy(text = newText, selection = TextRange(newCursor))
}

private fun buildLinesForUi(tc: TestCase, rawLines: List<String>): List<String> {
    return buildList {
        add("$ input: ${tc.input}")
        add("$ expected: ${tc.expectedOutput}")
        addAll(rawLines)
    }
}

private fun inferPassed(rawLines: List<String>): Boolean? {
    val joined = rawLines.joinToString("\n").lowercase()
    return when {
        "fail" in joined || "error" in joined || "exception" in joined -> false
        "passed" in joined || "✓" in joined -> true
        else -> null
    }
}
