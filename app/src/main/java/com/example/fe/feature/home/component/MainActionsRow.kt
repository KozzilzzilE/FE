package com.example.fe.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.R
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary

@Composable
fun MainActionsRow(
    modifier: Modifier = Modifier,
    onStudyClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onQuizClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. 학습하기 카드
        QuickMenuCard(
            title = "학습하기",
            subtitle = "알고리즘\n학습",
            iconResId = R.drawable.img_notebook,
            imageOffsetX = (-5).dp,
            imageOffsetY = (-3).dp,
            imageWidth = 56.dp,
            imageHeight = 64.dp,
            modifier = Modifier.weight(1f),
            onClick = onStudyClick
        )

        // 2. 즐겨찾기 카드
        QuickMenuCard(
            title = "즐겨찾기",
            subtitle = "저장한\n문제",
            iconResId = R.drawable.img_bookmark,
            imageOffsetX = 12.dp,
            imageOffsetY = 3.dp,
            imageWidth = 82.dp,
            imageHeight = 83.dp,
            modifier = Modifier.weight(1f),
            onClick = onFavoriteClick
        )

        // 3. CS 퀴즈 카드
        QuickMenuCard(
            title = "CS 퀴즈",
            subtitle = "오늘의\n퀴즈",
            iconResId = R.drawable.img_bulb,
            imageOffsetX = 4.dp,
            imageOffsetY = 0.dp,
            imageWidth = 78.dp,
            imageHeight = 80.dp,
            modifier = Modifier.weight(1f),
            onClick = onQuizClick
        )
    }
}

@Composable
private fun QuickMenuCard(
    title: String,
    subtitle: String,
    iconResId: Int,
    imageOffsetX: androidx.compose.ui.unit.Dp,
    imageOffsetY: androidx.compose.ui.unit.Dp,
    imageWidth: androidx.compose.ui.unit.Dp,
    imageHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier.height(110.dp)) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = BgSurface),
            border = BorderStroke(1.dp, BgElevated),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }
        }
        
        // 하단 우측 3D 이미지 에셋 (Card 바깥에 배치하여 잘림 방지)
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = imageOffsetX, y = imageOffsetY)
                .size(width = imageWidth, height = imageHeight),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun MainActionsRowPreview() {
    MainActionsRow(
        modifier = Modifier.padding(16.dp)
    )
}
