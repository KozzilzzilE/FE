package com.example.fe.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    leftContent: @Composable (() -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    showBackIcon: Boolean = true,
    showHomeIcon: Boolean = false,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        },
        navigationIcon = {
            if (showBackIcon || leftContent != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showBackIcon) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = "뒤로 가기",
                                tint = TextPrimary
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
            if (rightContent != null) {
                rightContent()
            }
            if (showHomeIcon) {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "홈",
                        tint = Primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BgSurface
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
