package com.example.fe.feature.concept.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.MoveButtonBar
import com.example.fe.common.TopBar
import com.example.fe.feature.concept.ConceptViewModel

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until totalPages) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (i == uiState.currentIndex) Color(0xFF4A90E2) else Color(0xFFE0E0E0)) // 현재 페이지만 파란색
                            )
                        }
                    }
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
                    // 공통 TopBar의 그림자 처리를 보호하기 위해 스크롤 내부 영역에만 패딩 부여
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                // 현재 몇 페이지인지 텍스트로 표기 (가로 진행률 바 대신)
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
                                end = Offset(size.width * 0.4f, y + 8.dp.toPx()), // 글자 너비의 일부만 밑줄
                                strokeWidth = strokeWidth
                            )
                        }
                )

                // 개요 설명 박스 (흰색/연회색 둥근 배경)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        // 테두리가 살짝 있는 연한 박스 효과 부여를 위한 커스텀 테두리 로직 대신 백그라운드와 그림자 약간 혹은 그냥 #F8FAFC
                        .background(Color(0xFFF8FAFC))
                        .padding(20.dp)
                ) {
                    Text(
                        text = currentConcept.point,
                        fontSize = 15.sp,
                        color = Color(0xFF333333),
                        lineHeight = 24.sp
                    )
                }

                // 예제 코드 (네이비 배경)
                if (currentConcept.exampleCode != null && !currentConcept.exampleCode.content.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "예제 코드",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A828A),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E232E))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = currentConcept.exampleCode.content,
                            fontSize = 14.sp,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 22.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }

                // 상세/추가 설명 파트 (연파스텔 배경)
                if (!currentConcept.detail.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "상세 설명",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A828A),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0F8FF)) // 부드러운 스카이블루/파스텔톤 연상
                            .padding(20.dp)
                    ) {
                        Text(
                            text = currentConcept.detail,
                            fontSize = 15.sp,
                            color = Color(0xFF333333),
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
