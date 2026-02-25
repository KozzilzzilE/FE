package com.example.fe.feature.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.solver.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        SolverUiState(
            code = DEFAULT_JAVA_TEMPLATE,
            testCases = listOf(
                TestCase(id = 1L, input = "nums = [2,7,11,15], target = 9", expectedOutput = "[0,1]"),
                TestCase(id = 2L, input = "nums = [3,2,4], target = 6", expectedOutput = "[1,2]")
            ),
            submissions = listOf(
                SubmissionRecord("2026.01.21 14:29:00", "Java", "정답", true),
                SubmissionRecord("2026.01.21 14:28:00", "Java", "오답", false)
            )
        )
    )
    val uiState: StateFlow<SolverUiState> = _uiState.asStateFlow()

    // 파생 Flow (기존 UI 호환용)
    private val started = SharingStarted.WhileSubscribed(5_000)

    val code: StateFlow<String> =
        uiState.map { it.code }.stateIn(viewModelScope, started, _uiState.value.code)

    val testCases: StateFlow<List<TestCase>> =
        uiState.map { it.testCases }.stateIn(viewModelScope, started, _uiState.value.testCases)

    val isRunning: StateFlow<Boolean> =
        uiState.map { it.isRunning }.stateIn(viewModelScope, started, _uiState.value.isRunning)

    val executionResult: StateFlow<List<String>?> =
        uiState.map { it.runResult?.terminalLines }
            .stateIn(viewModelScope, started, _uiState.value.runResult?.terminalLines)

    val submissions: StateFlow<List<SubmissionRecord>> =
        uiState.map { it.submissions }.stateIn(viewModelScope, started, _uiState.value.submissions)

    /** P-07 문제 상세 조회: /api/v1/problems/{problemId}?language=JAVA */
    fun loadProblemDetail(problemId: Long, language: String = _uiState.value.language) {
        viewModelScope.launch {
            _uiState.update { it.copy(problemId = problemId, language = language, isLoadingProblem = true) }

            delay(200)

            val detail = when (problemId) {
                1L -> ProblemDetail(
                    problemId = 1L,
                    title = "두 수의 합",
                    difficultyLabel = "쉬움",
                    description = "정수 배열 nums와 정수 target이 주어졌을 때, 합이 target이 되는 두 수의 인덱스를 반환하세요.",
                    exampleInput = "nums = [2, 7, 11, 15], target = 9",
                    exampleOutput = "[0, 1]",
                    constraints = listOf("2 ≤ nums.length ≤ 10⁴"),
                    initialCode = DEFAULT_JAVA_TEMPLATE
                )

                2L -> ProblemDetail(
                    problemId = 2L,
                    title = "스택 구현하기",
                    difficultyLabel = "보통",
                    description = "정수 스택을 구현하고 push/pop/top/isEmpty를 지원하세요.",
                    exampleInput = "push 1, push 2, top, pop, isEmpty",
                    exampleOutput = "top=2, pop=2, isEmpty=false",
                    constraints = listOf("연산 수 ≤ 100,000"),
                    initialCode = DEFAULT_JAVA_TEMPLATE
                )

                3L -> ProblemDetail(
                    problemId = 3L,
                    title = "큐 활용하기",
                    difficultyLabel = "보통",
                    description = "정수 큐를 구현하고 enqueue/dequeue/front/isEmpty를 지원하세요.",
                    exampleInput = "enqueue 3, enqueue 4, front, dequeue",
                    exampleOutput = "front=3, dequeue=3",
                    constraints = listOf("연산 수 ≤ 100,000"),
                    initialCode = DEFAULT_JAVA_TEMPLATE
                )

                else -> ProblemDetail(
                    problemId = problemId,
                    title = "임시 문제($problemId)",
                    difficultyLabel = "미정",
                    description = "아직 mock 데이터가 준비되지 않았습니다.",
                    exampleInput = "-",
                    exampleOutput = "-",
                    constraints = emptyList(),
                    initialCode = DEFAULT_JAVA_TEMPLATE
                )
            }

            _uiState.update { state ->
                state.copy(
                    isLoadingProblem = false,
                    problemDetail = detail,
                    // 초기 코드 주입: 빈 상태거나 기본 템플릿일 때만 덮어쓰기
                    code = if (state.code.isBlank() || state.code == DEFAULT_JAVA_TEMPLATE) detail.initialCode else state.code
                )
            }
        }
    }


    fun updateCode(newCode: String) {
        _uiState.update { it.copy(code = newCode) }
    }

    /** P-07 실행: /api/v1/problems/{problemId}/run */
    fun runCode() {
        val state = _uiState.value
        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isRunning = true, runResult = null) }

            // TODO: repository.run(problemId, code, language, 성공조건)
            delay(800)

            val lines = listOf(
                "$ Running test cases...",
                "Test 1: Passed ✓",
                "Test 2: Passed ✓",
                "All tests passed!"
            )

            _uiState.update {
                it.copy(
                    isRunning = false,
                    runResult = RunResult(
                        passed = true,
                        runtimeMs = 123,
                        errorMessage = null,
                        terminalLines = lines
                    )
                )
            }
        }
    }

    fun clearRunResult() {
        _uiState.update { it.copy(runResult = null) }
    }

    /** P-07-1 제출/채점: /api/v1/problems/{problemId}/submissions */
    fun submitCode() {
        val state = _uiState.value
        if (state.problemId == 0L) {
            _uiState.update { it.copy(errorToast = "problemId가 없습니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitResult = null) }

            // TODO: repository.submit(problemId, code, language, 성공조건)
            delay(700)

            val result = SubmitResult(isCorrect = true, runtimeMs = 210, errorMessage = null)
            val newRecord = SubmissionRecord(
                date = "2026.02.09 13:40:00",
                language = "Java",
                result = if (result.isCorrect) "정답" else "오답",
                isCorrect = result.isCorrect
            )

            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    submitResult = result,
                    submissions = listOf(newRecord) + it.submissions
                )
            }
        }
    }

    fun loadSubmissionHistory(problemId: Long = _uiState.value.problemId) {
        viewModelScope.launch {
            // TODO: repository.getSubmissionHistory(problemId)
            delay(200)
        }
    }

    /** P-08 모범 답안 조회: /api/v1/problems/{problemId}/solution?language=JAVA */
    fun loadSolution(problemId: Long = _uiState.value.problemId, language: String = _uiState.value.language) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSolution = true, solution = null) }

            // TODO: repository.getSolution(problemId, language)
            delay(350)

            _uiState.update {
                it.copy(
                    isLoadingSolution = false,
                    solution = SolutionDetail(
                        code = """
import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) return new int[]{ map.get(complement), i };
            map.put(nums[i], i);
        }
        return new int[]{};
    }
}
                        """.trimIndent(),
                        explanation = "해시맵을 사용하면 O(n)으로 해결할 수 있습니다."
                    )
                )
            }
        }
    }

    // 테스트케이스 관리
    fun addTestCase() {
        val nextId = (_uiState.value.testCases.maxOfOrNull { it.id } ?: 0L) + 1L
        _uiState.update { it.copy(testCases = it.testCases + TestCase(id = nextId)) }
    }

    fun removeTestCase(id: Long) {
        _uiState.update { it.copy(testCases = it.testCases.filterNot { tc -> tc.id == id }) }
    }

    fun updateTestCase(id: Long, input: String? = null, output: String? = null) {
        _uiState.update { state ->
            state.copy(
                testCases = state.testCases.map { tc ->
                    if (tc.id == id) tc.copy(
                        input = input ?: tc.input,
                        expectedOutput = output ?: tc.expectedOutput
                    ) else tc
                }
            )
        }
    }

    fun clearErrorToast() {
        _uiState.update { it.copy(errorToast = null) }
    }

    companion object {
        private val DEFAULT_JAVA_TEMPLATE = """
import java.util.*;

public class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 여기에 코드를 작성하세요...
        return new int[] {};
    }
}
        """.trimIndent()
    }
}
