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
import com.example.fe.feature.list.ui.ProblemListScreen
import com.example.fe.feature.list.ui.StepSelectionScreen
import com.example.fe.feature.list.ui.TopicListScreen
import com.example.fe.feature.home.HomeScreen
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.ui.EditorFullScreen
import com.example.fe.feature.solver.ui.EditorScreen
import com.example.fe.feature.solver.ui.SolveScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val solverViewModel: SolverViewModel = viewModel()

    // 인증 상태 모니터링 및 화면 전환
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                navController.navigate(Routes.HOME) { // 로그인 성공 시 HOME으로 이동
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            is AuthState.SignedUp -> {
                Toast.makeText(context, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            is AuthState.NeedsExtraInfo -> {
                navController.navigate(Routes.SOCIAL_SIGNUP)
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    NavHost(
        navController = navController, 
        startDestination = Routes.LOGIN
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

        // 5. 메인 알고리즘 주제 목록 화면 (학습 탭 누를 시)
        composable(route = Routes.TOPIC) {
            TopicListScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 5-1. 학습 단계 선택 화면 (주제 선택 시 진입)
        composable(
            route = Routes.STEP_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.TOPIC_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 1L
            val topicName = backStackEntry.arguments?.getString(Routes.TOPIC_NAME) ?: "주제"

            StepSelectionScreen(
                topicId = topicId,
                topicName = topicName,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 6. 알고리즘 분류 내 세부 문제 목록 화면
        composable(route = Routes.PROBLEM) {
            // TODO: 추후 TopicListScreen에서 선택한 특정 주제 ID를 받아 해당 문제들만 필터링하는 로직 필요
            ProblemListScreen(
                problems = emptyList(), // 당장 API가 없으므로 임시 빈 리스트나 샘플 할당
                onProblemClick = { problem ->
                    navController.navigate(Routes.solve(problem.id.toLong()))
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBackClick = { navController.popBackStack() }
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
            navController.navigate(Routes.TOPIC) {
                popUpTo(Routes.TOPIC) { inclusive = false }
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
                    navController.navigate(Routes.TOPIC) {
                        popUpTo(Routes.TOPIC) { inclusive = false }
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
