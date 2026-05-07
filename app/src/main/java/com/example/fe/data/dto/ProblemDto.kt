package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [문제 목록 조회 API]
 * GET /api/v1/topics/{topicId}/problems
 */

data class ProblemListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProblemListResult?
)

data class ProblemListResult(
    @SerializedName("topicId") val topicId: Long,
    @SerializedName("count") val count: Int,
    @SerializedName("problems") val problems: List<ProblemResult>
)

data class ProblemResult(
    @SerializedName("problemId") val problemId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("difficultyDisplayName") val difficultyDisplayName: String,
    @SerializedName("bookmarkCount") val bookmarkCount: Int?,
    @SerializedName("isBookmark") val isBookmark: Boolean?,
    @SerializedName("isCompleted") val isCompleted: Boolean?
)

/**
 * [전체 문제 목록 조회 API]
 * GET /api/v1/problems
 */
data class AllProblemListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AllProblemListResult?
)

data class AllProblemListResult(
    @SerializedName("problemList") val problemList: List<ProblemResult>,
    @SerializedName("totalPage") val totalPage: Int = 1,
    @SerializedName("totalElements") val totalElements: Int = 0
)

/**
 * [문제 상세 조회 API]
 * GET /api/v1/problems/{problemId}
 */

data class ProblemDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProblemDetailResult?
)

data class ProblemDetailResult(
    @SerializedName("exerciseId") val exerciseId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("constraint") val constraint: String,
    @SerializedName("testCases") val testCases: List<TestCaseDto>,
    @SerializedName("isCompleted") val isCompleted: Boolean,
    @SerializedName("bookmarkCount") val bookmarkCount: Int?,
    @SerializedName("isBookmark") val isBookmark: Boolean?
)

data class TestCaseDto(
    @SerializedName("input") val input: String,
    @SerializedName("output") val output: String
)

/**
 * [문제 모범 답안 조회 API]
 * GET /api/v1/problems/{problemId}/solutions
 */

data class SolutionResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SolutionResult?
)

data class SolutionResult(
    @SerializedName("lineSolution") val lineSolution: String,
    @SerializedName("solutionText") val solutionText: String,
    @SerializedName("language") val language: String,
    @SerializedName("solutionCode") val solutionCode: String
)

/**
 * [문제 코드 실행 요청 API]
 * POST /api/v1/problems/{problemId}/runs
 */

data class RunRequestDto(
    @SerializedName("sourceCode") val sourceCode: String,
    @SerializedName("timeLimit") val timeLimit: Double,
    @SerializedName("memoryLimit") val memoryLimit: Int
)

data class RunResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<RunResultItem>?
)

data class RunResultItem(
    @SerializedName("token") val token: String
)

/**
 * [문제 코드 정답 요청 API]
 * POST /api/v1/problems/{problemId}/submissions
 */

data class SubmitRequestDto(
    @SerializedName("sourceCode") val sourceCode: String,
    @SerializedName("timeLimit") val timeLimit: Double,
    @SerializedName("memoryLimit") val memoryLimit: Int
)

data class SubmitResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SubmitResult?
)

data class SubmitResult(
    @SerializedName("historyId") val historyId: Long?,
    @SerializedName("submissionId") val submissionId: String?
)

/**
 * [문제 코드 실행 결과 조회 API]
 * GET /api/v1/problems/runs/{token}/results
 */
data class RunResultResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: RunResultDto?
)

data class RunResultDto(
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("input") val input: String?,
    @SerializedName("output") val output: String?
)

/**
 * [문제 코드 채점 결과 조회 API]
 * GET /api/v1/problems/submissions/{historyId}/results
 */

data class SubmissionResultResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SubmissionResultDto?
)

data class SubmissionResultDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)

/**
 * [문제별 사용자 제출 기록 조회 API]
 */

data class SubmissionHistoryResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<SubmissionHistoryItem>?
)

data class SubmissionHistoryItem(
    @SerializedName("sourceCode") val sourceCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("language") val language: String,
    @SerializedName("createdAt") val createdAt: String
)