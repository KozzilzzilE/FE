package com.example.fe.feature.concept.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.fe.data.dto.NotionDto
import com.example.fe.feature.concept.ConceptViewModel
import com.example.fe.feature.concept.component.PageIndicatorDots
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Cyan
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
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

    LaunchedEffect(topicId) {
        viewModel.loadConcepts(topicId = topicId, initialIndex = initialIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        // csHdr - 중앙 정렬 헤더 (safe area 확보)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgPrimary)
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
                    contentDescription = "뒤로",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "개념 학습",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                if (uiState.topicTitle.isNotBlank()) {
                    Text(
                        text = uiState.topicTitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
            IconButton(onClick = onHome, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "홈",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        } // statusBarsPadding Column 닫기

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
            return@Column
        }

        if (uiState.concepts.isEmpty()) return@Column

        val total = uiState.concepts.size
        val currentIndex = uiState.currentIndex
        val concept = uiState.concepts[currentIndex]
        val images = listOfNotNull(concept.imgUrl)

        // csMain - 스크롤 가능한 콘텐츠 영역
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // progRow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.topicTitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Text(
                    text = "${currentIndex + 1} / $total",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // csTitleRow
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = concept.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Primary)
                )
            }

            // csCard
            ConceptContentCard(concept = concept)

            // imgSection
            if (images.isNotEmpty()) {
                ConceptImageSection(images = images)
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        // csNav - 하단 네비게이션
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgPrimary)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 슬라이드 페이지 인디케이터 dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until total) {
                    val isActive = i == currentIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isActive) 8.dp else 6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isActive) Primary else BgElevated)
                    )
                }
            }

            // 이전 / 다음 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.prevChapter() },
                    enabled = currentIndex > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary,
                        disabledContentColor = TextSecondary.copy(alpha = 0.3f)
                    )
                ) {
                    Text(text = "< 이전", fontSize = 14.sp)
                }
                Button(
                    onClick = {
                        if (currentIndex == total - 1) {
                            viewModel.completeCurrentNotionAndGoNext { onNextStepClick() }
                        } else {
                            viewModel.nextChapter()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = BgPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = if (currentIndex == total - 1) "다음 단계 >" else "다음 >",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ConceptContentCard(concept: NotionDto) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 요약 섹션
            Text(
                text = "💡  ${concept.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = concept.point,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            // 예제 코드 섹션
            if (concept.exampleCode != null && concept.exampleCode.content.isNotBlank()) {
                HorizontalDivider(color = BgElevated, thickness = 1.dp)
                Text(
                    text = "💻  예제 코드",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(BgPrimary)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = concept.exampleCode.content,
                        fontSize = 12.sp,
                        color = Cyan,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 18.sp
                    )
                }
            }

            // 상세 설명 섹션
            if (concept.detail.isNotBlank()) {
                HorizontalDivider(color = BgElevated, thickness = 1.dp)
                Text(
                    text = "📝  상세 설명",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Markdown(
                    content = concept.detail,
                    colors = markdownColor(
                        text = TextSecondary,
                        codeText = Cyan,
                        codeBackground = BgElevated,
                        dividerColor = BgElevated
                    ),
                    typography = markdownTypography(
                        h3 = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        paragraph = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        ),
                        code = MaterialTheme.typography.bodySmall.copy(
                            color = Cyan,
                            fontSize = 12.sp
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun ConceptImageSection(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[page])
                    .crossfade(true)
                    .build(),
                contentDescription = "이미지 ${page + 1}",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(BgSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary, modifier = Modifier.size(32.dp))
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(BgSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = null,
                                tint = BgElevated,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    else -> SubcomposeAsyncImageContent()
                }
            }
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                images.forEachIndexed { i, _ ->
                    val isActive = i == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .then(
                                if (isActive) Modifier.width(18.dp).height(5.dp)
                                else Modifier.size(5.dp)
                            )
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isActive) Primary else BgElevated)
                    )
                }
            }
        }
    }
}
