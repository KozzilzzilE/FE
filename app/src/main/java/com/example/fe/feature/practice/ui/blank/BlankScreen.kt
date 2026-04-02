package com.example.fe.feature.practice.ui.blank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.MoveButtonBar
import com.example.fe.data.dto.BlankDto
import com.example.fe.data.dto.QuizItemDto
import com.example.fe.ui.theme.CardBg
import com.example.fe.ui.theme.CheckBtnBg
import com.example.fe.ui.theme.CheckBtnText
import com.example.fe.ui.theme.CorrectText
import com.example.fe.ui.theme.PageBg
import com.example.fe.ui.theme.PrimaryButtonBlue
import com.example.fe.ui.theme.TitleText
import com.example.fe.ui.theme.WrongText

@Composable
fun BlankScreen(
    quiz: QuizItemDto,
    currentIndex: Int,
    totalCount: Int,
    choiceOptions: List<String>,
    filledAnswers: List<String?>,
    selectedBlankIndex: Int,
    isAnswerComplete: Boolean,
    checkResult: Boolean?,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onNextStepClick: () -> Unit,
    onBlankClick: (Int) -> Unit,
    onResetAnswers: () -> Unit,
    onAnswerSlotClick: (Int) -> Unit,
    onOptionClick: (String) -> Unit,
    onCheckAnswerClick: () -> Unit
) {
    // blanks는 선택지, 빈칸 개수는 totalBlanks 사용
    val blankCount = quiz.totalBlanks

    Scaffold(
        containerColor = PageBg,
        topBar = {
            PracticeHeaderBar(
                title = "응용 학습",
                subtitle = "알고리즘",
                onBack = onBack,
                onHome = onHome
            )
        },
        bottomBar = {
            MoveButtonBar(
                onNextStepClick = onNextStepClick,
                onNextClick = onNextClick,
                onPrevClick = onPrevClick,
                onNavigate = {},
                isFirstPage = currentIndex == 0,
                isLastPage = currentIndex == totalCount - 1
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PageBg)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                PracticeProgressHeader(
                    currentIndex = currentIndex,
                    totalCount = totalCount
                )

                Spacer(modifier = Modifier.height(10.dp))

                QuestionInfoCard(
                    title = quiz.title,
                    description = quiz.description
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 코드 템플릿 + 빈칸 표시 영역
                CodeBlankCard(
                    codeTemplate = quiz.codeTemplate,
                    blankCount = blankCount,
                    selectedBlankIndex = selectedBlankIndex,
                    filledAnswers = filledAnswers,
                    onBlankClick = onBlankClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 현재 사용자가 선택한 답안 확인 영역
                SelectedAnswersCard(
                    selectedAnswers = filledAnswers,
                    selectedIndex = selectedBlankIndex,
                    onReset = onResetAnswers,
                    onAnswerClick = onAnswerSlotClick
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "답변 선택",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TitleText,
                    modifier = Modifier.padding(start = 2.dp, bottom = 10.dp)
                )

                // 선택지 버튼 목록
                ChoiceGrid(
                    options = choiceOptions,
                    onOptionClick = onOptionClick
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 정답 확인 버튼
                Button(
                    onClick = onCheckAnswerClick,
                    enabled = isAnswerComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAnswerComplete) PrimaryButtonBlue else CheckBtnBg,
                        contentColor = if (isAnswerComplete) Color.White else CheckBtnText
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "정답 확인",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                // 정답/오답 결과 표시
                if (checkResult != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (checkResult) "정답입니다!" else "오답입니다. 다시 시도해보세요.",
                        color = if (checkResult) CorrectText else WrongText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun BlankScreenPreview() {
    val previewQuiz = QuizItemDto(
        exerciseId = 1L,
        title = "해시맵으로 문자 개수 세기",
        description = "문자열에서 각 문자의 개수를 세는 코드의 빈칸을 채워보세요.",
        codeTemplate = """
function countChars(str) {
  const map = new ____();

  for (let char of str) {
    if (map.____(____)) {
      map.set(char, map.get(char) + 1);
    } else {
      map.____(char, ____);
    }
  }

  return map;
}
        """.trimIndent(),
        appliedCompleted = false,
        totalBlanks = 5,
        blanks = listOf(
            BlankDto(content = "Map", answer = 1),
            BlankDto(content = "has", answer = 2),
            BlankDto(content = "char", answer = 3),
            BlankDto(content = "set", answer = 4),
            BlankDto(content = "1", answer = 5)
        )
    )

    BlankScreen(
        quiz = previewQuiz,
        currentIndex = 0,
        totalCount = 2,
        choiceOptions = buildChoiceOptions(previewQuiz),
        filledAnswers = listOf(null, null, null, null, null),
        selectedBlankIndex = 0,
        isAnswerComplete = false,
        checkResult = null,
        onBack = {},
        onHome = {},
        onPrevClick = {},
        onNextClick = {},
        onNextStepClick = {},
        onBlankClick = {},
        onResetAnswers = {},
        onAnswerSlotClick = {},
        onOptionClick = {},
        onCheckAnswerClick = {}
    )
}