package com.example.fe.feature.study.practice.ui.blank

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BlankBorder
import com.example.fe.ui.theme.BlankBorderSelected
import com.example.fe.ui.theme.BodyText
import com.example.fe.ui.theme.CardBg
import com.example.fe.ui.theme.CodeBg
import com.example.fe.ui.theme.CodeText
import com.example.fe.ui.theme.DividerColor
import com.example.fe.ui.theme.GrayText
import com.example.fe.ui.theme.Mint
import com.example.fe.ui.theme.ProgressBlue
import com.example.fe.ui.theme.ProgressTrack
import com.example.fe.ui.theme.TitleText
import com.example.fe.ui.theme.TopBarSub
import com.example.fe.ui.theme.TopBarTitle

@Composable
fun PracticeHeaderBar(
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
fun PracticeProgressHeader(
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
fun QuestionInfoCard(
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
fun CodeBlankCard(
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
fun SelectedAnswersCard(
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