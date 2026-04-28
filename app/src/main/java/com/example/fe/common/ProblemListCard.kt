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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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

@Composable
fun ProblemListCard(
    problem: AllProblemItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        imageVector = if (problem.isBookmarked) {
                            Icons.Filled.Star
                        } else {
                            Icons.Outlined.StarBorder
                        },
                        contentDescription = "즐겨찾기",
                        tint = if (problem.isBookmarked) Color(0xFFFFC107) else Color(0xFF94A3B8),
                        modifier = Modifier.clickable { onBookmarkClick() }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${problem.bookmarkCount}",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = problem.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }
    }
}

@Composable
private fun ProblemDifficultyBadge(
    difficulty: Difficulty
) {
    val backgroundColor = when (difficulty) {
        Difficulty.EASY -> Color(0xFFE7F8F0)
        Difficulty.MEDIUM -> Color(0xFFEAF1FF)
        Difficulty.HARD -> Color(0xFFFFEAEA)
    }

    val textColor = when (difficulty) {
        Difficulty.EASY -> Color(0xFF2EB67D)
        Difficulty.MEDIUM -> Color(0xFF5B8DEF)
        Difficulty.HARD -> Color(0xFFF26464)
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
            .background(Color(0xFFF7F9FB))
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