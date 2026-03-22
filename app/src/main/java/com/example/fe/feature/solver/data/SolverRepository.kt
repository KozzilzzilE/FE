package com.example.fe.feature.solver.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.RunResult
import com.example.fe.feature.solver.model.SolutionDetail
import com.example.fe.feature.solver.model.SubmitResult
import com.example.fe.feature.solver.model.SubmissionRecord
import kotlinx.coroutines.delay

class SolverRepository(private val apiService: ApiService) {

    companion object {
        // 기능별 Mock 제어 플래그
        const val USE_MOCK_LOAD = false    // 문제 상세/솔루션 조회 (API 존재)
        const val USE_MOCK_RUN = true      // 코드 실행 (API 미구현)
        const val USE_MOCK_SUBMIT = true   // 코드 제출 (API 미구현)

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

    /**
     * 문제 상세 정보 로드
     */
    suspend fun loadProblemDetail(token: String, problemId: Long, language: String): ProblemDetail {
        if (USE_MOCK_LOAD) {
            delay(200)
            return getMockProblemDetail(problemId)
        }

        val response = apiService.getProblemDetail("Bearer $token", problemId, language)
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
            if (body.isSuccess) {
                val result = body.result ?: throw Exception("결과 데이터가 비어 있습니다.")

                val firstTest = result.testCases.firstOrNull()
                
                return ProblemDetail(
                    problemId = result.exerciseId,
                    title = result.title,
                    difficultyLabel = "-",
                    description = result.description,
                    exampleInput = firstTest?.input ?: "-",
                    exampleOutput = firstTest?.output ?: "-",
                    constraints = result.constraint.split("\n").filter { it.isNotBlank() },
                    initialCode = result.codeTemplate ?: DEFAULT_JAVA_TEMPLATE
                )
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("문제 상세 조회 실패: ${response.code()}")
        }
    }

    /**
     * 코드 실행 (Run 버튼)
     */
    suspend fun runCode(problemId: Long, code: String, language: String): RunResult {
        if (USE_MOCK_RUN) {
            Log.d("SolverRepository", "[MOCK] 코드 실행: problemId=$problemId")
            delay(800)
            return RunResult(
                passed = true,
                runtimeMs = 123,
                errorMessage = null,
                terminalLines = listOf(
                    "$ Running test cases...",
                    "Test 1: Passed ✓",
                    "Test 2: Passed ✓",
                    "All tests passed!"
                )
            )
        }
        // TODO: 실제 코드 실행 API 연동 필요
        error("코드 실행 API가 아직 서버에 구현되지 않았습니다.")
    }

    /**
     * 코드 제출 (Submit 버튼)
     */
    suspend fun submitCode(problemId: Long, code: String, language: String): Pair<SubmitResult, SubmissionRecord> {
        if (USE_MOCK_SUBMIT) {
            Log.d("SolverRepository", "[MOCK] 코드 제출: problemId=$problemId")
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
        // TODO: 실제 코드 제출 API 연동 필요
        error("코드 제출 API가 아직 서버에 구현되지 않았습니다.")
    }

    /**
     * 모범 답안 로드
     */
    suspend fun loadSolution(token: String, problemId: Long, language: String): SolutionDetail {
        if (USE_MOCK_LOAD) {
            Log.d("SolverRepository", "[MOCK] 모범 답안 조회: problemId=$problemId")
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

        val response = apiService.getProblemSolution("Bearer $token", problemId, language)
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
            if (body.isSuccess) {
                val result = body.result ?: throw Exception("모범 답안 데이터가 없습니다.")
                return SolutionDetail(
                    code = result.solutionCode,
                    explanation = result.lineSolution + "\n\n" + result.solutionText
                )
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("모범 답안 조회 실패: ${response.code()}")
        }
    }

    /**
     * 제출 이력 로드 (현재 Mock만 존재)
     */
    suspend fun loadSubmissionHistory(problemId: Long) {
        if (USE_MOCK_SUBMIT) { // 제출 관련이므로 SUBMIT 플래그 공유
            Log.d("SolverRepository", "[MOCK] 제출 기록 조회: problemId=$problemId")
            delay(200)
            return
        }
        // TODO: 실제 API 호출 (필요 시)
    }

    // ================================================================
    // Mock 데이터 (API 명세 기준)
    // ================================================================
    private fun getMockProblemDetail(problemId: Long): ProblemDetail {
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
            4L -> ProblemDetail(
                problemId = 4L,
                title = "의상",
                difficultyLabel = "보통",
                description = "코니가 가진 의상들이 주어질 때 서로 다른 옷의 조합의 수를 반환하세요.",
                exampleInput = "clothes=[[\"yellow_hat\",\"headgear\"],[\"blue_sunglasses\",\"eyewear\"],[\"green_turban\",\"headgear\"]]",
                exampleOutput = "5",
                constraints = listOf("1 ≤ clothes.length ≤ 30"),
                initialCode = DEFAULT_JAVA_TEMPLATE
            )
            5L -> ProblemDetail(
                problemId = 5L,
                title = "베스트앨범",
                difficultyLabel = "어려움",
                description = "장르별로 가장 많이 재생된 노래를 두 개씩 모아 베스트 앨범을 출시하세요.",
                exampleInput = "genres=[\"classic\",\"pop\",\"classic\",\"classic\",\"pop\"] plays=[500,600,150,800,2500]",
                exampleOutput = "[4,1,3,0]",
                constraints = listOf("1 ≤ genres.length ≤ 10,000"),
                initialCode = DEFAULT_JAVA_TEMPLATE
            )
            else -> ProblemDetail(
                problemId = problemId,
                title = "임시 문제 ($problemId)",
                difficultyLabel = "미정",
                description = "아직 mock 데이터가 준비되지 않았습니다.",
                exampleInput = "-",
                exampleOutput = "-",
                constraints = emptyList(),
                initialCode = DEFAULT_JAVA_TEMPLATE
            )
        }
    }
}

