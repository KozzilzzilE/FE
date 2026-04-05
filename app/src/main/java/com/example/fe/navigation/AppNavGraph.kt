package com.example.fe.navigation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fe.feature.list.model.Difficulty
import com.example.fe.feature.list.model.Problem
import com.example.fe.feature.list.model.Concept
import com.example.fe.feature.list.model.Application
import com.example.fe.feature.auth.AuthViewModel
import com.example.fe.feature.auth.AuthViewModelFactory
import com.example.fe.feature.auth.data.AuthRepository
import com.example.fe.feature.auth.model.AuthState
import com.example.fe.feature.auth.ui.LoginScreen
import com.example.fe.feature.auth.ui.SignUpScreen
import com.example.fe.feature.list.ui.DetailListScreen
// 문제 목록 조회 추가
import com.example.fe.feature.list.ui.AllProblemListScreen
import com.example.fe.feature.list.ui.AllProblemDifficultyFilter
import com.example.fe.feature.list.ui.AllProblemItem
import com.example.fe.feature.step.ui.StepSelectionScreen
import com.example.fe.feature.list.ui.TopicListScreen
import com.example.fe.feature.home.ui.HomeScreen
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.SolverViewModelFactory
import com.example.fe.feature.solver.data.SolverRepository
import com.example.fe.feature.solver.ui.EditorFullScreen
import com.example.fe.feature.solver.ui.EditorScreen
import com.example.fe.feature.solver.ui.SolveScreen
import com.example.fe.feature.concept.ConceptViewModel
import com.example.fe.feature.concept.ConceptViewModelFactory
import com.example.fe.feature.concept.ui.ConceptDetailScreen
import com.example.fe.feature.practice.PracticeViewModel
import com.example.fe.feature.practice.PracticeViewModelFactory
import com.example.fe.feature.practice.data.PracticeRepository
// 마이페이지 추가
import com.example.fe.feature.profile.MyPageViewModel
import com.example.fe.feature.profile.MyPageViewModelFactory
import com.example.fe.feature.profile.data.ProfileRepository
import com.example.fe.feature.profile.ui.MyPageScreen
import com.example.fe.feature.profile.ui.EditProfileScreen
import com.example.fe.feature.profile.ui.LanguageSettingScreen
import com.example.fe.feature.list.ProblemListViewModel
import com.example.fe.feature.list.ProblemListViewModelFactory
import com.example.fe.feature.list.ProblemUiState
import com.example.fe.feature.list.data.ProblemRepository
import com.example.fe.api.RetrofitClient

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository(RetrofitClient.instance) }
    val authViewModelFactory = remember(authRepository) { AuthViewModelFactory(authRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val solverRepository = remember { SolverRepository(RetrofitClient.instance) }
    val solverViewModelFactory = remember(solverRepository) { SolverViewModelFactory(solverRepository) }
    val solverViewModel: SolverViewModel = viewModel(factory = solverViewModelFactory)

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
                    // [MOCK] 건너뛰기 시에도 가짜 토큰을 넣어서 Home API 실패 안 하게 방어
                    com.example.fe.common.TokenManager.saveAccessToken("mock_access_token_PocketCo_2026")
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

        // 4-1. 전체 문제 목록 화면 (문제 탭 누를 시) 임시 더미 데이터
        composable("problem") {
            var searchQuery by remember { mutableStateOf("") }
            var selectedDifficulty by remember { mutableStateOf(AllProblemDifficultyFilter.ALL) }

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
                onProblemClick = { problem ->
                    navController.navigate(Routes.solve(problem.problemId))
                },
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
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        //마이페이지
        composable(Routes.MY) {
            val profileRepository = remember {
                ProfileRepository(RetrofitClient.instance)
            }
            val factory = remember(profileRepository) {
                MyPageViewModelFactory(profileRepository)
            }
            val viewModel: MyPageViewModel = viewModel(factory = factory)

            MyPageScreen(
                viewModel = viewModel,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onEditProfileClick = {
                    navController.navigate(Routes.EDIT_PROFILE)
                },
                onLanguageClick = {
                    navController.navigate(Routes.LANGUAGE_SETTING)
                },
                onFavoriteClick = {},
                onSubmissionClick = {},
                onLogoutClick = {
                    com.example.fe.common.TokenManager.clearAccessToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {}
            )
        }
        //개인정보 수정 임시
        composable(Routes.EDIT_PROFILE) {
            val profileRepository = remember {
                ProfileRepository(RetrofitClient.instance)
            }
            val factory = remember(profileRepository) {
                MyPageViewModelFactory(profileRepository)
            }
            val viewModel: MyPageViewModel = viewModel(factory = factory)

            val uiState by viewModel.uiState.collectAsState()

            com.example.fe.feature.profile.ui.EditProfileScreen(
                initialName = uiState.userName,
                initialBio = uiState.bio,
                isSaving = uiState.isSaving,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name, bio ->
                    viewModel.updateProfileTemp(name, bio) {
                        navController.popBackStack()
                    }
                }
            )
        }
        //언어 수정 임의
        composable(Routes.LANGUAGE_SETTING) {
            val profileRepository = remember {
                ProfileRepository(RetrofitClient.instance)
            }
            val factory = remember(profileRepository) {
                MyPageViewModelFactory(profileRepository)
            }
            val viewModel: MyPageViewModel = viewModel(factory = factory)

            val uiState by viewModel.uiState.collectAsState()

            LanguageSettingScreen(
                initialLanguage = if (uiState.languageName.isBlank()) "Python" else uiState.languageName,
                isSaving = uiState.isSaving,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { language ->
                    viewModel.updatePreferredLanguageTemp(language) {
                        navController.popBackStack()
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
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. 알고리즘 분류 내 세부 목록 화면 (개념/응용/문제)
        composable(
            route = Routes.DETAIL_LIST_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.TOPIC_NAME) { type = NavType.StringType },
                navArgument(Routes.STEP_TYPE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 1L
            val topicName = backStackEntry.arguments?.getString(Routes.TOPIC_NAME) ?: "주제"
            val stepType = backStackEntry.arguments?.getString(Routes.STEP_TYPE) ?: "problem"

            when (stepType) {
                "concept" -> {
                    val factory = androidx.compose.runtime.remember { ConceptViewModelFactory() }
                    val conceptViewModel: ConceptViewModel = viewModel(factory = factory)
                    val uiState by conceptViewModel.uiState.collectAsState()

                    // 화면 진입 시 초기 데이터 로드
                    androidx.compose.runtime.LaunchedEffect(topicId) {
                        conceptViewModel.loadConcepts(topicId)
                    }

                    // 화면 복귀 시 데이터 리로드 (완료 상태 갱신)
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(topicId, lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                conceptViewModel.loadConcepts(topicId)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    // NotionDto -> Concept(DetailItem) 변환
                    val conceptItems = uiState.concepts.map { notion ->
                        Concept(
                            id = notion.notionId,
                            title = notion.title,
                            difficulty = Difficulty.EASY, // 개념 학습은 난이도 없음
                            isCompleted = notion.notionCompleted
                        )
                    }

                    if (uiState.isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else if (uiState.error != null) {
                        androidx.compose.material3.Text(
                            text = uiState.error ?: "오류 발생",
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else {
                        DetailListScreen(
                            screenTitle = "개념학습",
                            items = conceptItems,
                            onItemClick = { item ->
                                val index = conceptItems.indexOf(item).coerceAtLeast(0)
                                navController.navigate(Routes.concept(topicId, index))
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
                }

                "application" -> {
                    val factory = androidx.compose.runtime.remember {
                        PracticeViewModelFactory(PracticeRepository(RetrofitClient.instance))
                    }
                    val practiceViewModel: PracticeViewModel = viewModel(factory = factory)
                    val uiState by practiceViewModel.uiState.collectAsState()

                    // 화면 진입 시 초기 데이터 로드
                    androidx.compose.runtime.LaunchedEffect(topicId) {
                        practiceViewModel.loadQuizzes(topicId)
                    }

                    // 화면 복귀 시 데이터 리로드 (완료 상태 갱신)
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(topicId, lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                practiceViewModel.loadQuizzes(topicId)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    // QuizItemDto -> Application(DetailItem) 변환
                    val applicationItems = uiState.quizzes.map { quiz ->
                        Application(
                            id = quiz.exerciseId,
                            title = quiz.title,
                            difficulty = Difficulty.EASY,
                            isCompleted = quiz.appliedCompleted
                        )
                    }

                    if (uiState.isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else if (uiState.error != null) {
                        androidx.compose.material3.Text(
                            text = uiState.error ?: "오류 발생",
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else {
                        DetailListScreen(
                            screenTitle = "응용학습",
                            items = applicationItems,
                            onItemClick = { item ->
                                val index = applicationItems.indexOf(item).coerceAtLeast(0)
                                navController.navigate(Routes.practice(topicId, index))
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
                }

                else -> {
                    // 문제 풀이 (서버 API 연동)
                    val factory = androidx.compose.runtime.remember {
                        ProblemListViewModelFactory(ProblemRepository(RetrofitClient.instance))
                    }
                    val problemViewModel: ProblemListViewModel = viewModel(factory = factory)
                    val uiState by problemViewModel.uiState.collectAsState()

                    // 화면 진입 시 API 호출
                    androidx.compose.runtime.LaunchedEffect(topicId) {
                        problemViewModel.loadProblems(topicId)
                    }

                    if (uiState is ProblemUiState.Loading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else if (uiState is ProblemUiState.Success) {
                        val problems = (uiState as ProblemUiState.Success).problems.map { res ->
                            Problem(
                                id = res.problemId,
                                title = res.title,
                                difficulty = when (res.difficulty) {
                                    "EASY" -> Difficulty.EASY
                                    "NORMAL", "MEDIUM" -> Difficulty.MEDIUM
                                    "HARD" -> Difficulty.HARD
                                    else -> Difficulty.EASY
                                },
                                isCompleted = false // 서버 명세에 완료 여부 필드 추가 필요할 수 있음
                            )
                        }
                        DetailListScreen(
                            screenTitle = "문제학습",
                            items = problems,
                            onItemClick = { item ->
                                navController.navigate(Routes.solve(item.id))
                            },
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    } else {
                        // Error State
                        androidx.compose.material3.Text(
                            text = (uiState as? ProblemUiState.Error)?.message ?: "오류 발생",
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // ConceptDetailScreen: concept/{topicId}
        composable(
            route = Routes.CONCEPT_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.INITIAL_INDEX) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L
            val initialIndex = backStackEntry.arguments?.getInt(Routes.INITIAL_INDEX) ?: 0
            val conceptViewModelFactory = androidx.compose.runtime.remember { ConceptViewModelFactory() }
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptViewModelFactory)

            ConceptDetailScreen(
                topicId = topicId,
                initialIndex = initialIndex.toInt(),
                viewModel = conceptViewModel,
                onBack = { navController.popBackStack() },
                onHome = { 
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNextStepClick = {
                    // 이전 화면(DetailList)의 TOPIC_NAME 인자 가져오기 시도, 없으면 "주제"
                    val fallbackName = navController.previousBackStackEntry?.arguments?.getString(Routes.TOPIC_NAME) ?: "주제"
                    navController.navigate(Routes.detailList(topicId, fallbackName, "application")) {
                        popUpTo(Routes.STEP_ROUTE) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // PracticeScreen: practice/{topicId}/{initialIndex}
        composable(
            route = Routes.PRACTICE_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.INITIAL_INDEX) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L
            val initialIndex = backStackEntry.arguments?.getInt(Routes.INITIAL_INDEX) ?: 0
            
            val factory = androidx.compose.runtime.remember {
                PracticeViewModelFactory(PracticeRepository(RetrofitClient.instance))
            }
            val practiceViewModel: PracticeViewModel = viewModel(factory = factory)

            com.example.fe.feature.practice.ui.PracticeScreen(
                topicId = topicId,
                initialIndex = initialIndex,
                viewModel = practiceViewModel,
                onBack = { navController.popBackStack() },
                onHome = { 
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNextStepClick = {
                    val fallbackName = navController.previousBackStackEntry?.arguments?.getString(Routes.TOPIC_NAME) ?: "주제"
                    navController.navigate(Routes.detailList(topicId, fallbackName, "problem")) {
                        popUpTo(Routes.STEP_ROUTE) { inclusive = false }
                        launchSingleTop = true
                    }
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
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = false }
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
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
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
