package com.example.fe.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
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
import com.example.fe.feature.home.HomeScreen
import com.example.fe.feature.list.ProblemListScreen
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.ui.EditorFullScreen
import com.example.fe.feature.solver.ui.EditorScreen
import com.example.fe.feature.solver.ui.SolveScreen

// 응용학습 임시 UI 확인용 import
import com.example.fe.feature.study.practice.PracticeUiState
import com.example.fe.feature.study.practice.dto.BlankDto
import com.example.fe.feature.study.practice.dto.QuizItemDto
import com.example.fe.feature.study.practice.ui.PracticeContent
import com.example.fe.feature.study.practice.ui.PracticeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val solverViewModel: SolverViewModel = viewModel()

    // 인증 상태 모니터링 및 화면 전환
    // 지금은 응용학습 UI 확인 중이라 잠시 비활성화
//    LaunchedEffect(authState) {
//        when (val state = authState) {
//            is AuthState.Success -> {
//                navController.navigate(Routes.HOME) { //로그인 성공 시 HOME으로 이동
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

        // 실제 시작 화면
        // startDestination = Routes.LOGIN

        // 임시: 응용학습 화면 바로 확인용
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

        // 3. 소셜 회원가입 추가 정보 입력 화면
        composable(Routes.SOCIAL_SIGNUP) {
            val currentState = authState
            val initialName =
                if (currentState is AuthState.NeedsExtraInfo) currentState.name else ""
            val initialEmail =
                if (currentState is AuthState.NeedsExtraInfo) currentState.email else ""

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

        // 6. 응용학습 화면
        composable(
            route = Routes.PRACTICE_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L

            /*
            ==========================================================
            서버 연결 시 사용 될 코드
            ==========================================================

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
            */

            // TODO: 백엔드 연결 시 위 PracticeScreen 주석 해제하고 아래 더미 코드 제거

            // ==========================================================
            // 임시 UI 확인용 더미 데이터 버전
            // 백엔드 미연결 상태에서 화면만 보기 위한 코드
            // ==========================================================
            PracticeContent(
                state = practicePreviewState(),
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNextStepClick = {},
                onCheckAnswer = { _, _ -> false }
            )
        }

        // 7. 문제 풀이 화면
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

        // 8. 에디터 화면
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

                    navController.navigate(Routes.solve(problemId)) {
                        launchSingleTop = true
                    }
                },
                onGoSubmit = {
                    navController.navigate(Routes.solve(problemId)) {
                        launchSingleTop = true
                    }
                },
                onFullscreenClick = {
                    navController.navigate(Routes.editorFull(problemId))
                }
            )
        }

        // 9. 전체화면 에디터
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

/*
==========================================================
응용학습 UI 확인용 더미 데이터
==========================================================
*/
private fun practicePreviewState(): PracticeUiState {
    return PracticeUiState(
        isLoading = false,
        quizzes = listOf(
            QuizItemDto(
                exerciseId = 1L,
                title = "해시맵으로 문자 개수 세기",
                description = "문자열에서 각 문자의 개수를 세는 코드의 빈칸을 채워보세요.",
                codeTemplate = """
function countChars(str) {
  const map = new ____();

  for (let char of str) {
    if (map.____(____)) {
      map.set(char, map.get(char) + 1);
    } else {
      map.____(char, ____);
    }
  }

  return map;
}
                """.trimIndent(),
                appliedCompleted = false,
                totalBlanks = 5,
                blanks = listOf(
                    BlankDto(content = "Map", answer = 1),
                    BlankDto(content = "has", answer = 2),
                    BlankDto(content = "char", answer = 3),
                    BlankDto(content = "set", answer = 4),
                    BlankDto(content = "1", answer = 5)
                )
            ),
            QuizItemDto(
                exerciseId = 2L,
                title = "해시 탐색 기본",
                description = "두 번째 문제 예시입니다.",
                codeTemplate = """
const map = new ____();
map.____("a", ____);
                """.trimIndent(),
                appliedCompleted = false,
                totalBlanks = 3,
                blanks = listOf(
                    BlankDto(content = "Map", answer = 1),
                    BlankDto(content = "set", answer = 2),
                    BlankDto(content = "1", answer = 3)
                )
            )
        ),
        error = null
    )
}