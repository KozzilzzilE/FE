package com.example.fe.feature.practice.ui.blank

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fe.common.highlight
import com.example.fe.common.parseCodeFence
import com.example.fe.ui.theme.BlankBorder
import com.example.fe.ui.theme.BlankBorderSelected
import com.example.fe.ui.theme.BodyText
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.CardBg
import com.example.fe.ui.theme.CodeBg
import com.example.fe.ui.theme.CodeText
import com.example.fe.ui.theme.DividerColor
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.GrayText
import com.example.fe.ui.theme.Mint
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.ProgressBlue
import com.example.fe.ui.theme.ProgressTrack
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary
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
            .background(com.example.fe.ui.theme.BgPrimary)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = TopBarTitle,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TopBarTitle
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TopBarSub
                    )
                }
            }

            IconButton(onClick = onHome, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "홈",
                    tint = TopBarTitle,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PracticeProgressHeader(
    currentIndex: Int,
    totalCount: Int
) {
    val progress = ((currentIndex + 1).toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)

    Column {
        Text(
            text = "문제 ${currentIndex + 1} / $totalCount",
            color = BodyText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = Primary,
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

private data class LinePart(val text: String, val isBlank: Boolean, val originalLength: Int)

private fun parseLine(line: String): List<LinePart> {
    val result = mutableListOf<LinePart>()
    val regex = Regex("_{2,}")
    var lastEnd = 0
    for (match in regex.findAll(line)) {
        if (match.range.first > lastEnd) {
            val t = line.substring(lastEnd, match.range.first)
            result.add(LinePart(t, false, t.length))
        }
        result.add(LinePart("", true, match.value.length))
        lastEnd = match.range.last + 1
    }
    if (lastEnd < line.length) {
        val t = line.substring(lastEnd)
        result.add(LinePart(t, false, t.length))
    }
    return result
}

@Composable
fun CodeBlankCard(
    codeTemplate: String,
    blankCount: Int,
    selectedBlankIndex: Int,
    filledAnswers: List<String?>,
    onBlankClick: (Int) -> Unit
) {
    val (language, cleanTemplate) = remember(codeTemplate) { parseCodeFence(codeTemplate) }
    val fullHighlighted = remember(cleanTemplate, language) { highlight(cleanTemplate, language) }
    val lines = remember(cleanTemplate) { cleanTemplate.lines() }

    var globalOffset = 0
    var blankIndex = 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeBg, RoundedCornerShape(16.dp))
            .horizontalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            lines.forEach { line ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val parts = parseLine(line)
                    var partOffset = globalOffset

                    parts.forEach { part ->
                        if (part.isBlank) {
                            if (blankIndex < blankCount) {
                                BlankSlot(
                                    text = filledAnswers.getOrNull(blankIndex) ?: "",
                                    isSelected = selectedBlankIndex == blankIndex,
                                    onClick = { onBlankClick(blankIndex) }
                                )
                                blankIndex++
                            }
                        } else if (part.text.isNotEmpty()) {
                            val segStart = partOffset.coerceIn(0, fullHighlighted.length)
                            val segEnd = (partOffset + part.originalLength).coerceIn(0, fullHighlighted.length)
                            val segment = if (segStart < segEnd) {
                                fullHighlighted.subSequence(segStart, segEnd)
                            } else {
                                androidx.compose.ui.text.AnnotatedString(part.text)
                            }
                            Text(
                                text = segment,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                softWrap = false
                            )
                        }
                        partOffset += part.originalLength
                    }
                }

                globalOffset += line.length + 1
                Spacer(modifier = Modifier.height(2.dp))
            }
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

@Composable
fun ResultModal(
    isCorrect: Boolean,
    isLastProblem: Boolean,
    onClose: () -> Unit,
    onAction: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BgSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "닫기",
                                tint = GrayText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = if (isCorrect) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                        contentDescription = null,
                        tint = if (isCorrect) Success else Error,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isCorrect) "정답입니다!" else "오답입니다.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isCorrect) "잘 했어요! 계속 진행해 보세요." else "다시 한 번 생각해보고 풀어보세요!",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgElevated,
                            contentColor = TextPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = if (isCorrect) {
                                if (isLastProblem) "완료하기" else "다음으로 >"
                            } else "다시 풀기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}