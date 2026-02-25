package com.example.fe.feature.solver.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.model.SubmissionRecord
import com.example.fe.feature.solver.model.TestCase

private enum class SubmitSubScreen {
    MAIN, TESTCASE, RESULT, SOLUTION
}

@Composable
fun SubmitTabContent(
    viewModel: SolverViewModel
) {
    var currentSubScreen by remember { mutableStateOf(SubmitSubScreen.MAIN) }

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        SubmitMenuGrid(
            selectedMenu = currentSubScreen,
            onMenuClick = { next ->
                currentSubScreen = next

                // 모범답안 자동
                if (next == SubmitSubScreen.SOLUTION) {
                    viewModel.loadSolution()
                }
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (currentSubScreen) {
                SubmitSubScreen.MAIN -> SubmitHistoryView(
                    viewModel = viewModel,
                    isSubmitting = uiState.isSubmitting
                )

                SubmitSubScreen.TESTCASE -> TestCaseTabContent(viewModel)

                SubmitSubScreen.RESULT -> ExecutionResultView(viewModel)

                // 해설 탭: viewModel.uiState.solution 쓰는 화면으로 연결
                SubmitSubScreen.SOLUTION -> ProblemSolutionTabContent(viewModel)
            }
        }
    }
}

// 상단 고정 메뉴
@Composable
private fun SubmitMenuGrid(
    selectedMenu: SubmitSubScreen,
    onMenuClick: (SubmitSubScreen) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SubmitMenuButton(
                title = "제출 및 결과",
                icon = Icons.Filled.Send,
                isSelected = selectedMenu == SubmitSubScreen.MAIN,
                modifier = Modifier.weight(1f),
                onClick = { onMenuClick(SubmitSubScreen.MAIN) }
            )
            SubmitMenuButton(
                title = "테스트케이스",
                icon = Icons.Filled.FactCheck,
                isSelected = selectedMenu == SubmitSubScreen.TESTCASE,
                modifier = Modifier.weight(1f),
                onClick = { onMenuClick(SubmitSubScreen.TESTCASE) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SubmitMenuButton(
                title = "실행 결과",
                icon = Icons.Filled.Terminal,
                isSelected = selectedMenu == SubmitSubScreen.RESULT,
                modifier = Modifier.weight(1f),
                onClick = { onMenuClick(SubmitSubScreen.RESULT) }
            )
            SubmitMenuButton(
                title = "문제 해설",
                icon = Icons.Filled.MenuBook,
                isSelected = selectedMenu == SubmitSubScreen.SOLUTION,
                modifier = Modifier.weight(1f),
                onClick = { onMenuClick(SubmitSubScreen.SOLUTION) }
            )
        }
    }
}

// 제출 및 최근 제출 내역 (※ '최근 제출 결과: 정답 ✅' 박스 삭제 버전)
@Composable
private fun SubmitHistoryView(
    viewModel: SolverViewModel,
    isSubmitting: Boolean
) {
    val submissions by viewModel.submissions.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = { if (!isSubmitting) viewModel.submitCode() }
        ) {
            Box(
                modifier = Modifier.background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF6397FF), Color(0xFF72C6B4))
                    )
                ),
                contentAlignment = Alignment.Center
            ) {
                if (isSubmitting) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "제출 중...",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        "제출하기",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            "최근 제출 내역",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF4B5563)
        )
        Spacer(modifier = Modifier.height(16.dp))

        submissions.forEach { record ->
            SubmissionRecordItem(record)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// 실행 결과 화면
@Composable
private fun ExecutionResultView(viewModel: SolverViewModel) {
    val executionResult by viewModel.executionResult.collectAsState()
    val testCases by viewModel.testCases.collectAsState()

    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(testCases.size) {
        if (selectedIndex > testCases.lastIndex) selectedIndex = 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (executionResult == null || testCases.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "실행 결과가 없습니다.\n에디터에서 코드를 실행해 보세요.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            return
        }

        val lines = executionResult!!
        val tc = testCases[selectedIndex]
        val terminalLines = buildLinesForUi(tc, lines)
        val passed = inferPassed(lines)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 4.dp, bottom = 80.dp)
        ) {
            ExecutionTerminal(
                title = "테스트 ${selectedIndex + 1}",
                passed = passed,
                lines = terminalLines,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 12.dp),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                testCases.forEachIndexed { idx, _ ->
                    val isSelected = idx == selectedIndex

                    Surface(
                        modifier = Modifier.size(width = 44.dp, height = 36.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF8199E5) else Color(0xFFF3F4F6),
                        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        onClick = { selectedIndex = idx }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${idx + 1}",
                                color = if (isSelected) Color.White else Color(0xFF4B5563),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// 각종 버튼
@Composable
private fun SubmitMenuButton(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) Color(0xFF8199E5) else Color.White
    val contentColor = if (isSelected) Color.White else Color(0xFF4B5563)

    Surface(
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SubmissionRecordItem(record: SubmissionRecord) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(record.date, fontSize = 12.sp, color = Color(0xFF9CA3AF))
                Text(record.language, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
            Text(
                text = record.result,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (record.isCorrect) Color(0xFF72C6B4) else Color(0xFFF04438)
            )
        }
    }
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