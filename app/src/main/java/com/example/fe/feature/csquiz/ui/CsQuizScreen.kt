package com.example.fe.feature.csquiz.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.feature.csquiz.CsQuizViewModel
import com.example.fe.feature.csquiz.component.FeedbackCard
import com.example.fe.feature.csquiz.component.OxButtons
import com.example.fe.feature.csquiz.component.QuestionCard
import com.example.fe.feature.csquiz.component.QuizProgressSection
import com.example.fe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsQuizScreen(
    onBack: () -> Unit = {},
    viewModel: CsQuizViewModel = viewModel()
) {
    if (viewModel.showResult) {
        CsQuizAnswerScreen(
            score = viewModel.score,
            total = viewModel.total,
            onRetry = { viewModel.retry() },
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
            QuizProgressSection(
                current = viewModel.currentIndex + 1,
                total = viewModel.total
            )

            // 문제 카드
            AnimatedContent(
                targetState = viewModel.currentIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "question"
            ) { idx ->
                QuestionCard(question = viewModel.currentQuestion)
            }

            Spacer(modifier = Modifier.weight(1f))

            // 정답/오답 피드백
            if (viewModel.answered != null) {
                FeedbackCard(
                    isCorrect = viewModel.answered == viewModel.currentQuestion.answer,
                    explanation = viewModel.currentQuestion.explanation
                )
            }

            // O / X 버튼
            OxButtons(
                enabled = viewModel.answered == null,
                onSelect = { viewModel.selectAnswer(it) }
            )

            // 다음 버튼
            if (viewModel.answered != null) {
                Button(
                    onClick = { viewModel.nextQuestion() },
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
                        text = if (viewModel.currentIndex < viewModel.total - 1) "다음 문제" else "결과 보기",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
