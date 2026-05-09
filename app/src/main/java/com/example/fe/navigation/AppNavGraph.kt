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
import com.example.fe.feature.list.ui.AllProblemListScreen
import com.example.fe.feature.list.ui.AllProblemDifficultyFilter
import com.example.fe.feature.list.model.AllProblemItem
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
import com.example.fe.feature.practice.ui.PracticeScreen
import com.example.fe.feature.practice.PracticeViewModel
import com.example.fe.feature.practice.PracticeViewModelFactory
import com.example.fe.feature.practice.data.PracticeRepository

import com.example.fe.feature.profile.MyPageViewModel
import com.example.fe.feature.profile.MyPageViewModelFactory
import com.example.fe.feature.profile.data.ProfileRepository
import com.example.fe.feature.profile.ui.MyPageScreen
import com.example.fe.feature.profile.ui.EditProfileScreen
import com.example.fe.feature.profile.ui.LanguageSettingScreen
import com.example.fe.feature.profile.ui.BookmarkScreen

import com.example.fe.feature.list.ProblemListViewModel
import com.example.fe.feature.list.ProblemListViewModelFactory
import com.example.fe.feature.list.ProblemUiState
import com.example.fe.feature.list.data.ProblemRepository
import com.example.fe.feature.profile.BookmarkViewModel
import com.example.fe.feature.profile.BookmarkViewModelFactory
import com.example.fe.feature.profile.data.BookmarkRepository
import com.example.fe.feature.list.AllProblemListViewModel
import com.example.fe.feature.list.AllProblemListViewModelFactory
import com.example.fe.api.RetrofitClient
import com.example.fe.feature.aireview.SubmissionViewModel
import com.example.fe.feature.aireview.ui.SubmissionRecordScreen
import com.example.fe.feature.aireview.ui.SubmissionDetailScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application

    val authRepository = remember { AuthRepository(RetrofitClient.instance) }
    val authViewModelFactory = remember(authRepository) { AuthViewModelFactory(authRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val authState by authViewModel.authState.collectAsState()

    val solverRepository = remember { SolverRepository(RetrofitClient.instance) }
    val solverViewModelFactory = remember(solverRepository) {
        SolverViewModelFactory(solverRepository)
    }
    val solverViewModel: SolverViewModel = viewModel(factory = solverViewModelFactory)

    // 제출 기록 / AI 리뷰 ViewModel
    val submissionViewModel: SubmissionViewModel = viewModel(
        factory = remember { com.example.fe.feature.aireview.SubmissionViewModelFactory(RetrofitClient.instance) }
    )

    // 마이페이지 ViewModel은 여기서 1번만 생성해서 공유
    val profileRepository = remember { ProfileRepository(RetrofitClient.instance) }
    val profileViewModelFactory = remember(profileRepository) {
        MyPageViewModelFactory(application, profileRepository)
    }
    val profileViewModel: MyPageViewModel = viewModel(factory = profileViewModelFactory)
    val profileUiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                navController.navigate(Routes.HOME) {
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
            is AuthState.LoggedOut -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
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
                    com.example.fe.common.TokenManager.saveAccessToken("mock_access_token_PocketCo_2026")
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

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

        composable("problem") {
            val factory = remember {
                AllProblemListViewModelFactory(ProblemRepository(RetrofitClient.instance))
            }
            val viewModel: AllProblemListViewModel = viewModel(factory = factory)
            val uiState by viewModel.uiState.collectAsState()
            val currentPage by viewModel.currentPage.collectAsState()
            val totalPages by viewModel.totalPages.collectAsState()
            val selectedDifficultyStr by viewModel.selectedDifficulty.collectAsState()

            // ViewModel의 문자열 난이도 상태를 UI용 Enum으로 변환
            val selectedDifficulty = when (selectedDifficultyStr) {
                "EASY" -> AllProblemDifficultyFilter.EASY
                "NORMAL", "MEDIUM" -> AllProblemDifficultyFilter.MEDIUM
                "HARD" -> AllProblemDifficultyFilter.HARD
                else -> AllProblemDifficultyFilter.ALL
            }

            LaunchedEffect(Unit) {
                viewModel.loadAllProblems()
            }

            val problems = if (uiState is ProblemUiState.Success) {
                (uiState as ProblemUiState.Success).problems.map { res ->
                    AllProblemItem(
                        problemId = res.problemId,
                        title = res.title,
                        difficulty = when (res.difficultyDisplayName) {
                            "쉬움" -> Difficulty.EASY
                            "보통", "중간" -> Difficulty.MEDIUM
                            "어려움" -> Difficulty.HARD
                            else -> when (res.difficulty) {
                                "EASY" -> Difficulty.EASY
                                "NORMAL", "MEDIUM" -> Difficulty.MEDIUM
                                "HARD" -> Difficulty.HARD
                                else -> Difficulty.EASY
                            }
                        },
                        bookmarkCount = res.bookmarkCount ?: 0,
                        isBookmarked = res.isBookmark ?: false,
                        isCompleted = res.isCompleted ?: false
                    )
                }
            } else emptyList()

            AllProblemListScreen(
                problems = problems,
                selectedDifficulty = selectedDifficulty,
                currentPage = currentPage,
                totalPages = totalPages,
                isLoading = uiState is ProblemUiState.Loading,
                onDifficultySelected = { filter ->
                    val diffStr = when (filter) {
                        AllProblemDifficultyFilter.EASY -> "EASY"
                        AllProblemDifficultyFilter.MEDIUM -> "NORMAL"
                        AllProblemDifficultyFilter.HARD -> "HARD"
                        else -> null
                    }
                    viewModel.loadAllProblems(page = 1, difficulty = diffStr)
                },
                onProblemClick = { item ->
                    navController.navigate(Routes.solve(item.problemId, item.difficulty.name))
                },
                onBookmarkClick = { problemId ->
                    val isBookmarked = problems.find { it.problemId == problemId }?.isBookmarked ?: false
                    viewModel.toggleBookmark(problemId, isBookmarked)
                },
                onPageChange = { viewModel.loadAllProblems(page = it) },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 마이페이지
        composable(Routes.MY) {
            MyPageScreen(
                viewModel = profileViewModel,
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
                onFavoriteClick = {
                    navController.navigate(Routes.FAVORITE_PROBLEMS)
                },
                onSubmissionClick = {
                    navController.navigate(Routes.SUBMISSION_RECORD)
                },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onSettingsClick = {}
            )
        }

        // 개인정보 수정
        composable(Routes.EDIT_PROFILE) {
            LaunchedEffect(profileUiState.error) {
                profileUiState.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            EditProfileScreen(
                initialName = profileUiState.userName,
                isSaving = profileUiState.isSaving,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name ->
                    profileViewModel.updateProfile(name) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // 선호 언어 수정
        composable(Routes.LANGUAGE_SETTING) {
            LaunchedEffect(profileUiState.error) {
                profileUiState.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            LanguageSettingScreen(
                initialLanguage = if (profileUiState.languageName.isBlank()) "JAVA" else profileUiState.languageName,
                languages = profileUiState.languageOptions,
                isSaving = profileUiState.isSaving,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { language ->
                    profileViewModel.updatePreferredLanguage(language) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable(Routes.FAVORITE_PROBLEMS) {
            val factory = remember { BookmarkViewModelFactory(BookmarkRepository(RetrofitClient.instance)) }
            val bookmarkViewModel: BookmarkViewModel = viewModel(factory = factory)
            BookmarkScreen(
                viewModel = bookmarkViewModel,
                onBackClick = { navController.popBackStack() },
                onProblemClick = { problemId ->
                    navController.navigate(Routes.solve(problemId))
                }
            )
        }

        // 제출 기록 목록
        composable(Routes.SUBMISSION_RECORD) {
            SubmissionRecordScreen(
                viewModel = submissionViewModel,
                onBack = { navController.popBackStack() },
                onEntryClick = { historyId ->
                    navController.navigate(Routes.submissionDetail(historyId))
                }
            )
        }

        // 제출 상세 / AI 코드 리뷰
        composable(
            route = Routes.SUBMISSION_DETAIL_ROUTE,
            arguments = listOf(navArgument(Routes.HISTORY_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val historyId = backStackEntry.arguments?.getLong(Routes.HISTORY_ID) ?: return@composable
            LaunchedEffect(historyId) { submissionViewModel.selectEntry(historyId) }
            SubmissionDetailScreen(
                viewModel = submissionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

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
                    val factory = remember { ConceptViewModelFactory() }
                    val conceptViewModel: ConceptViewModel = viewModel(factory = factory)
                    val uiState by conceptViewModel.uiState.collectAsState()

                    val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                        .getLanguage(context)
                        .ifBlank { "JAVA" }

                    LaunchedEffect(topicId, preferredLanguage) {
                        conceptViewModel.loadConcepts(topicId, preferredLanguage)
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(topicId, lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                val language = com.example.fe.common.LanguagePreferenceManager
                                    .getLanguage(context)
                                    .ifBlank { "JAVA" }
                                conceptViewModel.loadConcepts(topicId, language)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    val conceptItems = uiState.concepts.map { notion ->
                        Concept(
                            id = notion.notionId,
                            title = notion.title,
                            difficulty = Difficulty.EASY,
                            isCompleted = notion.notionCompleted
                        )
                    }

                    if (uiState.error != null) {
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
                    val factory = remember {
                        PracticeViewModelFactory(PracticeRepository(RetrofitClient.instance))
                    }
                    val practiceViewModel: PracticeViewModel = viewModel(factory = factory)
                    val uiState by practiceViewModel.uiState.collectAsState()

                    val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                        .getLanguage(context)
                        .ifBlank { "JAVA" }

                    LaunchedEffect(topicId, preferredLanguage) {
                        practiceViewModel.loadQuizzes(topicId, preferredLanguage)
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(topicId, lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                val language = com.example.fe.common.LanguagePreferenceManager
                                    .getLanguage(context)
                                    .ifBlank { "JAVA" }
                                practiceViewModel.loadQuizzes(topicId, language)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    val applicationItems = uiState.quizzes.map { quiz ->
                        Application(
                            id = quiz.exerciseId,
                            title = quiz.title,
                            difficulty = Difficulty.EASY,
                            isCompleted = quiz.appliedCompleted
                        )
                    }

                    if (uiState.error != null) {
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
                    val factory = remember {
                        ProblemListViewModelFactory(ProblemRepository(RetrofitClient.instance))
                    }
                    val problemViewModel: ProblemListViewModel = viewModel(factory = factory)
                    val uiState by problemViewModel.uiState.collectAsState()

                    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                    androidx.compose.runtime.DisposableEffect(lifecycleOwner, topicId) {
                        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                                problemViewModel.loadProblems(topicId)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    if (uiState is ProblemUiState.Error) {
                        androidx.compose.material3.Text(
                            text = (uiState as ProblemUiState.Error).message,
                            modifier = androidx.compose.ui.Modifier.fillMaxSize()
                        )
                    } else {
                        val problems = if (uiState is ProblemUiState.Success) {
                            (uiState as ProblemUiState.Success).problems.map { res ->
                                Problem(
                                    id = res.problemId,
                                    title = res.title,
                                    difficulty = when (res.difficultyDisplayName) {
                                        "쉬움" -> Difficulty.EASY
                                        "보통", "중간" -> Difficulty.MEDIUM
                                        "어려움" -> Difficulty.HARD
                                        else -> when (res.difficulty) {
                                            "EASY" -> Difficulty.EASY
                                            "NORMAL", "MEDIUM" -> Difficulty.MEDIUM
                                            "HARD" -> Difficulty.HARD
                                            else -> Difficulty.EASY
                                        }
                                    },
                                    isCompleted = res.isCompleted ?: false,
                                    bookmarkCount = res.bookmarkCount ?: 0,
                                    isBookmarked = res.isBookmark ?: false
                                )
                            }
                        } else emptyList()

                        DetailListScreen(
                            screenTitle = "문제학습",
                            items = problems,
                            onItemClick = { item ->
                                navController.navigate(Routes.solve(item.id, item.difficulty.name))
                            },
                            onBookmarkClick = { item ->
                                if (item is Problem) {
                                    problemViewModel.toggleBookmark(item.id, item.isBookmarked)
                                }
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
            }
        }

        composable(
            route = Routes.CONCEPT_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.INITIAL_INDEX) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L
            val initialIndex = backStackEntry.arguments?.getInt(Routes.INITIAL_INDEX) ?: 0
            val conceptViewModelFactory = remember { ConceptViewModelFactory() }
            val conceptViewModel: ConceptViewModel = viewModel(factory = conceptViewModelFactory)
            val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                .getLanguage(context)
                .ifBlank { "JAVA" }

            LaunchedEffect(topicId, preferredLanguage) {
                conceptViewModel.loadConcepts(topicId, preferredLanguage, initialIndex)
            }

            ConceptDetailScreen(
                topicId = topicId,
                initialIndex = initialIndex,
                viewModel = conceptViewModel,
                onBack = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNextStepClick = {
                    val fallbackName = navController.previousBackStackEntry?.arguments?.getString(Routes.TOPIC_NAME) ?: "주제"
                    navController.navigate(Routes.detailList(topicId, fallbackName, "application")) {
                        popUpTo(Routes.STEP_ROUTE) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.PRACTICE_ROUTE,
            arguments = listOf(
                navArgument(Routes.TOPIC_ID) { type = NavType.LongType },
                navArgument(Routes.INITIAL_INDEX) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong(Routes.TOPIC_ID) ?: 0L
            val initialIndex = backStackEntry.arguments?.getInt(Routes.INITIAL_INDEX) ?: 0

            val factory = remember {
                PracticeViewModelFactory(PracticeRepository(RetrofitClient.instance))
            }
            val practiceViewModel: PracticeViewModel = viewModel(factory = factory)
            val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                .getLanguage(context)
                .ifBlank { "JAVA" }

            LaunchedEffect(topicId, preferredLanguage) {
                practiceViewModel.loadQuizzes(topicId, preferredLanguage)
            }

            PracticeScreen(
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

        composable(
            route = Routes.SOLVE_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType },
                navArgument("difficulty") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L
            val difficulty = backStackEntry.arguments?.getString("difficulty")
            val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                .getLanguage(context)
                .ifBlank { "JAVA" }

            LaunchedEffect(problemId, preferredLanguage, difficulty) {
                solverViewModel.loadProblemDetail(problemId, preferredLanguage, difficulty)
            }

            // 마이페이지에서 돌아왔을 때 언어 변경 감지를 위한 Observer 추가
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner, problemId) {
                val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                    if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                        val currentLang = com.example.fe.common.LanguagePreferenceManager
                            .getLanguage(context)
                            .ifBlank { "JAVA" }
                        // 현재 ViewModel의 언어와 다르면 다시 로드
                        if (solverViewModel.uiState.value.language != currentLang) {
                            solverViewModel.loadProblemDetail(problemId, currentLang, difficulty)
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

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
                },
                onNextProblem = { nextId ->
                    navController.navigate(Routes.solve(nextId))
                }
            )
        }

        composable(
            route = Routes.EDITOR_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L
            val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                .getLanguage(context)
                .ifBlank { "JAVA" }

            LaunchedEffect(problemId, preferredLanguage) {
                solverViewModel.loadProblemDetail(problemId, preferredLanguage)
            }

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

        composable(
            route = Routes.EDITOR_FULL_ROUTE,
            arguments = listOf(
                navArgument(Routes.PROBLEM_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getLong(Routes.PROBLEM_ID) ?: 0L
            val preferredLanguage = com.example.fe.common.LanguagePreferenceManager
                .getLanguage(context)
                .ifBlank { "JAVA" }

            LaunchedEffect(problemId, preferredLanguage) {
                solverViewModel.loadProblemDetail(problemId, preferredLanguage)
            }

            EditorFullScreen(
                problemId = problemId,
                viewModel = solverViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // CS 퀴즈
        composable(Routes.CS_QUIZ) {
            com.example.fe.feature.csquiz.ui.CsQuizScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}