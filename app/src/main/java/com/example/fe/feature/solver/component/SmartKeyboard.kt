package com.example.fe.feature.solver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary

@Composable
fun SmartKeyboardPanel(
    onInsert: (String) -> Unit,
    onRun: () -> Unit = {},
    onSubmit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 기호 키 — HorizontalPager로 손가락 따라 실시간 전환
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val keys = when (page) {
                0 -> listOf(";", ",", ".", "\"", "'", "!", "=", "+", "-", "*")
                1 -> listOf("{", "}", "[", "]", "(", ")", "<", ">", ":", "?")
                else -> listOf("/", "%", "&", "|", "^", "~", "_", "@", "#", "$")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                keys.forEach { k ->
                    SmallKey(text = k, modifier = Modifier.weight(1f)) { onInsert(k) }
                }
            }
        }

        // 페이지 인디케이터 dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { i ->
                val isActive = i == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .then(
                            if (isActive) Modifier.width(14.dp).height(4.dp)
                            else Modifier.size(4.dp)
                        )
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isActive) Primary else BgElevated)
                )
            }
        }

        // 액션 키 — 모든 페이지 공통 (Tab / 줄바꿈 / 주석)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ActionKey(text = "Tab", modifier = Modifier.weight(1f)) { onInsert("\t") }
            ActionKey(text = "줄바꿈", modifier = Modifier.weight(1f)) { onInsert("\n") }
            ActionKey(text = "주석", modifier = Modifier.weight(1f)) { onInsert("// ") }
        }

        // 실행하기 + 제출하기
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                onClick = onRun,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                color = Primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "▶  실행하기",
                        color = BgPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Surface(
                onClick = onSubmit,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                color = Primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "▶  제출하기",
                        color = BgPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallKey(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = BgElevated,
        modifier = modifier.height(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ActionKey(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(BgElevated)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
