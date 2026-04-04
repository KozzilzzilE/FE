package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.profile.MyPageViewModel
import com.example.fe.feature.profile.component.LogoutButton
import com.example.fe.feature.profile.component.MenuItemCard
import com.example.fe.feature.profile.component.ProfileHeader
import com.example.fe.feature.profile.component.StatSection
import com.example.fe.feature.profile.model.ProfileStat
import com.example.fe.navigation.Routes

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
    onNavigate: (String) -> Unit,
    onEditProfileClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onSubmissionClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMyPageInfo()
    }

    MyPageScreenContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onEditProfileClick = onEditProfileClick,
        onLanguageClick = onLanguageClick,
        onFavoriteClick = onFavoriteClick,
        onSubmissionClick = onSubmissionClick,
        onLogoutClick = onLogoutClick,
        onSettingsClick = onSettingsClick
    )
}

@Composable
private fun MyPageScreenContent(
    uiState: MyPageUiState,
    onNavigate: (String) -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onSubmissionClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFF6F7FB),
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = Routes.MY,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F7FB))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            TopBar(onSettingsClick = onSettingsClick)

            Spacer(modifier = Modifier.height(20.dp))

            ProfileHeader(
                userName = if (uiState.userName.isBlank()) "사용자" else uiState.userName,
                bio = if (uiState.bio.isBlank()) "언어 정보 없음" else uiState.bio,
                level = uiState.level
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = Color(0xFFE6EAF0),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            StatSection(
                rank = uiState.stat.rank,
                streak = uiState.stat.streak,
                solved = uiState.stat.solved
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("계정 관리")
            Spacer(modifier = Modifier.height(10.dp))

            MenuItemCard(
                title = "개인 정보 수정",
                iconText = "👤",
                iconColor = Color(0xFF5B8DEF),
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("학습 관리")
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    MenuItemCardInner(
                        title = "선호 언어 설정",
                        iconText = "🌐",
                        iconColor = Color(0xFF4DB6AC),
                        onClick = onLanguageClick
                    )
                    MenuItemCardInner(
                        title = "내가 찜한 문제",
                        iconText = "☆",
                        iconColor = Color(0xFFE57373),
                        onClick = onFavoriteClick
                    )
                    MenuItemCardInner(
                        title = "제출 기록 보기",
                        iconText = "↺",
                        iconColor = Color(0xFF6C63FF),
                        onClick = onSubmissionClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            LogoutButton(onClick = onLogoutClick)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TopBar(
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "마이페이지",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E2430)
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "설정",
                tint = Color(0xFFAAB1BC),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = Color(0xFF7D8592),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun MenuItemCardInner(
    title: String,
    iconText: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = iconText,
                color = iconColor
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = title,
                color = Color(0xFF1E2430)
            )
        }

        Text(
            text = ">",
            color = Color(0xFFC2C7D0)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MyPageScreenPreview() {
    MyPageScreenContent(
        uiState = MyPageUiState(
            userName = "이성규",
            bio = "JAVA",
            level = 1,
            stat = ProfileStat(
                rank = "#142",
                streak = "14D",
                solved = "85"
            )
        )
    )
}