package com.example.fe.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    leftContent: @Composable (() -> Unit)? = null, // 화살표 옆 커스텀 아이콘 등을 위한 슬롯
    showBackIcon: Boolean = true, // 뒤로 가기 화살표 렌더링 여부 속성
    showHomeIcon: Boolean = true, // 홈 아이콘 렌더링 여부 속성
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1F27) // 진한 텍스트
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFF7A828A), // 살짝 연한 회색
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        },
        navigationIcon = {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                if (showBackIcon) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "뒤로 가기",
                            tint = Color(0xFF1A1F27) // 화살표 짙은 색
                        )
                    }
                }
                if (leftContent != null) {
                    leftContent()
                }
            }
        },
        actions = {
            if (showHomeIcon) {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "홈",
                        tint = Color(0xFF38B2AC) // 시안의 민트색/청록색 아이콘
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier
    )
}

@Preview
@Composable
fun TopBarPreview() {
    Column {
        TopBar(title = "알고리즘 학습", onBackClick = {}, onHomeClick = {})
        TopBar(title = "응용 학습", subtitle = "해시", onBackClick = {}, onHomeClick = {})
    }
}
