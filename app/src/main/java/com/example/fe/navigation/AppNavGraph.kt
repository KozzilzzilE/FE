package com.example.fe.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem
import com.example.fe.feature.auth.AuthViewModel
import com.example.fe.feature.auth.model.AuthState
import com.example.fe.feature.auth.ui.LoginScreen
import com.example.fe.feature.auth.ui.SignUpScreen
import com.example.fe.feature.list.ProblemListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    // 인증 상태 모니터링 및 화면 전환
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                navController.navigate(Routes.STUDY) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            is AuthState.SignedUp -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        // 1. 로그인 화면
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onSignUpClick = {
                    navController.navigate(Routes.SIGNUP)
                },
                onGoogleLoginClick = { idToken ->
                    authViewModel.signInWithGoogle(idToken)
                },
                onGithubLoginClick = { activity ->
                    authViewModel.signInWithGithub(activity)
                }
            )
        }

        // 2. 회원가입 화면
        composable(Routes.SIGNUP) {
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignUpComplete = { name, email, password, language ->
                    authViewModel.signUp(name, email, password, language) 
                },
                onGoogleSignUpClick = { idToken ->
                    authViewModel.signInWithGoogle(idToken)
                },
                onGithubSignUpClick = { activity ->
                    authViewModel.signInWithGithub(activity)
                }
            )
        }

        // 3. 메인 문제 목록 화면 (로그인 성공 시 이동할 곳)
        composable(Routes.STUDY) {
            val sampleProblems = listOf(
                Problem(1, "두 수의 합", Difficulty.EASY, false),
                Problem(2, "스택 구현하기", Difficulty.MEDIUM, true),
                Problem(3, "큐 활용하기", Difficulty.MEDIUM, false),
                Problem(4, "힙 정렬", Difficulty.HARD, false),
                Problem(5, "DFS 탐색", Difficulty.HARD, true),
            )
            
            ProblemListScreen(
                problems = sampleProblems,
                onItemClick = {},
                onNavigate = {}
            )
        }
    }
}
