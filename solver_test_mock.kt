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
                    initialCode = DEFAULT_JAVA_TEMPLATE
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
        // TODO: 실제 코드 실행 API 연동 필요 (현재 서버 미구현)
        // 임시로 내부 Mock 로직 유지
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

    /**
     * 코드 제출 (Submit 버튼)
     */
    suspend fun submitCode(problemId: Long, code: String, language: String): Pair<SubmitResult, SubmissionRecord> {
        // TODO: 실제 코드 제출 API 연동 필요 (현재 서버 미구현)
        // 임시로 내부 Mock 로직 유지
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

    /**
     * 모범 답안 로드
     */
    suspend fun loadSolution(token: String, problemId: Long, language: String): SolutionDetail {
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
        Log.d("SolverRepository", "[MOCK] 제출 기록 조회: problemId=$problemId")
        delay(200)
        return
    }
}
