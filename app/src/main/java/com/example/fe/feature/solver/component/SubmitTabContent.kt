package com.example.fe.feature.solver.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.model.SubmissionRecord
import com.example.fe.feature.solver.model.TestCase
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

enum class SubmitSubScreen {
    MAIN, TESTCASE, RESULT, SOLUTION
}

@Composable
fun SubmitTabContent(
    viewModel: SolverViewModel,
    currentSubScreen: SubmitSubScreen,
    onSubScreenChange: (SubmitSubScreen) -> Unit,
    onNextProblem: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // ersPills - 4개 탭 가로 선택
        ErsPills(
            selected = currentSubScreen,
            onSelect = { next ->
                onSubScreenChange(next)
                if (next == SubmitSubScreen.SOLUTION) {
                    viewModel.loadSolution()
                }
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (currentSubScreen) {
                SubmitSubScreen.MAIN -> SubmitResultView(
                    viewModel = viewModel,
                    isSubmitting = uiState.isSubmitting,
                    onViewSolution = { onSubScreenChange(SubmitSubScreen.SOLUTION) },
                    onNextProblem = onNextProblem
                )
                SubmitSubScreen.TESTCASE -> TestCaseTabContent(viewModel)
                SubmitSubScreen.RESULT -> ExecutionResultView(viewModel)
                SubmitSubScreen.SOLUTION -> ProblemSolutionTabContent(viewModel)
            }
        }
    }
}

@Composable
private fun ErsPills(
    selected: SubmitSubScreen,
    onSelect: (SubmitSubScreen) -> Unit
) {
    val items = listOf(
        SubmitSubScreen.MAIN to "제출결과",
        SubmitSubScreen.TESTCASE to "테스트케이스",
        SubmitSubScreen.RESULT to "실행결과",
        SubmitSubScreen.SOLUTION to "문제해설"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BgSurface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEach { (screen, label) ->
            val isSelected = selected == screen
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Primary else Color.Transparent)
                    .clickable { onSelect(screen) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) BgPrimary else TextMuted
                )
            }
        }
    }
}

@Composable
private fun SubmitResultView(
    viewModel: SolverViewModel,
    isSubmitting: Boolean,
    onViewSolution: () -> Unit,
    onNextProblem: () -> Unit
) {
    val submissions by viewModel.submissions.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val latestResult = uiState.submitResult
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // resCard - 최근 제출 결과 (항상 표시)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (latestResult != null || submissions.isNotEmpty()) {
                        val isCorrect = latestResult?.isCorrect ?: submissions.firstOrNull()?.isCorrect ?: false
                        Icon(
                            imageVector = if (isCorrect) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                            contentDescription = null,
                            tint = if (isCorrect) Success else Error,
                            modifier = Modifier.size(52.dp)
                        )
                        Text(
                            text = if (isCorrect) "정답입니다!" else "오답입니다.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) Success else Error
                        )
                        HorizontalDivider(thickness = 1.dp, color = BgElevated)
                    } else {
                        // 제출 전 placeholder
                        Icon(
                            imageVector = Icons.Outlined.HourglassEmpty,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "아직 제출하지 않았습니다",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Text(
                            text = "코드를 작성한 후 제출해 보세요",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { if (!isSubmitting) viewModel.submitCode() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            if (isSubmitting) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp),
                                        color = BgPrimary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("제출 중...", color = BgPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Text("제출하기", color = BgPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // histLbl - 제출 기록 (항상 표시)
            Text(
                text = "제출 기록",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            if (submissions.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    submissions.forEach { record ->
                        SubmissionRecordItem(record)
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = BgSurface
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "제출 기록이 없습니다",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // 하단 버튼 (제출 이력이 있을 때)
        if (submissions.isNotEmpty() || latestResult != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(BgPrimary)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { 
                        viewModel.loadSolution()
                        onViewSolution()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
                ) {
                    Text("해설 보기", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { onNextProblem() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text("다음 문제", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BgPrimary)
                }
            }
        }
    }
}

@Composable
private fun ExecutionResultView(viewModel: SolverViewModel) {
    val executionResult by viewModel.executionResult.collectAsState()
    val testCases by viewModel.testCases.collectAsState()

    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(testCases.size) {
        if (selectedIndex > testCases.lastIndex) selectedIndex = 0
    }

    if (testCases.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "테스트 케이스가 없습니다.",
                color = TextMuted,
                fontSize = 14.sp
            )
        }
        return
    }

    val hasResult = executionResult != null
    val tc = testCases[selectedIndex]
    
    val caseCount = testCases.size
    val inputText = tc.input
    val expectedText = tc.expectedOutput
    val myOutputText = if (hasResult) {
        executionResult!!.filter { !it.startsWith("$") }.joinToString("\n").ifEmpty { "(출력 없음)" }
    } else {
        "출력 없음"
    }
    val passed = if (hasResult) {
        myOutputText.trim() == expectedText.trim()
    } else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 4.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // caseRow — 케이스 선택 칩
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (0 until caseCount).forEach { idx ->
                val isSelected = idx == selectedIndex
                val chipBg = if (isSelected) Color(0x1A22C55E) else BgSurface
                val chipBorder = if (isSelected) Color(0xFF22C55E) else Color(0xFF44403C)
                
                // 결과가 있으면 결과에 따라, 없으면 기본 초록색 도트 표시
                val dotColor = when {
                    passed == null -> if (isSelected) Color(0xFF22C55E) else Color(0xFF78716C)
                    passed -> Color(0xFF22C55E)
                    else -> Color(0xFFEF4444)
                }
                
                val textColor = if (isSelected) Color(0xFF22C55E) else TextMuted

                Surface(
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = chipBg,
                    border = BorderStroke(1.dp, chipBorder),
                    onClick = { selectedIndex = idx }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(dotColor)
                        )
                        Text(
                            text = "Case ${idx + 1}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                }
            }
        }

        // statRow — 결과 상태 (결과가 있을 때만 표시)
        if (passed != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isCorrect = passed == true
                Icon(
                    imageVector = if (isCorrect) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                    contentDescription = null,
                    tint = if (isCorrect) Color(0xFF22C55E) else Error,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (isCorrect) "맞았습니다" else "틀렸습니다",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) Color(0xFF22C55E) else Error
                )
            }
        }

        // inCard — 입력 카드
        ExecInfoCard(
            label = "입력",
            content = inputText
        )

        // myCard — 나의 출력 카드
        ExecInfoCard(
            label = "나의 출력",
            content = myOutputText
        )

        // ansCard — 정답 카드
        ExecInfoCard(
            label = "정답",
            content = expectedText
        )
    }
}

/**
 * 피그마 inCard / myCard / ansCard 공통 카드
 * - 배경: #292524 (BgSurface), 테두리: #44403C, 둥글기: 12dp
 * - 라벨: 12sp SemiBold #78716C
 * - 값 박스: 배경 #1C1917 (BgPrimary), 둥글기: 8dp, 패딩: 10x12
 * - 값 텍스트: 12sp Regular #A8A29E
 */
@Composable
private fun ExecInfoCard(
    label: String,
    content: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = BgSurface,
        border = BorderStroke(1.dp, Color(0xFF44403C))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgPrimary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = content,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFA8A29E),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SubmissionRecordItem(record: SubmissionRecord) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = BgSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (record.isCorrect) "정답" else "오답",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (record.isCorrect) Success else Error,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = record.language,
                fontSize = 11.sp,
                color = TextMuted,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = record.date,
                fontSize = 12.sp,
                color = TextMuted
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
