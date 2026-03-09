package com.example.fe.feature.study.practice.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*
import com.example.fe.common.MoveButtonBar
import com.example.fe.feature.study.practice.dto.BlankDto
import com.example.fe.feature.study.practice.dto.QuizItemDto

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
    // API에서 blanks가 null일 수 있어서 안전하게 빈칸 개수 처리
    val blankCount = quiz.blanks?.size ?: 0

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
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAnswerComplete) Color(0xFF4A90E2) else CheckBtnBg,
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

@Composable
private fun PracticeHeaderBar(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = TopBarTitle
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopBarTitle
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TopBarSub
                )
            }

            IconButton(onClick = onHome) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "홈",
                    tint = Mint
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = DividerColor
        )
    }
}

@Composable
private fun PracticeProgressHeader(
    currentIndex: Int,
    totalCount: Int
) {
    val progress = ((currentIndex + 1).toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "문제 ${currentIndex + 1} / $totalCount",
                color = BodyText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "빈칸 채우기",
                color = ProgressBlue,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = ProgressBlue,
            trackColor = ProgressTrack
        )
    }
}

@Composable
private fun QuestionInfoCard(
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardBg,
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TitleText
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                color = BodyText,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun CodeBlankCard(
    codeTemplate: String,
    blankCount: Int,
    selectedBlankIndex: Int,
    filledAnswers: List<String?>,
    onBlankClick: (Int) -> Unit
) {
    // 코드 템플릿을 줄 단위로 분리
    val lines = remember(codeTemplate) {
        codeTemplate.lines()
    }

    var blankIndex = 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeBg, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        lines.forEach { line ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val parts = line.split("____")

                parts.forEachIndexed { index, part ->
                    Text(
                        text = part,
                        color = CodeText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )

                    // ____ 위치에 빈칸 슬롯 삽입
                    if (index < parts.lastIndex && blankIndex < blankCount) {
                        BlankSlot(
                            text = filledAnswers.getOrNull(blankIndex) ?: "",
                            isSelected = selectedBlankIndex == blankIndex,
                            onClick = { onBlankClick(blankIndex) }
                        )
                        blankIndex++
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun BlankSlot(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .height(24.dp)
            .width(52.dp)
            .background(
                color = if (isSelected) BlankBoxSelected else BlankBox,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF9CB8FF) else Color(0xFF5D6C99),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (text.isBlank()) "___" else text,
            color = if (text.isBlank()) Color.White else Color(0xFF7DA2F8),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectedAnswersCard(
    selectedAnswers: List<String?>,
    selectedIndex: Int,
    onReset: () -> Unit,
    onAnswerClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardBg,
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "선택한 답변",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitleText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onReset() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "reset",
                        tint = GrayText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "초기화",
                        color = GrayText,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                selectedAnswers.forEachIndexed { index, answer ->
                    SelectedAnswerItem(
                        modifier = Modifier.weight(1f),
                        number = index + 1,
                        answer = answer ?: "?",
                        isSelected = index == selectedIndex,
                        onClick = { onAnswerClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedAnswerItem(
    modifier: Modifier = Modifier,
    number: Int,
    answer: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val isFilled = answer != "?"

    val backgroundColor = if (isSelected) Color(0xFFEAF1FF) else Color.White

    val borderColor = when {
        isSelected -> Color(0xFF7DA2F8)
        isFilled -> Color(0xFF7DA2F8)
        else -> Color(0xFFD9E3F0)
    }

    val borderStyle = if (isFilled) {
        Stroke(width = 3f)
    } else {
        Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f))
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number.toString(),
            color = GrayText,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick() }
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        style = borderStyle,
                        cornerRadius = CornerRadius(28f, 28f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = answer,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = GrayText
            )
        }
    }
}

@Composable
private fun ChoiceGrid(
    options: List<String>,
    onOptionClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { option ->
                    ChoiceButton(
                        text = option,
                        modifier = Modifier.weight(1f),
                        onClick = { onOptionClick(option) }
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ChoiceButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CardBg,
            contentColor = TitleText
        ),
        border = BorderStroke(1.dp, ChoiceBorder),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// 보기 목록 생성
// blanks가 null이면 빈 리스트 반환
fun buildChoiceOptions(quiz: QuizItemDto): List<String> {
    val correctOptions = quiz.blanks?.map { it.content } ?: emptyList()
    val extras = listOf("get", "delete", "HashMap", "0")
    return (correctOptions + extras).distinct()
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