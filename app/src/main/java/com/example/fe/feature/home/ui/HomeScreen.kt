package com.example.fe.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import com.example.fe.api.RetrofitClient
import com.example.fe.common.AppConstants
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.home.HomeUiState
import com.example.fe.feature.home.HomeViewModel
import com.example.fe.feature.home.HomeViewModelFactory
import com.example.fe.feature.home.component.ContributionGraph
import com.example.fe.feature.home.component.HomeTopBar
import com.example.fe.feature.home.component.LanguageDropdown
import com.example.fe.feature.home.data.HomeRepository
import com.example.fe.navigation.Routes
import com.example.fe.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(HomeRepository(RetrofitClient.instance))
    ),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedLanguage by remember { mutableStateOf("") }

    // 서버 언어 응답 → 로컬 선택값 동기화 (기존 로직 유지)
    LaunchedEffect(uiState) {
        if (uiState is HomeUiState.Success && selectedLanguage.isEmpty()) {
            val serverLang = (uiState as HomeUiState.Success).languageName
            val matchedLang = AppConstants.SUPPORTED_LANGUAGES.find {
                it.equals(serverLang, ignoreCase = true)
            } ?: "Java"
            selectedLanguage = matchedLang
        }
    }

    val displayUserName = when (uiState) {
        is HomeUiState.Success -> (uiState as HomeUiState.Success).name
        else -> "사용자"
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            Column {
                HomeTopBar(
                    userName = displayUserName,
                    onProfileClick = { onNavigate(Routes.MY) }
                )
                LanguageDropdown(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { selectedLanguage = it }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "home",
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            is HomeUiState.Error -> {
                // 기존 동일: 에러 시 UI에 메시지 표시 안 함
            }
            is HomeUiState.Success -> {
                // 더미 기여 데이터 (실제 API 연동 전까지 유지)
                val dummyContributions = remember {
                    val map = mutableMapOf<LocalDate, Int>()
                    val today = LocalDate.now()
                    for (i in 0..150) {
                        val date = today.minusDays(i.toLong())
                        if (i % 3 == 0) map[date] = (0..20).random()
                    }
                    map
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 인사말 섹션
                    GreetingSection(userName = displayUserName)

                    // 빠른 메뉴 3칸
                    QuickMenuRow(onNavigate = onNavigate)

                    // 스트릭 그래프
                    ContributionGraph(contributions = dummyContributions)

                    // 명언 카드
                    QuoteCard()

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

// ── 인사말 섹션 ──────────────────────────────────────────────

@Composable
private fun GreetingSection(userName: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "안녕하세요, ${userName}님 👋",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "오늘도 코딩 실력을 키워볼까요?",
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

// ── 빠른 메뉴 ────────────────────────────────────────────────

@Composable
private fun QuickMenuRow(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickMenuCard(
            label = "학습하기",
            icon = Icons.Default.Menu,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(Routes.TOPIC) }
        )
        QuickMenuCard(
            label = "즐겨찾기",
            icon = Icons.Default.Favorite,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(Routes.MY) }
        )
        QuickMenuCard(
            label = "CS 퀴즈",
            icon = Icons.Default.Star,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate("cs_quiz") }
        )
    }
}

@Composable
private fun QuickMenuCard(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(PrimaryDim15, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Primary,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

// ── 명언 카드 ─────────────────────────────────────────────────

private val quotes = listOf(
    "\"코드는 시처럼 써야 한다.\" — Knuth",
    "\"단순함이 최고의 정교함이다.\" — Leonardo da Vinci",
    "\"먼저 작동하게 만들고, 그 다음 빠르게 만들어라.\" — Kent Beck",
    "\"버그를 수정하는 가장 좋은 방법은 처음부터 만들지 않는 것이다.\""
)

@Composable
private fun QuoteCard() {
    val quote = remember { quotes.random() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "오늘의 명언",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Primary
        )
        Text(
            text = quote,
            fontSize = 14.sp,
            color = TextSecondary,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}
