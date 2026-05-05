package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.ui.AllProblemItem
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Blue
import com.example.fe.ui.theme.Error
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.Success
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary

@Composable
fun ProblemListCard(
    problem: AllProblemItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ProblemDifficultyBadge(difficulty = problem.difficulty)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "찜하기",
                        tint = if (problem.isBookmarked) Primary else BgElevated,
                        modifier = Modifier.clickable { onBookmarkClick() }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${problem.bookmarkCount}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = problem.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun ProblemDifficultyBadge(
    difficulty: Difficulty
) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY -> Color(0x2022C55E)   // Success 20%
        Difficulty.MEDIUM -> Color(0x203B82F6) // Blue 20%
        Difficulty.HARD -> Color(0x20FB2C36)   // Error 20%
    }

    val textColor = when (difficulty) {
        Difficulty.EASY -> Success
        Difficulty.MEDIUM -> Blue
        Difficulty.HARD -> Error
    }

    val label = when (difficulty) {
        Difficulty.EASY -> "쉬움"
        Difficulty.MEDIUM -> "보통"
        Difficulty.HARD -> "어려움"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
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
            .padding(16.dp)
    ) {
        ProblemListCard(
            problem = AllProblemItem(
                problemId = 1L,
                title = "배열 두 배 만들기",
                difficulty = Difficulty.EASY,
                bookmarkCount = 120,
                isBookmarked = true
            ),
            onClick = {},
            onBookmarkClick = {}
        )
    }
}