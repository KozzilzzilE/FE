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
        // ============================================================
        // ★ Mock 데이터 전환 플래그 ★
        // true  → 더미 데이터 사용 (서버 없이 UI 테스트)
        // false → 실제 API 호출 (서버 연결 시 변경)
        // ============================================================
        const val USE_MOCK = true

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

    suspend fun loadProblemDetail(problemId: Long, language: String): ProblemDetail {
        if (USE_MOCK) {
            delay(200)
            return getMockProblemDetail(problemId)
        }
        // TODO: 실제 API 호출
        // return apiService.getProblemDetail(problemId, language).body()!!.toDomain()
        error("실제 API 미구현")
    }

    suspend fun runCode(problemId: Long, code: String, language: String): RunResult {
        if (USE_MOCK) {
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
        // TODO: 실제 API 호출
        error("실제 API 미구현")
    }

    suspend fun submitCode(problemId: Long, code: String, language: String): Pair<SubmitResult, SubmissionRecord> {
        if (USE_MOCK) {
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
        // TODO: 실제 API 호출
        error("실제 API 미구현")
    }

    suspend fun loadSolution(problemId: Long, language: String): SolutionDetail {
        if (USE_MOCK) {
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
        // TODO: 실제 API 호출
        error("실제 API 미구현")
    }

    suspend fun loadSubmissionHistory(problemId: Long) {
        if (USE_MOCK) {
            Log.d("SolverRepository", "[MOCK] 제출 기록 조회: problemId=$problemId")
            delay(200)
            return
        }
        // TODO: 실제 API 호출
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
                title = "완주하지 못한 선수",
                difficultyLabel = "쉬움",
                description = "마라톤에 참여한 선수들 중 완주하지 못한 선수의 이름을 반환하세요.",
                exampleInput = "participant=[\"leo\",\"kiki\",\"eden\"], completion=[\"eden\",\"kiki\"]",
                exampleOutput = "\"leo\"",
                constraints = listOf("1 ≤ participant.length ≤ 100,000"),
                initialCode = DEFAULT_JAVA_TEMPLATE
            )
            3L -> ProblemDetail(
                problemId = 3L,
                title = "전화번호 목록",
                difficultyLabel = "보통",
                description = "전화번호부에 있는 어떤 번호가 다른 번호의 접두어인지 확인하세요.",
                exampleInput = "phone_book=[\"119\",\"97674223\",\"1195524421\"]",
                exampleOutput = "false",
                constraints = listOf("1 ≤ phone_book.length ≤ 1,000,000"),
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

