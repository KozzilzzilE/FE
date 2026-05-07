package com.example.fe.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.model.AllProblemItem
import com.example.fe.ui.theme.*

@Composable
fun ProblemListCard(
    problem: AllProblemItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = BgSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 난이도 배지
            ProblemDifficultyBadge(difficulty = problem.difficulty)

            // 제목
            Text(
                text = problem.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 북마크 + 카운트
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "찜하기",
                        tint = if (problem.isBookmarked) Primary else BgElevated,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${problem.bookmarkCount}",
                    fontSize = 13.sp,
                    color = if (problem.isBookmarked) Primary else BgElevated
                )
            }

            // 완료 여부
            Icon(
                imageVector = if (problem.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                contentDescription = null,
                tint = if (problem.isCompleted) Success else BgElevated,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProblemDifficultyBadge(difficulty: Difficulty) {
    val bgColor = when (difficulty) {
        Difficulty.EASY   -> Color(0x1A22C55E)
        Difficulty.MEDIUM -> Color(0x1AF59E0B)
        Difficulty.HARD   -> Color(0x1AEF4444)
    }
    val textColor = when (difficulty) {
        Difficulty.EASY   -> Success
        Difficulty.MEDIUM -> Primary
        Difficulty.HARD   -> ErrorAlt
    }
    val label = when (difficulty) {
        Difficulty.EASY   -> "쉬움"
        Difficulty.MEDIUM -> "보통"
        Difficulty.HARD   -> "어려움"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProblemListCardPreview() {
    Column(
        modifier = Modifier
            .background(BgPrimary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProblemListCard(
            problem = AllProblemItem(1L, "배열 두 배 만들기", Difficulty.EASY, 120, false, true),
            onClick = {}, onBookmarkClick = {}
        )
        ProblemListCard(
            problem = AllProblemItem(2L, "최빈값 구하기", Difficulty.MEDIUM, 85, true, false),
            onClick = {}, onBookmarkClick = {}
        )
        ProblemListCard(
            problem = AllProblemItem(3L, "특정 문자 제거하기", Difficulty.HARD, 45, false, false),
            onClick = {}, onBookmarkClick = {}
        )
    }
}
