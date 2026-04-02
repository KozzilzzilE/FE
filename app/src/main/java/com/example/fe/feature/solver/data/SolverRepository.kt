package com.example.fe.feature.solver.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.RunResult
import com.example.fe.feature.solver.model.SolutionDetail
import com.example.fe.feature.solver.model.SubmitResult
import com.example.fe.feature.solver.model.SubmissionRecord
import com.example.fe.feature.solver.model.TestCase

class SolverRepository(private val apiService: ApiService) {

    companion object {
        private val DEFAULT_JAVA_TEMPLATE = """
import java.util.*;

public class Solution {
    public int[] solution(int[] nums) {
        // 여기에 코드를 작성하세요...
        return new int[] {};
    }
}
        """.trimIndent()
    }

    suspend fun loadProblemDetail(token: String, problemId: Long, language: String): ProblemDetail {
        val response = apiService.getProblemDetail("Bearer $token", problemId, language)
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
            if (body.isSuccess) {
                val result = body.result ?: throw Exception("결과 데이터가 비어 있습니다.")

                val mappedTestCases = result.testCases.mapIndexed { index, tc ->
                    TestCase(
                        id = (index + 1).toLong(),
                        input = tc.input,
                        expectedOutput = tc.output
                    )
                }

                val firstTest = mappedTestCases.firstOrNull()

                return ProblemDetail(
                    problemId = result.exerciseId,
                    title = result.title,
                    difficultyLabel = "-",
                    description = result.description,
                    exampleInput = firstTest?.input ?: "-",
                    exampleOutput = firstTest?.expectedOutput ?: "-",
                    constraints = result.constraint.split("\n").filter { it.isNotBlank() },
                    initialCode = DEFAULT_JAVA_TEMPLATE,
                    testCases = mappedTestCases
                )
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("문제 상세 조회 실패: ${response.code()}")
        }
    }

    suspend fun runCode(token: String, problemId: Long, code: String, language: String): RunResult {
        val response = apiService.runCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language,
            request = com.example.fe.data.dto.RunRequestDto(
                sourceCode = code,
                timeLimit = 1.0,
                memoryLimit = 128000000
            )
        )

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")

            if (body.isSuccess) {
                val resultList = body.result ?: emptyList()
                val firstToken = resultList.firstOrNull()?.token

                return RunResult(
                    passed = true,
                    runtimeMs = null,
                    errorMessage = null,
                    terminalLines = listOf(
                        "$ Run request sent",
                        "실행 토큰: ${firstToken ?: "없음"}"
                    )
                )
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("코드 실행 실패: ${response.code()}")
        }
    }

    suspend fun submitCode(token: String, problemId: Long, code: String, language: String): Pair<SubmitResult, SubmissionRecord> {
        val response = apiService.submitCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language,
            request = com.example.fe.data.dto.SubmitRequestDto(
                sourceCode = code,
                timeLimit = 1.0,
                memoryLimit = 128000000
            )
        )

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")

            if (body.isSuccess) {
                val submissionId = body.result?.submissionId ?: "unknown"

                val result = SubmitResult(
                    isCorrect = true,
                    runtimeMs = null,
                    errorMessage = null
                )
                val record = SubmissionRecord(
                    date = "방금",
                    language = language,
                    result = "제출 완료",
                    isCorrect = true
                )
                Log.d("SolverRepository", "제출 완료: submissionId=$submissionId")
                return Pair(result, record)
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("코드 제출 실패: ${response.code()}")
        }
    }

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

    suspend fun loadSubmissionHistory(problemId: Long) {
        // TODO: 실제 API 호출 (서버 구현 후 연동)
    }
}
