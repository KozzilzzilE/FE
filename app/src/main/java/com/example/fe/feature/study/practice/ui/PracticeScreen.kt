package com.example.fe.feature.study.practice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.feature.study.practice.PracticeUiState
import com.example.fe.feature.study.practice.PracticeViewModel
import com.example.fe.feature.study.practice.dto.QuizItemDto

private val PageBg = Color(0xFFF7F9FC)
private val BodyText = Color(0xFF667085)
private val ProgressBlue = Color(0xFF6E8FE6)

@Composable
fun PracticeScreen(
    topicId: Long,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onNextStepClick: () -> Unit = {},
    viewModel: PracticeViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(topicId) {
        viewModel.loadQuizzes(topicId)
    }

    PracticeContent(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onNextStepClick = onNextStepClick,
        onCheckAnswer = { quizIndex, answers ->
            viewModel.checkAnswers(
                quizIndex = quizIndex,
                userAnswers = answers
            )
        }
    )
}

@Composable
fun PracticeContent(
    state: PracticeUiState,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onNextStepClick: () -> Unit = {},
    onCheckAnswer: (Int, List<String>) -> Boolean = { _, _ -> false }
) {
    var currentIndex by remember(state.quizzes) { mutableIntStateOf(0) }

    when {
        state.isLoading -> {
            PracticeLoadingScreen()
        }

        state.error != null -> {
            PracticeMessageScreen(message = state.error ?: "오류가 발생했습니다.")
        }

        else -> {
            val totalCount = state.quizzes.size
            val quiz = state.quizzes.getOrNull(currentIndex)

            if (quiz == null) {
                PracticeMessageScreen(message = "문제가 없습니다.")
            } else {
                PracticeBlankContent(
                    quiz = quiz,
                    currentIndex = currentIndex,
                    totalCount = totalCount.coerceAtLeast(1),
                    onBack = onBack,
                    onHome = onHome,
                    onPrevClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    onNextClick = {
                        if (currentIndex < totalCount - 1) currentIndex++
                    },
                    onNextStepClick = onNextStepClick,
                    onCheckAnswer = { answers ->
                        onCheckAnswer(currentIndex, answers)
                    }
                )
            }
        }
    }
}

@Composable
private fun PracticeBlankContent(
    quiz: QuizItemDto,
    currentIndex: Int,
    totalCount: Int,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onNextStepClick: () -> Unit,
    onCheckAnswer: (List<String>) -> Boolean
) {
    val filledAnswers = remember(quiz.exerciseId) {
        mutableStateListOf<String?>().apply {
            repeat(quiz.blanks.size) { add(null) }
        }
    }

    var selectedBlankIndex by remember(quiz.exerciseId) {
        mutableIntStateOf(0)
    }

    var checkResult by remember(quiz.exerciseId) {
        mutableStateOf<Boolean?>(null)
    }

    val isAnswerComplete = filledAnswers.none { it.isNullOrBlank() }

    BlankScreen(
        quiz = quiz,
        currentIndex = currentIndex,
        totalCount = totalCount,
        choiceOptions = buildChoiceOptions(quiz),
        filledAnswers = filledAnswers,
        selectedBlankIndex = selectedBlankIndex,
        isAnswerComplete = isAnswerComplete,
        checkResult = checkResult,
        onBack = onBack,
        onHome = onHome,
        onPrevClick = onPrevClick,
        onNextClick = onNextClick,
        onNextStepClick = onNextStepClick,
        onBlankClick = { index ->
            selectedBlankIndex = index
        },
        onResetAnswers = {
            repeat(filledAnswers.size) { index ->
                filledAnswers[index] = null
            }
            selectedBlankIndex = 0
            checkResult = null
        },
        onAnswerSlotClick = { index ->
            if (selectedBlankIndex == index && filledAnswers[index] != null) {
                filledAnswers[index] = null
                checkResult = null
            } else {
                selectedBlankIndex = index
            }
        },
        onOptionClick = { option ->
            if (selectedBlankIndex in filledAnswers.indices) {
                filledAnswers[selectedBlankIndex] = option
                checkResult = null

                val nextEmpty = filledAnswers.indexOfFirst { it == null }
                if (nextEmpty != -1) {
                    selectedBlankIndex = nextEmpty
                }
            }
        },
        onCheckAnswerClick = {
            val answers = filledAnswers.map { it ?: "" }
            checkResult = onCheckAnswer(answers)
        }
    )
}

@Composable
private fun PracticeLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(color = ProgressBlue)
    }
}

@Composable
private fun PracticeMessageScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = message,
            color = BodyText
        )
    }
}