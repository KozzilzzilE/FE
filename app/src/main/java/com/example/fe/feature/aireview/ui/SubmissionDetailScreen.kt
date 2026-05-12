package com.example.fe.feature.aireview.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.fe.feature.concept.component.CodeExampleBox
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.aireview.SubmissionViewModel
import com.example.fe.feature.aireview.model.AiReviewResult
import com.example.fe.ui.theme.*

private enum class AiReviewState { IDLE, LOADING, DONE }

@Composable
fun SubmissionDetailScreen(
    viewModel: SubmissionViewModel,
    onBack: () -> Unit
) {
    val entry = viewModel.selectedEntry.collectAsState().value ?: return
    val aiReviewState by viewModel.aiReview.collectAsState()
    val isReviewLoading by viewModel.isReviewLoading.collectAsState()

    val problemTitle = entry.problemTitle
    val language = entry.language
    val date = entry.date
    val isCorrect = entry.isCorrect
    val sourceCode = entry.sourceCode

    val reviewState = when {
        isReviewLoading -> AiReviewState.LOADING
        aiReviewState == null -> AiReviewState.IDLE
        aiReviewState?.aiStatus == "PROCESSING" -> AiReviewState.LOADING
        aiReviewState?.aiStatus == "ACCEPTED" -> AiReviewState.DONE
        else -> AiReviewState.IDLE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ersHdr
            SdsHeader(title = problemTitle, onBack = onBack)

            // 스크롤 컨텐츠
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // infoCard
                InfoCard(language = language, date = date, isCorrect = isCorrect)

                // codeCard
                CodeCard(
                    dotColor = Error,
                    title = "내가 제출한 코드",
                    code = sourceCode
                )

                // AI 리뷰 카드들 (DONE 상태일 때만)
                AnimatedVisibility(
                    visible = reviewState == AiReviewState.DONE,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ReviewTextCard(
                            dotColor = Primary,
                            title = "아쉬운 점",
                            content = (aiReviewState?.aiReview ?: "").stripNumberedList()
                        )
                        ReviewTextCard(
                            dotColor = Cyan,
                            title = "개선할 점",
                            content = (aiReviewState?.aiImprovement ?: "").stripNumberedList()
                        )
                        aiReviewState?.aiCode?.let { code ->
                            if (code.isNotEmpty()) {
                                CodeCard(
                                    dotColor = Success,
                                    title = "개선된 코드 주요 로직",
                                    code = code
                                )
                            }
                        }
                    }
                }
            }
        }

        // 하단 AI 리뷰 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(BgPrimary)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            AiReviewButton(
                state = reviewState,
                onClick = {
                    viewModel.requestAiReview(entry.historyId)
                }
            )
        }
    }
}

@Composable
private fun SdsHeader(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로",
                tint = TextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun InfoCard(
    language: String,
    date: String,
    isCorrect: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = BgSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = language,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Text(
                text = date,
                fontSize = 12.sp,
                color = TextMuted
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isCorrect) Color(0x1A22C55E) else Color(0x1AFB2C36)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isCorrect) "정답" else "오답",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCorrect) Success else Error
                )
            }
        }
    }
}

@Composable
private fun CodeCard(
    dotColor: Color,
    title: String,
    code: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BgSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 헤더
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            // 코드 블록
            CodeExampleBox(code = code)
        }
    }
}

@Composable
private fun ReviewTextCard(
    dotColor: Color,
    title: String,
    content: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BgSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Text(
                text = content,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun AiReviewButton(
    state: AiReviewState,
    onClick: () -> Unit
) {
    Button(
        onClick = { if (state == AiReviewState.IDLE) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        when (state) {
            AiReviewState.IDLE -> {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = BgPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI 코드리뷰",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BgPrimary
                )
            }
            AiReviewState.LOADING -> {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                    color = BgPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "분석 중...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BgPrimary
                )
            }
            AiReviewState.DONE -> {
                Text(
                    text = "AI 리뷰 도착",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BgPrimary
                )
            }
        }
    }
}

private fun String.stripNumberedList(): String =
    lines().joinToString("\n") { line ->
        line.replace(Regex("^\\d+\\.\\s*"), "")
    }.trim()
