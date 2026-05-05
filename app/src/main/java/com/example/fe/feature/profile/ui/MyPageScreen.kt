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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

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
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadMyPageInfo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
        containerColor = BgPrimary,
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
                .background(BgPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            MyPageTopBar(onSettingsClick = onSettingsClick)

            Spacer(modifier = Modifier.height(20.dp))

            ProfileHeader(
                userName = if (uiState.userName.isBlank()) "사용자" else uiState.userName,
                bio = if (uiState.languageName.isBlank()) "언어 정보 없음" else uiState.languageName,
                level = uiState.level
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = BgDivider, thickness = 1.dp)

            Spacer(modifier = Modifier.height(20.dp))

            StatSection(
                streak = uiState.stat.streak,
                solved = uiState.stat.solved
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("계정 관리")
            Spacer(modifier = Modifier.height(10.dp))

            MenuItemCard(
                title = "개인 정보 수정",
                iconText = "👤",
                iconColor = Primary,
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("학습 관리")
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = BgSurface
            ) {
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    MenuItemCardInner(
                        title = "선호 언어 설정",
                        iconText = "🌐",
                        onClick = onLanguageClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = BgDivider,
                        thickness = 0.5.dp
                    )
                    MenuItemCardInner(
                        title = "내가 찜한 문제",
                        iconText = "☆",
                        onClick = onFavoriteClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = BgDivider,
                        thickness = 0.5.dp
                    )
                    MenuItemCardInner(
                        title = "제출 기록 보기",
                        iconText = "↺",
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
private fun MyPageTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "마이페이지",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "설정",
                tint = TextMuted,
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
        color = TextSecondary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun MenuItemCardInner(
    title: String,
    iconText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = iconText, color = Primary)
            Spacer(modifier = Modifier.size(12.dp))
            Text(text = title, color = TextPrimary)
        }
        Text(text = ">", color = TextMuted)
    }
}
