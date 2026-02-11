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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fe.common.bottomNavItems
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem
import com.example.fe.feature.auth.AuthViewModel
import com.example.fe.feature.auth.LoginScreen
import com.example.fe.feature.auth.SignUpScreen
import com.example.fe.feature.list.ProblemListScreen
import com.example.fe.ui.theme.FETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    
                    NavHost(navController = navController, startDestination = "login") {
                        // 1. 로그인 화면
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    authViewModel.login(email, password)
                                },
                                onSignUpClick = {
                                    navController.navigate("signup")
                                }
                            )
                        }

                        // 2. 회원가입 화면
                        composable("signup") {
                            SignUpScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onSignUpComplete = { name, id, password ->
                                    // 여기서는 일단 Firebase 이메일로 가입시킴 (아이디는 차후 처리)
                                    authViewModel.signUp(id, password) 
                                }
                            )
                        }

                        // 3. 메인 문제 목록 화면 (로그인 성공 시 이동할 곳)
                        composable("problem_list") {
                            // 기존 코드 연결
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
        }
    }
}