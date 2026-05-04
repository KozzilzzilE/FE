package com.example.fe.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import com.example.fe.api.RetrofitClient
import com.example.fe.common.AppConstants
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.home.HomeUiState
import com.example.fe.feature.home.HomeViewModel
import com.example.fe.feature.home.HomeViewModelFactory
import com.example.fe.feature.home.component.*
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
    val scrollState = rememberScrollState()

    // 언어 선택 상태 (임시)
    var selectedLanguage by remember { mutableStateOf("") }

    // 서버 언어 응답 -> 로컬 선택값 동기화
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
                // 에러 처리 (필요시 추가)
            }
            is HomeUiState.Success -> {
                // 기여 데이터 생성 로직
                val dummyContributions = remember {
                    val map = mutableMapOf<LocalDate, Int>()
                    val today = LocalDate.now()
                    for (i in 0..13) {
                        val date = today.minusDays(i.toLong())
                        map[date] = (5..15).random()
                    }
                    for (i in 14..150) {
                        val date = today.minusDays(i.toLong())
                        if (i % 2 == 0) map[date] = (0..20).random()
                    }
                    map
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // 1. 환영 섹션 (팀원 컴포넌트 + 성규님 디자인 테마 적용 가능하도록 수정됨)
                    WelcomeSection(userName = "${displayUserName}님")

                    // 2. 메인 액션 카드 (찜한 문제 + CS 퀴즈)
                    MainActionsRow(
                        onFavoriteClick = { onNavigate(Routes.BOOKMARK) },
                        onQuizClick = { onNavigate("cs_quiz") }
                    )

                    // 3. 명언 카드 (아까 정리한 40개 명언 데이터 버전)
                    QuoteCard()

                    // 4. 학습 기록 잔디
                    ContributionGraph(
                        contributions = dummyContributions,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}
 