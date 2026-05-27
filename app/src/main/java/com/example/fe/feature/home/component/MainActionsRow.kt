package com.example.fe.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary

@Composable
fun MainActionsRow(
    modifier: Modifier = Modifier,
    onStudyClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onQuizClick: () -> Unit = {},
    onAiReviewClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickMenuCard(
                title = "학습하기",
                subtitle = "알고리즘\n학습",
                modifier = Modifier.weight(1f),
                onClick = onStudyClick,
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.img_notebook),
                        contentDescription = null,
                        modifier = Modifier
                            .offset(x = 38.dp, y = 40.dp)
                            .size(width = 73.dp, height = 63.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            QuickMenuCard(
                title = "즐겨찾기",
                subtitle = "저장한\n문제",
                modifier = Modifier.weight(1f),
                onClick = onFavoriteClick,
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.img_bookmark),
                        contentDescription = null,
                        modifier = Modifier
                            .offset(x = 36.dp, y = 29.dp)
                            .size(width = 82.dp, height = 83.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickMenuCard(
                title = "CS 퀴즈",
                subtitle = "오늘의\n퀴즈",
                modifier = Modifier.weight(1f),
                onClick = onQuizClick,
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.img_bulb),
                        contentDescription = null,
                        modifier = Modifier
                            .offset(x = 37.dp, y = 29.dp)
                            .size(width = 78.dp, height = 80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            QuickMenuCard(
                title = "AI 코드 리뷰",
                subtitle = "제출한\n코드 분석",
                modifier = Modifier.weight(1f),
                onClick = onAiReviewClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier
                            .offset(x = 42.dp, y = 32.dp)
                            .size(64.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun QuickMenuCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = BorderStroke(1.dp, BgElevated),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
            icon()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun MainActionsRowPreview() {
    MainActionsRow(
        modifier = Modifier.padding(16.dp)
    )
}
