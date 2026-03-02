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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.HomeTopBar
import com.example.fe.common.LanguageDropdown
import com.example.fe.common.bottomNavItems

@Composable
fun HomeScreen(
    userName: String = "홍길동",
    onNavigate: (String) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf("Java") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                HomeTopBar(userName = userName)
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
            // 빈 중앙 콘텐츠 영역 (향후 기능 추가를 위한 공간)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFFE8F1FA), // 제공된 이미지와 유사한 라이트 블루/스카이블루 배경
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        userName = "테스터",
        onNavigate = {}
    )
}
