package com.example.fe.feature.list.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.list.component.PaginationBar
import com.example.fe.feature.list.model.Difficulty

enum class AllProblemDifficultyFilter {
    ALL, EASY, MEDIUM, HARD
}

data class AllProblemItem(
    val problemId: Long,
    val title: String,
    val difficulty: Difficulty,
    val bookmarkCount: Int = 0,
    val isBookmarked: Boolean = false
)

@Composable
fun AllProblemListScreen(
    problems: List<AllProblemItem>,
    selectedDifficulty: AllProblemDifficultyFilter,
    currentPage: Int,
    totalPages: Int,
    onDifficultySelected: (AllProblemDifficultyFilter) -> Unit,
    onProblemClick: (AllProblemItem) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    onPageChange: (Int) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "문제 목록",
                showBackIcon = false,
                showHomeIcon = false,
                onBackClick = {},
                onHomeClick = {}
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "problem",
                onNavigate = onNavigate
            )
        },
        //containerColor = Color(0xFFF7F9FB)
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                //.background(Color(0xFFF7F9FB))
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE5E7EB)
            )

            DifficultyFilterRow(
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = onDifficultySelected,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(problems) { problem ->
                    AllProblemCard(
                        problem = problem,
                        onClick = { onProblemClick(problem) },
                        onBookmarkClick = { onBookmarkClick(problem.problemId) }
                    )
                }
            }

            PaginationBar(
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = onPageChange
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DifficultyFilterRow(
    selectedDifficulty: AllProblemDifficultyFilter,
    onDifficultySelected: (AllProblemDifficultyFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DifficultyChip(
            text = "전체",
            selected = selectedDifficulty == AllProblemDifficultyFilter.ALL,
            onClick = { onDifficultySelected(AllProblemDifficultyFilter.ALL) }
        )
        DifficultyChip(
            text = "쉬움",
            selected = selectedDifficulty == AllProblemDifficultyFilter.EASY,
            onClick = { onDifficultySelected(AllProblemDifficultyFilter.EASY) }
        )
        DifficultyChip(
            text = "보통",
            selected = selectedDifficulty == AllProblemDifficultyFilter.MEDIUM,
            onClick = { onDifficultySelected(AllProblemDifficultyFilter.MEDIUM) }
        )
        DifficultyChip(
            text = "어려움",
            selected = selectedDifficulty == AllProblemDifficultyFilter.HARD,
            onClick = { onDifficultySelected(AllProblemDifficultyFilter.HARD) }
        )
    }
}

@Composable
private fun DifficultyChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = CircleShape,
        color = if (selected) Color(0xFF7EA3F7) else Color.White,
        border = BorderStroke(
            1.dp,
            if (selected) Color(0xFF7EA3F7) else Color(0xFFE2E8F0)
        )
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF64748B),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun AllProblemCard(
    problem: AllProblemItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                DifficultyBadge(difficulty = problem.difficulty)

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
private fun DifficultyBadge(
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

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = difficulty.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AllProblemListScreenPreview() {
    var selectedDifficulty by remember { mutableStateOf(AllProblemDifficultyFilter.ALL) }
    var currentPage by remember { mutableIntStateOf(1) }
    var sampleProblems by remember {
        mutableStateOf(
            listOf(
                AllProblemItem(1, "배열 두 배 만들기", Difficulty.EASY, 120, false),
                AllProblemItem(2, "최빈값 구하기", Difficulty.MEDIUM, 85, false),
                AllProblemItem(3, "문자열 뒤집기", Difficulty.EASY, 210, true),
                AllProblemItem(4, "특정 문자 제거하기", Difficulty.HARD, 45, false),
                AllProblemItem(5, "다음에 올 숫자", Difficulty.HARD, 310, true)
            )
        )
    }

    val filteredProblems = sampleProblems.filter { problem ->
        when (selectedDifficulty) {
            AllProblemDifficultyFilter.ALL -> true
            AllProblemDifficultyFilter.EASY -> problem.difficulty == Difficulty.EASY
            AllProblemDifficultyFilter.MEDIUM -> problem.difficulty == Difficulty.MEDIUM
            AllProblemDifficultyFilter.HARD -> problem.difficulty == Difficulty.HARD
        }
    }

    AllProblemListScreen(
        problems = filteredProblems,
        selectedDifficulty = selectedDifficulty,
        currentPage = currentPage,
        totalPages = 3,
        onDifficultySelected = { selectedDifficulty = it },
        onProblemClick = {},
        onBookmarkClick = { problemId ->
            sampleProblems = sampleProblems.map { problem ->
                if (problem.problemId == problemId) {
                    problem.copy(isBookmarked = !problem.isBookmarked)
                } else {
                    problem
                }
            }
        },
        onPageChange = { currentPage = it },
        onNavigate = {}
    )
}