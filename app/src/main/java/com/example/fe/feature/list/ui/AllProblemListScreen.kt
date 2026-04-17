package com.example.fe.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.TopBar
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.list.component.AllProblemCard
import com.example.fe.feature.list.component.DifficultyChip
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
        containerColor = Color.White
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // 상단 구분선
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE5E7EB)
            )

            // 필터
            DifficultyFilterRow(
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = onDifficultySelected,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9FB))
                    .padding(top = 12.dp)   // 여기 추가
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(12.dp))
            }
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
            sampleProblems = sampleProblems.map {
                if (it.problemId == problemId) {
                    it.copy(isBookmarked = !it.isBookmarked)
                } else it
            }
        },
        onPageChange = { currentPage = it },
        onNavigate = {}
    )
}