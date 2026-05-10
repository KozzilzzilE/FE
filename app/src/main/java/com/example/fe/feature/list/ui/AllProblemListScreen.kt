package com.example.fe.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.ProblemListCard
import com.example.fe.common.bottomNavItems
import com.example.fe.feature.list.component.DifficultyChip
import com.example.fe.feature.list.component.PaginationBar
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.feature.list.model.AllProblemItem


enum class AllProblemDifficultyFilter {
    ALL, EASY, NORMAL, HARD
}




@Composable
fun AllProblemListScreen(
    problems: List<AllProblemItem>,
    selectedDifficulty: AllProblemDifficultyFilter,
    currentPage: Int,
    totalPages: Int,
    isLoading: Boolean = false,
    onDifficultySelected: (AllProblemDifficultyFilter) -> Unit,
    onProblemClick: (AllProblemItem) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    onPageChange: (Int) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "problem",
                onNavigate = onNavigate
            )
        },
        containerColor = BgPrimary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BgPrimary)
        ) {
            // plHdr
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "문제 목록",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // filterRow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
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
                    selected = selectedDifficulty == AllProblemDifficultyFilter.NORMAL,
                    onClick = { onDifficultySelected(AllProblemDifficultyFilter.NORMAL) }
                )
                DifficultyChip(
                    text = "어려움",
                    selected = selectedDifficulty == AllProblemDifficultyFilter.HARD,
                    onClick = { onDifficultySelected(AllProblemDifficultyFilter.HARD) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // plCards
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = com.example.fe.ui.theme.Primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(problems) { problem ->
                        ProblemListCard(
                            problem = problem,
                            onClick = { onProblemClick(problem) },
                            onBookmarkClick = { onBookmarkClick(problem.problemId) }
                        )
                    }
                }
            }

            PaginationBar(
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = onPageChange
            )
        }
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
                AllProblemItem(1, "배열 두 배 만들기", Difficulty.EASY, 120, false, false),
                AllProblemItem(2, "최빈값 구하기", Difficulty.MEDIUM, 85, false, true),
                AllProblemItem(3, "문자열 뒤집기", Difficulty.EASY, 210, true, false),
                AllProblemItem(4, "특정 문자 제거하기", Difficulty.HARD, 45, false, false),
                AllProblemItem(5, "다음에 올 숫자", Difficulty.HARD, 310, true, true)
            )
        )
    }

    val filteredProblems = sampleProblems.filter { problem ->
        when (selectedDifficulty) {
            AllProblemDifficultyFilter.ALL -> true
            AllProblemDifficultyFilter.EASY -> problem.difficulty == Difficulty.EASY
            AllProblemDifficultyFilter.NORMAL -> problem.difficulty == Difficulty.MEDIUM
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