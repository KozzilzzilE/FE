package com.example.fe.feature.csquiz.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*

// ─── 데이터 모델 ───────────────────────────────────────────────
data class CsQuizQuestion(
    val id: Int,
    val question: String,
    val answer: Boolean,          // true = O, false = X
    val explanation: String
)

// 샘플 퀴즈 데이터
private val sampleQuestions = listOf(
    CsQuizQuestion(1, "스택(Stack)은 FIFO(First In First Out) 방식으로 동작한다.", false,
        "스택은 LIFO(Last In First Out) 방식입니다. FIFO는 큐(Queue)의 동작 방식입니다."),
    CsQuizQuestion(2, "HTTP는 상태를 유지하지 않는 무상태(Stateless) 프로토콜이다.", true,
        "HTTP는 기본적으로 Stateless 프로토콜로, 각 요청이 독립적으로 처리됩니다. 상태 유지를 위해 쿠키/세션을 사용합니다."),
    CsQuizQuestion(3, "이진 탐색(Binary Search)은 정렬되지 않은 배열에서도 사용 가능하다.", false,
        "이진 탐색은 반드시 정렬된 배열에서만 사용 가능합니다. 정렬되지 않은 배열에서는 선형 탐색을 사용합니다."),
    CsQuizQuestion(4, "프로세스(Process)는 스레드(Thread)보다 더 적은 메모리를 사용한다.", false,
        "스레드가 프로세스보다 더 적은 메모리를 사용합니다. 스레드는 프로세스의 메모리를 공유하기 때문입니다."),
    CsQuizQuestion(5, "TCP는 UDP보다 신뢰성이 높지만 속도가 느리다.", true,
        "TCP는 연결 지향적이고 오류 검출 및 재전송을 보장하므로 신뢰성이 높지만, 이로 인해 UDP보다 속도가 느립니다.")
)

// ─── 메인 화면 ────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsQuizScreen(
    onBack: () -> Unit = {}
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf<Boolean?>(null) } // null=미답, true=O선택, false=X선택
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val question = sampleQuestions[currentIndex]
    val total = sampleQuestions.size

    if (showResult) {
        CsQuizAnswerScreen(
            score = score,
            total = total,
            onRetry = {
                currentIndex = 0
                answered = null
                showResult = false
                score = 0
            },
            onBack = onBack
        )
        return
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CS 퀴즈",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 진행 표시
            ProgressSection(current = currentIndex + 1, total = total)

            // 문제 카드
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "question"
            ) { idx ->
                QuestionCard(question = sampleQuestions[idx])
            }

            Spacer(modifier = Modifier.weight(1f))

            // 정답/오답 피드백
            if (answered != null) {
                FeedbackCard(
                    isCorrect = answered == question.answer,
                    explanation = question.explanation
                )
            }

            // O / X 버튼
            OxButtons(
                enabled = answered == null,
                onSelect = { userAnswer ->
                    answered = userAnswer
                    if (userAnswer == question.answer) score++
                }
            )

            // 다음 버튼
            if (answered != null) {
                Button(
                    onClick = {
                        if (currentIndex < total - 1) {
                            currentIndex++
                            answered = null
                        } else {
                            showResult = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = BgPrimary
                    )
                ) {
                    Text(
                        text = if (currentIndex < total - 1) "다음 문제" else "결과 보기",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ─── 진행 표시 ────────────────────────────────────────────────
@Composable
private fun ProgressSection(current: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$current / $total",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(current.toFloat() / total * 100).toInt()}%",
                color = Primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { current.toFloat() / total },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(99.dp)),
            color = Primary,
            trackColor = BgElevated
        )
    }
}

// ─── 문제 카드 ────────────────────────────────────────────────
@Composable
private fun QuestionCard(question: CsQuizQuestion) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 180.dp),
        shape = RoundedCornerShape(20.dp),
        color = BgSurface
    ) {
        Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── O / X 버튼 ───────────────────────────────────────────────
@Composable
private fun OxButtons(
    enabled: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // O 버튼
        Button(
            onClick = { onSelect(true) },
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x2022C55E),
                contentColor = Success,
                disabledContainerColor = BgElevated,
                disabledContentColor = TextMuted
            )
        ) {
            Text("O", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }

        // X 버튼
        Button(
            onClick = { onSelect(false) },
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x20FB2C36),
                contentColor = Error,
                disabledContainerColor = BgElevated,
                disabledContentColor = TextMuted
            )
        ) {
            Text("X", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ─── 정답/오답 피드백 ─────────────────────────────────────────
@Composable
private fun FeedbackCard(isCorrect: Boolean, explanation: String) {
    val bgColor = if (isCorrect) Color(0x2022C55E) else Color(0x20FB2C36)
    val borderColor = if (isCorrect) Success else Error
    val label = if (isCorrect) "✓ 정답!" else "✗ 오답"
    val labelColor = if (isCorrect) Success else Error

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                color = labelColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = explanation,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
