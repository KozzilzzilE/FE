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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.api.RetrofitClient
import com.example.fe.feature.csquiz.CsQuizUiState
import com.example.fe.feature.csquiz.CsQuizViewModel
import com.example.fe.feature.csquiz.CsQuizViewModelFactory
import com.example.fe.feature.csquiz.component.FeedbackCard
import com.example.fe.feature.csquiz.component.OxButtons
import com.example.fe.feature.csquiz.component.QuestionCard
import com.example.fe.feature.csquiz.component.QuizProgressSection
import com.example.fe.feature.csquiz.data.CsQuizRepository
import com.example.fe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsQuizScreen(
    onBack: () -> Unit = {},
    viewModel: CsQuizViewModel = viewModel(
        factory = CsQuizViewModelFactory(CsQuizRepository(RetrofitClient.instance))
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is CsQuizUiState.Loading -> {
            Scaffold(
                containerColor = BgPrimary,
                topBar = { CsQuizTopBar(onBack = onBack) }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
        }

        is CsQuizUiState.Error -> {
            Scaffold(
                containerColor = BgPrimary,
                topBar = { CsQuizTopBar(onBack = onBack) }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                        Button(
                            onClick = { viewModel.loadQuestions() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("다시 시도", color = BgPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        is CsQuizUiState.Quiz -> {
            if (state.showResult) {
                CsQuizAnswerScreen(
                    score = state.score,
                    total = state.total,
                    onRetry = { viewModel.retry() },
                    onNextQuiz = { viewModel.nextQuiz() },
                    onBack = onBack
                )
                return
            }

            Scaffold(
                containerColor = BgPrimary,
                topBar = { CsQuizTopBar(onBack = onBack) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    QuizProgressSection(
                        current = state.currentIndex + 1,
                        total = state.total
                    )

                    AnimatedContent(
                        targetState = state.currentIndex,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "question"
                    ) {
                        QuestionCard(question = state.currentQuestion)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (state.answered != null) {
                        FeedbackCard(
                            isCorrect = state.answered == state.currentQuestion.answer,
                            explanation = state.currentQuestion.explanation
                        )
                    }

                    OxButtons(
                        enabled = state.answered == null,
                        onSelect = { viewModel.selectAnswer(it) }
                    )

                    if (state.answered != null) {
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
                                text = if (state.currentIndex < state.total - 1) "다음 문제" else "결과 보기",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CsQuizTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text("CS 퀴즈", fontWeight = FontWeight.Bold, color = TextPrimary)
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
