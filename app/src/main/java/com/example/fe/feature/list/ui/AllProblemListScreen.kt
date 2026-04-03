package com.example.fe.feature.list.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
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
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDifficultySelected: (AllProblemDifficultyFilter) -> Unit,
    onProblemClick: (AllProblemItem) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    onFilterClick: () -> Unit = {},
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "문제",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "problem",
                onNavigate = onNavigate
            )
        },
        containerColor = Color(0xFFF7F9FB)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FB))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            SearchBarSection(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onFilterClick = onFilterClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            DifficultyFilterRow(
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = onDifficultySelected
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "전체 문제",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
        }
    }
}

@Composable
private fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF111827)
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "검색",
                        tint = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        if (query.isBlank()) {
                            Text(
                                text = "문제 번호나 제목을 검색해보세요",
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F5F9))
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "필터",
                tint = Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun DifficultyFilterRow(
    selectedDifficulty: AllProblemDifficultyFilter,
    onDifficultySelected: (AllProblemDifficultyFilter) -> Unit
) {
    Row(
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
    val backgroundColor = if (selected) Color(0xFF7EA3F7) else Color(0xFFF1F5F9)
    val textColor = if (selected) Color.White else Color(0xFF64748B)

    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = CircleShape,
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
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
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf(AllProblemDifficultyFilter.ALL) }
    var sampleProblems by remember {
        mutableStateOf(
            listOf(
                AllProblemItem(
                    problemId = 1,
                    title = "배열 두 배 만들기",
                    difficulty = Difficulty.EASY,
                    bookmarkCount = 120,
                    isBookmarked = false
                ),
                AllProblemItem(
                    problemId = 2,
                    title = "최빈값 구하기",
                    difficulty = Difficulty.MEDIUM,
                    bookmarkCount = 85,
                    isBookmarked = false
                ),
                AllProblemItem(
                    problemId = 3,
                    title = "문자열 뒤집기",
                    difficulty = Difficulty.EASY,
                    bookmarkCount = 210,
                    isBookmarked = true
                ),
                AllProblemItem(
                    problemId = 4,
                    title = "특정 문자 제거하기",
                    difficulty = Difficulty.HARD,
                    bookmarkCount = 45,
                    isBookmarked = false
                ),
                AllProblemItem(
                    problemId = 5,
                    title = "다음에 올 숫자",
                    difficulty = Difficulty.HARD,
                    bookmarkCount = 310,
                    isBookmarked = true
                )
            )
        )
    }

    val filteredProblems = sampleProblems.filter { problem ->
        val matchesSearch = problem.title.contains(searchQuery, ignoreCase = true)
        val matchesDifficulty = when (selectedDifficulty) {
            AllProblemDifficultyFilter.ALL -> true
            AllProblemDifficultyFilter.EASY -> problem.difficulty == Difficulty.EASY
            AllProblemDifficultyFilter.MEDIUM -> problem.difficulty == Difficulty.MEDIUM
            AllProblemDifficultyFilter.HARD -> problem.difficulty == Difficulty.HARD
        }
        matchesSearch && matchesDifficulty
    }

    AllProblemListScreen(
        problems = filteredProblems,
        selectedDifficulty = selectedDifficulty,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
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
        onFilterClick = {},
        onNavigate = {}
    )
}