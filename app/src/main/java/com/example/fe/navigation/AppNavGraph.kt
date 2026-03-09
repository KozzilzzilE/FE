package com.example.fe.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fe.data.Difficulty
import com.example.fe.data.Problem
import com.example.fe.feature.auth.AuthViewModel
import com.example.fe.feature.auth.model.AuthState
import com.example.fe.feature.auth.ui.LoginScreen
import com.example.fe.feature.auth.ui.SignUpScreen
import com.example.fe.feature.list.ProblemListScreen
import com.example.fe.feature.home.HomeScreen
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.ui.EditorFullScreen
import com.example.fe.feature.solver.ui.EditorScreen
import com.example.fe.feature.solver.ui.SolveScreen

//임시 응용학습 관련 추가
import com.example.fe.feature.study.practice.PracticeViewModel
import com.example.fe.feature.study.practice.ui.PracticeScreen
import com.example.fe.feature.study.practice.PracticeUiState
import com.example.fe.feature.study.practice.dto.BlankDto
import com.example.fe.feature.study.practice.dto.QuizItemDto
import com.example.fe.feature.study.practice.ui.PracticeContent
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val solverViewModel: SolverViewModel = viewModel()

    // 인증 상태 모니터링 및 화면 전환

    //임시 비활성화

//    LaunchedEffect(authState) {
//        when (val state = authState) {
//            is AuthState.Success -> {
//                navController.navigate(Routes.HOME) { // 로그인 성공 시 HOME으로 이동
//                    popUpTo(Routes.LOGIN) { inclusive = true }
//                }
//            }
//            is AuthState.SignedUp -> {
//                Toast.makeText(context, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
//                navController.navigate(Routes.LOGIN) {
//                    popUpTo(Routes.LOGIN) { inclusive = true }
//                }
//            }
//            is AuthState.NeedsExtraInfo -> {
//                navController.navigate(Routes.SOCIAL_SIGNUP)
//            }
//            is AuthState.Error -> {
//                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
//            }
//            else -> {}
//        }
//    }

    NavHost(
        navController = navController,

        //기존 실제 시작 화면 LOGIN
        //startDestination = Routes.LOGIN

      //응용학습 ui 수정
        startDestination = Routes.practice(1L)

    ) {
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
                    authViewModel.signInWithGoogleLogin(idToken)
                },
                onGithubLoginClick = { activity ->
                    authViewModel.signInWithGithubLogin(activity)
                },
                onSkipLoginClick = {
                    // 개발용: 로그인 절차 없이 바로 홈 화면으로 직행
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
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
                    authViewModel.signInWithGoogleSignUp(idToken)
                },
                onGithubSignUpClick = { activity ->
                    authViewModel.signInWithGithubSignUp(activity)
                }
            )
        }

        // 3. 소셜 회원가입 시 추가 정보 입력 화면
        composable(Routes.SOCIAL_SIGNUP) {
            val currentState = authState
            val initialName = if (currentState is AuthState.NeedsExtraInfo) currentState.name else ""
            val initialEmail = if (currentState is AuthState.NeedsExtraInfo) currentState.email else ""
            
            com.example.fe.feature.auth.ui.SocialSignUpScreen(
                initialName = initialName,
                initialEmail = initialEmail,
                onNavigateBack = { navController.popBackStack() },
                onSignUpComplete = { name, email, lang ->
                    authViewModel.completeSocialSignUp(name, email, lang)
                }
            )
        }

        // 4. 메인 홈 화면
        composable(Routes.HOME) {
            HomeScreen(
                onNavigate = { route -> 
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 5. 메인 문제 목록 화면 (학습 탭 누를 시)
        composable(route = Routes.STUDY) {
            val sampleProblems = listOf(
                Problem(1, "두 수의 합", Difficulty.EASY, false),
                Problem(2, "스택 구현하기", Difficulty.MEDIUM, true),
                Problem(3, "큐 활용하기", Difficulty.MEDIUM, false),
                Problem(4, "힙 정렬", Difficulty.HARD, false),
                Problem(5, "DFS 탐색", Difficulty.HARD, true),
            )
            // 문제 리스트 화면으로 넘어갈 때 (main 브랜치 구조)
            ProblemListScreen(
                problems = sampleProblems,
                onProblemClick = { p ->
                    navController.navigate(Routes.solve(p.id.toLong()))
                },
                onNavigate = { route ->
                    when (route) {
                        Routes.HOME, Routes.STUDY, Routes.PROBLEM, Routes.MY -> {
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }

        // 응용학습 화면 2차 수정
        composable(
            route = Routes.PRACTICE_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L

            PracticeScreen(
                topicId = topicId,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNextStepClick = {
                    // 추후 개념학습 / 문제학습 연결 시 사용
                }
            )
        }



        // SolveScreen: solve/{problemId}
        composable(
            route = Routes.SOLVE_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L

            SolveScreen(
                problemId = problemId,
                viewModel = solverViewModel,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Routes.STUDY) {
                        popUpTo(Routes.STUDY) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onOpenEditorFull = { id ->
                    navController.navigate(Routes.editorFull(id))
                }
            )
        }

        // EditorScreen: editor/{problemId}
        composable(
            route = Routes.EDITOR_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L

            EditorScreen(
                problemId = problemId,
                viewModel = solverViewModel,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Routes.STUDY) {
                        popUpTo(Routes.STUDY) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onGoProblem = {
                    // 에디터에서 "문제" 탭 누르면 SolveScreen으로 (PROBLEM 탭이 보이게 할 거면 SolveScreen에서 탭 상태 처리)
                    navController.navigate(Routes.solve(problemId)) {
                        launchSingleTop = true
                    }
                },
                onGoSubmit = {
                    // 제출 탭도 SolveScreen에서 처리하는 구조라면 SolveScreen으로 보내고 SUBMIT 탭 선택은 SolveScreen 쪽에서 제어
                    navController.navigate(Routes.solve(problemId)) {
                        launchSingleTop = true
                    }
                },
                onFullscreenClick = {
                    navController.navigate(Routes.editorFull(problemId))
                }
            )
        }

        // EditorFullScreen: editor_full/{problemId}
        composable(
            route = Routes.EDITOR_FULL_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L

            EditorFullScreen(
                problemId = problemId,
                viewModel = solverViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
