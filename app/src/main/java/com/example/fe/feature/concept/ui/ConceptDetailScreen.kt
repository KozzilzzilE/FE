package com.example.fe.feature.concept.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.MoveButtonBar
import com.example.fe.common.TopBar
import com.example.fe.feature.concept.ConceptViewModel
import com.example.fe.feature.concept.component.CodeExampleBox
import com.example.fe.feature.concept.component.ConceptDetailBox
import com.example.fe.feature.concept.component.ConceptImage
import com.example.fe.feature.concept.component.ConceptSummaryBox
import com.example.fe.feature.concept.component.PageIndicatorDots

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

//    // 뷰 진입 시 해당 주제의 개념 데이터 로드 (시작 인덱스 전달)
//    LaunchedEffect(topicId) {
//        viewModel.loadConcepts(topicId = topicId, initialIndex = initialIndex)
//    }

    Scaffold(
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
                        .background(Color.White)
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
                    
                    // 최하단 페이지 인디케이터 (Dots)
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
                // 현재 몇 페이지인지 텍스트로 표기
                Spacer(modifier = Modifier.height(20.dp))
                val total = uiState.concepts.size
                val current = uiState.currentIndex + 1
                Text(
                    text = "$current / $total",
                    fontSize = 14.sp,
                    color = Color(0xFF7A828A)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 본문 챕터 제목 (하단 언더바 포인트 효과)
                Text(
                    text = currentConcept.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1F27),
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 24.dp)
                        .drawBehind {
                            val strokeWidth = 4.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color(0xFF4A90E2),
                                start = Offset(0f, y + 8.dp.toPx()),
                                end = Offset(size.width * 0.4f, y + 8.dp.toPx()),
                                strokeWidth = strokeWidth
                            )
                        }
                )

                // 개요 이미지
                ConceptImage(imageUrl = currentConcept.imgUrl)

                // 개요 설명 박스
                ConceptSummaryBox(text = currentConcept.point)

                // 예제 코드
                if (currentConcept.exampleCode != null && !currentConcept.exampleCode.content.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    CodeExampleBox(code = currentConcept.exampleCode.content)
                }

                // 상세/추가 설명 파트
                if (!currentConcept.detail.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    ConceptDetailBox(text = currentConcept.detail)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

