package com.example.fe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fe.common.bottomNavItems
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem
import com.example.fe.feature.list.ProblemListScreen
import com.example.fe.ui.theme.FETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FETheme {
                val sampleProblems = listOf(
                    Problem(1, "두 수의 합", Difficulty.EASY, false),
                    Problem(2, "스택 구현하기", Difficulty.MEDIUM, true),
                    Problem(3, "큐 활용하기", Difficulty.MEDIUM, false),
                    Problem(4, "힙 정렬", Difficulty.HARD, false),
                    Problem(5, "DFS 탐색", Difficulty.HARD, true),
                )
                
                ProblemListScreen(
                    problems = sampleProblems,
                    onProblemClick = {},
                    onNavigate = {}
                )
            }
        }
    }
}