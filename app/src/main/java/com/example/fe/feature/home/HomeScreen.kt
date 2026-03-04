package com.example.fe.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.AppConstants
import com.example.fe.common.BottomNavigationBar
import com.example.fe.feature.home.component.HomeTopBar
import com.example.fe.feature.home.component.LanguageDropdown
import com.example.fe.common.bottomNavItems

@Composable
fun HomeScreen(
    // userName 파라미터는 뷰 모델을 통해 가져오므로 제거하거나 기본값 대체 가능 (여기서는 뷰 모델을 사용함)
    viewModel: HomeViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedLanguage by remember { mutableStateOf("") }

    // uiState가 Success이고 서버에서 정보를 가져오면 초기 선택 언어를 맞춥니다.
    LaunchedEffect(uiState) {
        if (uiState is HomeUiState.Success && selectedLanguage.isEmpty()) {
            val serverLang = (uiState as HomeUiState.Success).languageName
            // "JAVA" 등 서버 대문자 응답 처리를 위해 우리 상수 목록에서 매칭
            val matchedLang = AppConstants.SUPPORTED_LANGUAGES.find { 
                it.equals(serverLang, ignoreCase = true) 
            } ?: "Java" // 기본 폴백
            
            selectedLanguage = matchedLang
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                // UI 상태에 따라 적절한 유저명을 뽑아내서 전달 (로딩 중이거 에러면 빈스트링이나 '테스터' 표시)
                val displayUserName = when (uiState) {
                    is HomeUiState.Success -> (uiState as HomeUiState.Success).name
                    else -> "사용자"
                }

                HomeTopBar(userName = displayUserName)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 중앙 콘텐츠 박스 위에서 UI State별 분기 처리
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFFE8F1FA),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 상태별 화면 렌더링
                when (uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator(color = Color(0xFF4A90E2))
                    }
                    is HomeUiState.Error -> {
                        // 사용자 요청에 따라 UI에는 아무런 에러 메시지도 표시하지 않습니다.
                    }
                    is HomeUiState.Success -> {
                        val name = (uiState as HomeUiState.Success).name
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "환영합니다, ${name}님!", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            // 추천 문제 등 표시 로직 (TODO)
                        }
                    }
                }
            }
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
