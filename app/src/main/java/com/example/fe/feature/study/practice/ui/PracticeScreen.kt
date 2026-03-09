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
import com.example.fe.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.feature.study.practice.PracticeUiState
import com.example.fe.feature.study.practice.PracticeViewModel
import com.example.fe.feature.study.practice.PracticeViewModelFactory
import com.example.fe.feature.study.practice.api.PracticeApi
import com.example.fe.feature.study.practice.data.PracticeRepository
import com.example.fe.feature.study.practice.dto.QuizItemDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PracticeScreen(
    topicId: Long,
    onBack: () -> Unit = {},
    onHome: () -> Unit = {},
    onNextStepClick: () -> Unit = {}
) {

    // Retrofit 생성 (임시)
    // 나중에 RetrofitInstance로 분리 ?
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // 에뮬레이터에서 로컬 서버 접근 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API 인터페이스 생성
    val practiceApi = remember(retrofit) {
        retrofit.create(PracticeApi::class.java)
    }

    // Repository 생성 (API 호출 담당)
    val repository = remember(practiceApi) {
        PracticeRepository(practiceApi)
    }

    // ViewModelFactory 생성
    // ViewModel에 Repository를 전달하기 위해 필요
    val factory = remember(repository) {
        PracticeViewModelFactory(repository)
    }

    // ViewModel 생성
    val viewModel: PracticeViewModel = viewModel(factory = factory)

    // ViewModel 상태 구독
    val state = viewModel.uiState.collectAsState().value

    // 화면 진입 시 문제 로드
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

    // 현재 보고 있는 문제 index
    var currentIndex by remember(state.quizzes) { mutableIntStateOf(0) }

    when {

        // 로딩 상태
        state.isLoading -> {
            PracticeLoadingScreen()
        }

        // 오류 발생
        state.error != null -> {
            PracticeMessageScreen(message = state.error ?: "오류가 발생했습니다.")
        }

        // 정상 데이터
        else -> {

            val totalCount = state.quizzes.size
            val quiz = state.quizzes.getOrNull(currentIndex)

            // 문제 없을 경우
            if (quiz == null) {
                PracticeMessageScreen(message = "문제가 없습니다.")
            } else {

                // 빈칸 채우기 문제 화면
                PracticeBlankContent(
                    quiz = quiz,
                    currentIndex = currentIndex,
                    totalCount = totalCount.coerceAtLeast(1),

                    onBack = onBack,
                    onHome = onHome,

                    // 이전 문제
                    onPrevClick = {
                        if (currentIndex > 0) currentIndex--
                    },

                    // 다음 문제
                    onNextClick = {
                        if (currentIndex < totalCount - 1) currentIndex++
                    },

                    onNextStepClick = onNextStepClick,

                    // 정답 체크
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

    // blanks는 API에서 null 가능하므로 안전 처리
    val blanks = quiz.blanks ?: emptyList()

    // 각 빈칸에 입력된 사용자 답
    val filledAnswers = remember(quiz.exerciseId) {
        mutableStateListOf<String?>().apply {
            repeat(blanks.size) { add(null) }
        }
    }

    // 현재 선택된 빈칸 index
    var selectedBlankIndex by remember(quiz.exerciseId) {
        mutableIntStateOf(0)
    }

    // 정답 결과 (true / false / null)
    var checkResult by remember(quiz.exerciseId) {
        mutableStateOf<Boolean?>(null)
    }

    // 모든 빈칸이 채워졌는지 여부
    val isAnswerComplete =
        blanks.isNotEmpty() && filledAnswers.none { it.isNullOrBlank() }

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

        // 빈칸 선택
        onBlankClick = { index ->
            selectedBlankIndex = index
        },

        // 답 초기화
        onResetAnswers = {
            repeat(filledAnswers.size) { index ->
                filledAnswers[index] = null
            }
            selectedBlankIndex = 0
            checkResult = null
        },

        // 답칸 클릭
        onAnswerSlotClick = { index ->
            if (selectedBlankIndex == index && filledAnswers[index] != null) {
                filledAnswers[index] = null
                checkResult = null
            } else {
                selectedBlankIndex = index
            }
        },

        // 선택지 클릭
        onOptionClick = { option ->
            if (selectedBlankIndex in filledAnswers.indices) {

                filledAnswers[selectedBlankIndex] = option
                checkResult = null

                // 다음 빈칸 자동 이동
                val nextEmpty = filledAnswers.indexOfFirst { it == null }
                if (nextEmpty != -1) {
                    selectedBlankIndex = nextEmpty
                }
            }
        },

        // 정답 확인 버튼
        onCheckAnswerClick = {
            val answers = filledAnswers.map { it ?: "" }
            checkResult = onCheckAnswer(answers)
        }
    )
}

@Composable
private fun PracticeLoadingScreen() {

    // 로딩 화면
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            color = ProgressBlue
        )
    }
}

@Composable
private fun PracticeMessageScreen(message: String) {

    // 오류 / 데이터 없음 메시지 화면
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