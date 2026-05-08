package com.example.fe.feature.solver.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.RunRequestDto
import com.example.fe.data.dto.SubmitRequestDto
import com.example.fe.feature.solver.model.ProblemDetail
import com.example.fe.feature.solver.model.RunResult
import com.example.fe.feature.solver.model.SolutionDetail
import com.example.fe.feature.solver.model.TestCase

data class SubmitInfo(
    val historyId: Long,
    val submissionId: String
)

class SolverRepository(
    private val apiService: ApiService
) {

    companion object {
        fun getInitialCode(language: String): String {
            return CodeTemplates.getInitialCode(language)
        }
    }

    /**
     * 문제 상세 조회
     */
    suspend fun loadProblemDetail(
        token: String,
        problemId: Long,
        language: String,
        difficultyLabel: String? = null
    ): ProblemDetail {
        val response = apiService.getProblemDetail("Bearer $token", problemId, language)

        if (!response.isSuccessful) {
            throw Exception("문제 상세 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

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
            problemId = result.problemId,
            title = result.title,
            difficultyLabel = difficultyLabel ?: "-",
            description = result.description,
            exampleInput = firstTest?.input ?: "-",
            exampleOutput = firstTest?.expectedOutput ?: "-",
            constraints = result.constraint
                .split("\n")
                .filter { it.isNotBlank() },
            initialCode = getInitialCode(language),
            testCases = mappedTestCases,
            isBookmarked = result.isBookmark ?: false,
            bookmarkCount = result.bookmarkCount ?: 0
        )
    }

    /**
     * 북마크 토글
     */
    suspend fun toggleBookmark(token: String, problemId: Long, isCurrentlyBookmarked: Boolean): Boolean {
        val response = if (isCurrentlyBookmarked) {
            apiService.deleteBookmark("Bearer $token", problemId)
        } else {
            apiService.addBookmark("Bearer $token", problemId)
        }

        if (!response.isSuccessful) {
            throw Exception("북마크 변경 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        return body.result?.bookmarked ?: !isCurrentlyBookmarked
    }

    /**
     * 코드 실행 요청
     * 실행 결과는 바로 오지 않고 runToken만 반환됨
     */
    suspend fun runCode(
        token: String,
        problemId: Long,
        code: String,
        language: String
    ): String {
        val response = apiService.runCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language,
            request = RunRequestDto(
                sourceCode = code,
                timeLimit = 1.0,
                memoryLimit = 128000000
            )
        )

        if (!response.isSuccessful) {
            throw Exception("코드 실행 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val runToken = body.result
            ?.firstOrNull()
            ?.token
            ?: throw Exception("실행 토큰이 없습니다.")

        return runToken
    }

    /**
     * 실행 결과 조회
     * GET /api/v1/problems/runs/{token}/results
     */
    suspend fun getRunResult(
        token: String,
        runToken: String
    ): RunResult {
        val response = apiService.getRunResult(
            token = "Bearer $token",
            runToken = runToken
        )

        if (!response.isSuccessful) {
            throw Exception("실행 결과 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val result = body.result ?: throw Exception("실행 결과가 없습니다.")

        val isPassed = result.statusId == 3
        val outputText = result.output?.takeIf { it.isNotBlank() } ?: "출력 없음"

        val terminalLines = buildList {
            add("\$ Running test cases...")
            add("상태: ${result.status} (${result.statusId})")

            if (!result.input.isNullOrBlank()) {
                add("입력: ${result.input}")
            }

            add("출력: $outputText")

            if (isPassed) {
                add("실행 완료")
            } else {
                add("실행 실패 또는 미완료")
            }
        }

        return RunResult(
            passed = isPassed,
            runtimeMs = null,
            errorMessage = if (isPassed) null else result.status,
            terminalLines = terminalLines
        )
    }

    /**
     * 코드 제출 요청
     * 채점 결과 조회에 필요한 historyId + submissionId 반환
     */
    suspend fun submitCode(
        token: String,
        problemId: Long,
        code: String,
        language: String
    ): SubmitInfo {
        val response = apiService.submitCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language,
            request = SubmitRequestDto(
                sourceCode = code,
                timeLimit = 1.0,
                memoryLimit = 128000000
            )
        )

        if (!response.isSuccessful) {
            throw Exception("코드 제출 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val result = body.result ?: throw Exception("제출 결과가 없습니다.")

        val historyId = result.historyId
            ?: throw Exception("historyId가 없습니다.")

        val submissionId = result.submissionId
            ?: throw Exception("submissionId가 없습니다.")

        return SubmitInfo(
            historyId = historyId,
            submissionId = submissionId
        )
    }

    /**
     * 모범 답안 조회
     */
    suspend fun loadSolution(
        token: String,
        problemId: Long,
        language: String
    ): SolutionDetail {
        val response = apiService.getProblemSolution("Bearer $token", problemId, language)

        if (!response.isSuccessful) {
            throw Exception("모범 답안 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val result = body.result ?: throw Exception("모범 답안 데이터가 없습니다.")

        return SolutionDetail(
            code = result.solutionCode,
            explanation = result.lineSolution + "\n\n" + result.solutionText
        )
    }

    /**
     * 채점 결과 조회 (폴링용)
     * GET /api/v1/problems/submissions/{historyId}/results
     */
    suspend fun getSubmissionResult(
        token: String,
        historyId: Long,
        submissionId: String
    ): Pair<Boolean, String> {
        val response = apiService.getSubmissionResult(
            token = "Bearer $token",
            historyId = historyId,
            submissionId = submissionId
        )

        if (!response.isSuccessful) {
            throw Exception("채점 결과 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val result = body.result ?: throw Exception("결과 데이터가 없습니다.")

        val status = result.status
        val isCorrect = status == "ACCEPTED"

        return Pair(isCorrect, status)
    }

    /**
     * 문제별 제출 기록 조회
     */
    suspend fun loadSubmissionHistory(
        token: String,
        problemId: Long
    ): List<com.example.fe.feature.solver.model.SubmissionRecord> {

        val response = apiService.getSubmissionHistories(
            token = "Bearer $token",
            problemId = problemId
        )

        if (!response.isSuccessful) {
            throw Exception("제출 기록 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        val result = body.result ?: emptyList()

        return result.map {
            val isCorrect = it.status == "ACCEPTED"

            val resultText = when (it.status) {
                "ACCEPTED" -> "정답"
                "WRONG_ANSWER" -> "오답"
                "COMPILATION_ERROR" -> "컴파일 에러"
                "RUNTIME_ERROR" -> "런타임 에러"
                else -> it.status
            }

            com.example.fe.feature.solver.model.SubmissionRecord(
                date = it.createdAt,
                language = it.language,
                result = resultText,
                isCorrect = isCorrect
            )
        }
    }

    /**
     * 임시 코드 저장 (Draft)
     */
    suspend fun saveDraftCode(
        token: String,
        problemId: Long,
        language: String,
        code: String
    ) {
        val response = apiService.saveTempCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language,
            request = com.example.fe.data.dto.TempSaveRequestDto(sourceCode = code)
        )

        if (!response.isSuccessful) {
            throw Exception("임시 저장 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }
    }

    /**
     * 임시 코드 조회 (Draft)
     */
    suspend fun loadDraftCode(
        token: String,
        problemId: Long,
        language: String
    ): String? {
        val response = apiService.getTempCode(
            token = "Bearer $token",
            problemId = problemId,
            language = language
        )

        if (!response.isSuccessful) return null

        val body = response.body() ?: return null
        if (!body.isSuccess) return null

        return body.result?.sourceCode
    }
}