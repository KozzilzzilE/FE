package com.example.fe.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.api.RetrofitClient
import com.example.fe.feature.home.HomeUiState
import com.example.fe.feature.home.HomeViewModel
import com.example.fe.feature.home.HomeViewModelFactory
import com.example.fe.feature.home.component.*
import com.example.fe.feature.home.data.HomeRepository
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(HomeRepository(RetrofitClient.instance))
    ),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // UI 상태에 따른 유저 정보
    val displayUserName = when (uiState) {
        is HomeUiState.Success -> (uiState as HomeUiState.Success).name
        else -> "사용자"
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC), // 전체 배경색 고정
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "home",
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // 1. 환영 문구 섹션
            WelcomeSection(userName = "${displayUserName}님")

            // 2. 메인 액션 카드 섹션 (찜한 문제 + CS 퀴즈)
            MainActionsRow(
                onFavoriteClick = {
                    onNavigate(com.example.fe.navigation.Routes.FAVORITE_PROBLEMS)
                },
                onQuizClick = {
                    // 퀴즈 클릭 시 동작 (필요시 추가)
                }
            )

            // 3. 오늘의 명언 섹션
            QuoteCard()

            Spacer(modifier = Modifier.height(24.dp))

            // 4. 나의 학습 기록 섹션 타이틀
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "나의 학습 기록",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "전체보기",
                        fontSize = 14.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. 잔디 그래프 (Contribution Graph)
            // [DUMMY DATA]
            val dummyContributions = remember {
                val map = mutableMapOf<LocalDate, Int>()
                val today = LocalDate.now()
                // 1. 최근 14일은 무조건 연속 스트릭이 생기도록 데이터 생성
                for (i in 0..13) {
                    val date = today.minusDays(i.toLong())
                    map[date] = (5..15).random()
                }
                // 2. 그 이전 데이터는 랜덤하게 생성 (잔디용)
                for (i in 14..150) {
                    val date = today.minusDays(i.toLong())
                    if (i % 2 == 0) map[date] = (0..20).random()
                }
                map
            }

            ContributionGraph(
                contributions = dummyContributions,
                modifier = Modifier.padding(horizontal = 8.dp) // 내부 패딩 고려
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigate = {}
    )
}
