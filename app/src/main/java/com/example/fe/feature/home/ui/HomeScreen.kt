package com.example.fe.feature.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.api.RetrofitClient
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

    val displayUserName = when (uiState) {
        is HomeUiState.Success -> (uiState as HomeUiState.Success).name
        else -> "사용자"
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            HomeTopBar(
                userName = displayUserName,
                onProfileClick = { onNavigate(Routes.MY) }
            )
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
                // 에러 처리
            }
            is HomeUiState.Success -> {
                val successState = uiState as HomeUiState.Success

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. 환영 섹션
                    WelcomeSection(
                        userName = "안녕하세요, ${displayUserName}님 👋"
                    )

                    // 2. "빠른 메뉴" 섹션 타이틀
                    Text(
                        text = "빠른 메뉴",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    // 3. 빠른 메뉴 카드 3장
                    MainActionsRow(
                        onStudyClick = { onNavigate(Routes.TOPIC) },
                        onFavoriteClick = { onNavigate(Routes.FAVORITE_PROBLEMS) },
                        onQuizClick = { onNavigate(Routes.CS_QUIZ) }
                    )

                    // 4. "연속 학습" 섹션 타이틀
                    Text(
                        text = "연속 학습",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    // 5. 학습 기록 잔디 (카드 안에)
                    ContributionGraph(
                        contributions = successState.contributionData,
                        streakDays = successState.streakDays
                    )

                    // 6. 명언 카드 (Amber 테두리)
                    QuoteCard()

                    Spacer(modifier = Modifier.height(8.dp))
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
