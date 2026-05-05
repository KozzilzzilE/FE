package com.example.fe.feature.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*

private val iconTintAmber  = Primary               // 0xFFF59E0B
private val iconTintSalmon = Color(0xFFFFBFB2)
private val iconTintYellow = Color(0xFFF2F3C7)

@Composable
fun TopicCard(title: String, index: Int = 0, onClick: () -> Unit) {
    val icon   = getTopicIcon(title)
    val iconBg = getTopicIconBg(index)
    val iconTint = getTopicIconTint(index)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BgSurface)
            .border(1.dp, BgElevated, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

fun getTopicIcon(name: String): ImageVector {
    val key = name.lowercase()
    return when {
        key.contains("hash") || key.contains("해시") -> Icons.Default.Tag
        key.contains("stack") || key.contains("queue") || key.contains("스택") || key.contains("큐") -> Icons.Default.Layers
        key.contains("graph") || key.contains("그래프") -> Icons.Default.Hub
        key.contains("dp") || key.contains("dynamic") || key.contains("동적") -> Icons.Default.Memory
        key.contains("sort") || key.contains("정렬") -> Icons.Default.SwapVert
        key.contains("binary") || key.contains("이진") || key.contains("search") || key.contains("탐색") -> Icons.Default.Search
        key.contains("tree") || key.contains("트리") -> Icons.Default.AccountTree
        key.contains("greedy") || key.contains("탐욕") -> Icons.Default.FlashOn
        key.contains("backtrack") || key.contains("백트래킹") -> Icons.Default.Replay
        key.contains("divide") || key.contains("분할") -> Icons.Default.CallSplit
        key.contains("string") || key.contains("문자열") -> Icons.Default.TextFields
        key.contains("math") || key.contains("수학") -> Icons.Default.Functions
        key.contains("bit") || key.contains("비트") -> Icons.Default.Code
        key.contains("dfs") || key.contains("bfs") || key.contains("탐색") -> Icons.Default.TravelExplore
        else -> Icons.Default.Code
    }
}

fun getTopicIconBg(index: Int): Color = when (index % 3) {
    0    -> IconBgAmber
    1    -> IconBgSalmon
    else -> IconBgYellow
}

fun getTopicIconTint(index: Int): Color = when (index % 3) {
    0    -> iconTintAmber
    1    -> iconTintSalmon
    else -> iconTintYellow
}
