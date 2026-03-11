package com.example.fe.feature.solver.data

import com.example.fe.api.ApiService
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.RunResult
import com.example.fe.feature.solver.model.SolutionDetail
import com.example.fe.feature.solver.model.SubmitResult
import com.example.fe.feature.solver.model.SubmissionRecord
import kotlinx.coroutines.delay

class SolverRepository(private val apiService: ApiService) {

    private val DEFAULT_JAVA_TEMPLATE = """
import java.util.*;

public class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 여기에 코드를 작성하세요...
        return new int[] {};
    }
}
    """.trimIndent()

    suspend fun loadProblemDetail(problemId: Long, language: String): ProblemDetail {
        // TODO: 향후 진짜 API 호출 (apiService.getProblemDetail(problemId, language)) 대체 구간
        delay(200)

        return when (problemId) {
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
    }

    suspend fun runCode(problemId: Long, code: String, language: String): RunResult {
        // TODO: 진짜 API 호출 연동 (apiService.runCode(..))
        delay(800)

        val lines = listOf(
            "$ Running test cases...",
            "Test 1: Passed ✓",
            "Test 2: Passed ✓",
            "All tests passed!"
        )

        return RunResult(
            passed = true,
            runtimeMs = 123,
            errorMessage = null,
            terminalLines = lines
        )
    }

    suspend fun submitCode(problemId: Long, code: String, language: String): Pair<SubmitResult, SubmissionRecord> {
        // TODO: 진짜 API 호출 연동
        delay(700)
        val result = SubmitResult(isCorrect = true, runtimeMs = 210, errorMessage = null)
        val record = SubmissionRecord(
            date = "2026.02.09 13:40:00",
            language = "Java",
            result = if (result.isCorrect) "정답" else "오답",
            isCorrect = result.isCorrect
        )
        return Pair(result, record)
    }

    suspend fun loadSolution(problemId: Long, language: String): SolutionDetail {
        // TODO: 진짜 API 호출 연동
        delay(350)
        return SolutionDetail(
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
    }

    suspend fun loadSubmissionHistory(problemId: Long) {
        // TODO: 히스토리 API 연동
        delay(200)
    }
}
