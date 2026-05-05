package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.OpenInFull
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
import androidx.compose.ui.platform.LocalDensity
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
import com.example.fe.feature.solver.SolveTab
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.DraftSaveButton
import com.example.fe.feature.solver.component.ExecutionTerminal
import com.example.fe.feature.solver.component.SmartKeyboardPanel
import com.example.fe.feature.solver.component.SubmitTabContent
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.TestCase
import com.example.fe.common.TopBar
import com.example.fe.ui.theme.*
import kotlin.math.max

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
        viewModel.loadDraft(problemId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(SolveTab.PROBLEM) }

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

    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val navBottomPx = WindowInsets.navigationBars.getBottom(density)
    val smartPadBottomDp = with(density) { max(0, imeBottomPx - navBottomPx).toDp() }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopBar(
                    title = "문제 풀이",
                    subtitle = titleToShow,
                    showBackIcon = true,
                    showHomeIcon = true,
                    onBackClick = onBack,
                    onHomeClick = onHome,
                    rightContent = {
                        val isBookmarked = detail?.isBookmarked == true
                        val bookmarkCount = detail?.bookmarkCount ?: 0
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            IconButton(onClick = { viewModel.toggleBookmark() }) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = "즐겨찾기",
                                    tint = if (isBookmarked) Primary else BgElevated
                                )
                            }
                            Text(
                                text = "$bookmarkCount",
                                fontSize = 13.sp,
                                color = BgElevated
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (!(selectedTab == SolveTab.EDITOR && editorFocused)) {
                    StepIndicator(
                        total = 3,
                        current = when (selectedTab) {
                            SolveTab.PROBLEM -> 1
                            SolveTab.EDITOR -> 2
                            SolveTab.SUBMIT -> 3
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BgPrimary)
            ) {
                SolveTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
                HorizontalDivider(thickness = 1.dp, color = BgDivider)

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
                            val editorScrollState = rememberScrollState()

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(editorScrollState)
                            ) {
                                EditorTabContent(
                                    value = tfv,
                                    onValueChange = { newValue ->
                                        tfv = newValue
                                        viewModel.updateCode(newValue.text)
                                    },
                                    onExpand = { onOpenEditorFull(problemId) },
                                    onReset = { viewModel.updateCode("") },
                                    onRun = {
                                        viewModel.runCode()
                                        selectedTab = SolveTab.SUBMIT
                                    },
                                    onSaveDraft = { viewModel.saveDraft() },
                                    isRunning = uiState.isRunning,
                                    onEditorFocusChange = { focused ->
                                        editorFocused = focused
                                    }
                                )

                                if (executionLines != null && testCases.isNotEmpty()) {
                                    val tc = testCases[selectedResultIndex]
                                    val terminalLines = buildLinesForUi(tc, executionLines)
                                    val passed = inferPassed(executionLines)

                                    Column(modifier = Modifier.padding(16.dp)) {
                                        ExecutionTerminal(
                                            title = "테스트 ${selectedResultIndex + 1}",
                                            passed = passed,
                                            lines = terminalLines
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        TestCaseSelectorBar(
                                            count = testCases.size,
                                            selectedIndex = selectedResultIndex,
                                            onSelect = { selectedResultIndex = it }
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 8.dp,
                            end = 12.dp,
                            bottom = 0.dp
                        )
                )
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

@Composable
private fun EditorTabContent(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onExpand: () -> Unit,
    onReset: () -> Unit,
    onRun: () -> Unit,
    onSaveDraft: () -> Unit,
    isRunning: Boolean,
    onEditorFocusChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DraftSaveButton(
                onClick = onSaveDraft
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(18.dp),
            color = CodeBgDark,
            border = BorderStroke(1.dp, BgDivider)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .onFocusChanged { onEditorFocusChange(it.isFocused) },
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    cursorBrush = SolidColor(Primary),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.None
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(1f)
                ) {
                    IconButton(
                        onClick = onExpand,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.OpenInFull,
                            contentDescription = "Fullscreen",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRun,
            enabled = !isRunning,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (isRunning) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = BgPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("실행 중...", fontWeight = FontWeight.Bold, color = BgPrimary)
                }
            } else {
                Text("▷ 실행", fontWeight = FontWeight.Bold, color = BgPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BgDivider)
        ) { Text("초기화", color = TextPrimary) }
    }
}

@Composable
private fun SolveTabBar(
    selectedTab: SolveTab,
    onTabSelected: (SolveTab) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgElevated)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SolveTab.values().forEach { tab ->
            SolveTabChip(tab, selectedTab == tab) { onTabSelected(tab) }
        }
    }
}

@Composable
private fun RowScope.SolveTabChip(tab: SolveTab, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) TextPrimary else TextMuted
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) BgSurface else Color.Transparent,
        modifier = Modifier
            .weight(1f)
            .height(44.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = tab.label,
                color = color,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProblemTab(
    detail: ProblemDetail?,
    isLoading: Boolean
) {
    val scroll = rememberScrollState()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        if (!detail.difficultyLabel.isNullOrBlank() && detail.difficultyLabel != "-") {
            val diffLabelUpper = detail.difficultyLabel.uppercase()
            val diffText = when (diffLabelUpper) {
                "EASY" -> "쉬움"
                "MEDIUM", "NORMAL" -> "보통"
                "HARD" -> "어려움"
                else -> detail.difficultyLabel
            }
            val diffColor = when (diffLabelUpper) {
                "EASY", "쉬움" -> Color(0xFF10B981)
                "MEDIUM", "NORMAL", "보통" -> Color(0xFFF59E0B)
                "HARD", "어려움" -> Color(0xFFEF4444)
                else -> Color(0xFFF04438)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("난이도: ", color = TextSecondary, fontSize = 14.sp)
                Text(
                    text = diffText,
                    color = diffColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = BgSurface,
            border = BorderStroke(1.dp, BgDivider)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("문제 설명", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                Text(detail.description, lineHeight = 22.sp, fontSize = 15.sp, color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "입출력 예시",
            fontWeight = FontWeight.SemiBold,
            color = TextMuted,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = BgSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("입력:", color = TextMuted, fontSize = 13.sp)
                Text(detail.exampleInput, fontSize = 14.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                Text("출력:", color = TextMuted, fontSize = 13.sp)
                Text(detail.exampleOutput, fontSize = 14.sp, color = TextPrimary)
            }
        }

        if (detail.constraints.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0x20FB2C36),
                border = BorderStroke(1.dp, Color(0x40FB2C36))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    detail.constraints.forEach {
                        Text("• $it", color = Error, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(
    total: Int,
    current: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(if (i + 1 == current) 32.dp else 8.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (i + 1 == current) Primary else BgElevated)
            )
        }
    }
}

private fun insertIntoTextFieldValue(
    value: TextFieldValue,
    insert: String
): TextFieldValue {
    val text = value.text
    val start = value.selection.start.coerceIn(0, text.length)
    val end = value.selection.end.coerceIn(0, text.length)

    val newText = buildString(text.length + insert.length) {
        append(text.substring(0, start))
        append(insert)
        append(text.substring(end))
    }

    val newCursor = start + insert.length
    return value.copy(
        text = newText,
        selection = TextRange(newCursor)
    )
}

private fun buildLinesForUi(
    tc: TestCase,
    rawLines: List<String>
): List<String> {
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
