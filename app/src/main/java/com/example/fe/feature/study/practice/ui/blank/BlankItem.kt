package com.example.fe.feature.study.practice.ui.blank

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.study.practice.dto.QuizItemDto
import com.example.fe.ui.theme.BlankBorder
import com.example.fe.ui.theme.BlankBorderSelected
import com.example.fe.ui.theme.BlankBox
import com.example.fe.ui.theme.BlankBoxSelected
import com.example.fe.ui.theme.BlankTextFilled
import com.example.fe.ui.theme.CardBg
import com.example.fe.ui.theme.ChoiceBorder
import com.example.fe.ui.theme.EmptyBorder
import com.example.fe.ui.theme.GrayText
import com.example.fe.ui.theme.SelectedAnswerBg
import com.example.fe.ui.theme.TitleText

@Composable
fun BlankSlot(
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
                color = if (isSelected) BlankBorderSelected else BlankBorder,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (text.isBlank()) "___" else text,
            color = if (text.isBlank()) Color.White else BlankTextFilled,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SelectedAnswerItem(
    modifier: Modifier = Modifier,
    number: Int,
    answer: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val isFilled = answer != "?"

    val backgroundColor = if (isSelected) SelectedAnswerBg else Color.White

    val borderColor = when {
        isSelected -> BlankTextFilled
        isFilled -> BlankTextFilled
        else -> EmptyBorder
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
fun ChoiceGrid(
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
fun ChoiceButton(
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