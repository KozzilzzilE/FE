package com.example.fe.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
            // 부제목 유무와 상관없이 동일한 높이를 차지하도록 설정하여 타이틀의 y축 위치를 고정
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1F27)
                )
                // subtitle이 null이어도 공간을 차지하게 만들어 타이틀을 밀어올리는 정도를 동일하게 유지
                Text(
                    text = subtitle ?: " ", 
                    fontSize = 14.sp,
                    color = if (subtitle != null) Color(0xFF7A828A) else Color.Transparent,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        },
        navigationIcon = {
            if (showBackIcon || leftContent != null) {
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
