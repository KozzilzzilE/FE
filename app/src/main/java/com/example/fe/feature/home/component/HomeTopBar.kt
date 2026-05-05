package com.example.fe.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.R
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary

/**
 * 피그마 HomeScreen AppBar
 * - 좌: POCKETCO 로고 (텍스트로 대체, 추후 이미지 에셋 교체 가능)
 * - 우: Amber 그라디언트 원형 프로필 버튼 (32x32dp)
 * - 하단: BgElevated 구분선
 * - 높이: 56dp, 좌우 패딩: 20dp
 */
@Composable
fun HomeTopBar(
    userName: String = "사용자",
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 좌측: POCKETCO 로고 (이미지 에셋 사용)
            Image(
                painter = painterResource(id = R.drawable.logo_home),
                contentDescription = "POCKETCO Logo",
                modifier = Modifier.size(167.dp, 54.dp),
                contentScale = ContentScale.Fit
            )

            // 우측: Amber 그라디언트 프로필 버튼 (Container 32x32)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Primary,
                                Primary.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "프로필",
                    tint = BgPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 하단 구분선
        HorizontalDivider(
            thickness = 1.dp,
            color = BgElevated
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar()
}
