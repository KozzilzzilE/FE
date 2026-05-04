package com.example.fe.feature.concept.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.MoveButtonBar
import com.example.fe.common.TopBar
import com.example.fe.feature.concept.ConceptViewModel
import com.example.fe.feature.concept.component.*
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

@Composable
fun ConceptDetailScreen(
    topicId: Long,
    initialIndex: Int = 0,
    viewModel: ConceptViewModel,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onNextStepClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // 뷰 진입 시 해당 주제의 개념 데이터 로드 (시작 인덱스 전달)
    LaunchedEffect(topicId) {
        viewModel.loadConcepts(topicId = topicId, initialIndex = initialIndex)
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopBar(
                title = "개념 학습",
                subtitle = uiState.topicTitle,
                showBackIcon = true,
                showHomeIcon = true,
                onBackClick = onBack,
                onHomeClick = onHome
            )
        },
        bottomBar = {
            val totalPages = uiState.concepts.size
            if (totalPages > 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgPrimary)
                ) {
                    MoveButtonBar(
                        onNextStepClick = {
                            viewModel.completeCurrentNotionAndGoNext { onNextStepClick() }
                        },
                        onNextClick = { viewModel.nextChapter() },
                        onPrevClick = { viewModel.prevChapter() },
                        onNavigate = {},
                        isFirstPage = uiState.currentIndex == 0,
                        isLastPage = uiState.currentIndex == totalPages - 1
                    )
                    PageIndicatorDots(
                        totalPages = totalPages,
                        currentPage = uiState.currentIndex
                    )
                }
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
        } else if (uiState.concepts.isNotEmpty()) {
            val currentConcept = uiState.concepts[uiState.currentIndex]
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                val total = uiState.concepts.size
                val current = uiState.currentIndex + 1
                Text(
                    text = "$current / $total",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentConcept.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 24.dp)
                        .drawBehind {
                            val strokeWidth = 4.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Primary,
                                start = Offset(0f, y + 8.dp.toPx()),
                                end = Offset(size.width * 0.4f, y + 8.dp.toPx()),
                                strokeWidth = strokeWidth
                            )
                        }
                )

                ConceptImage(imageUrl = currentConcept.imgUrl)
                ConceptSummaryBox(text = currentConcept.point)

                if (currentConcept.exampleCode != null && !currentConcept.exampleCode.content.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    CodeExampleBox(code = currentConcept.exampleCode.content)
                }

                if (!currentConcept.detail.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    ConceptDetailBox(text = currentConcept.detail)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
