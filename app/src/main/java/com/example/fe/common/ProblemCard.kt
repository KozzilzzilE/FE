package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.list.model.DetailItem
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.model.Problem
import com.example.fe.ui.theme.*

@Composable
fun DetailCard(
    item: DetailItem,
    index: Int = 0,
    onClick: () -> Unit,
    onBookmarkClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val difficultyColor = when (item.difficulty) {
        Difficulty.EASY   -> Success
        Difficulty.MEDIUM -> Primary
        Difficulty.HARD   -> Error
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 번호 배지 - 40dp 원형
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryDim15),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (index > 0) index.toString() else item.id.toString(),
                    color = Primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.difficulty.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = difficultyColor
                )
            }

            // 북마크 (Problem 타입에만 표시)
            if (item is Problem) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onBookmarkClick?.invoke() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "북마크",
                            tint = if (item.isBookmarked) Primary else BgElevated
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.bookmarkCount}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            // 완료 여부 아이콘
            Icon(
                imageVector = if (item.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = if (item.isCompleted) Success else BgDivider,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY   -> Color(0x2022C55E)
        Difficulty.MEDIUM -> Color(0x20F59E0B)
        Difficulty.HARD   -> Color(0x20FB2C36)
    }
    val textColor = when (difficulty) {
        Difficulty.EASY   -> Success
        Difficulty.MEDIUM -> Primary
        Difficulty.HARD   -> Error
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = difficulty.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun DetailCardPreview() {
    Column(modifier = Modifier.background(BgPrimary)) {
        DetailCard(item = Problem(1L, "두 수의 합", Difficulty.EASY, false, 42, true), index = 1, onClick = {})
        DetailCard(item = Problem(2L, "스택 구현하기", Difficulty.MEDIUM, true, 15, false), index = 2, onClick = {})
        DetailCard(item = Problem(3L, "힙 정렬", Difficulty.HARD, false, 8, true), index = 3, onClick = {})
    }
}
