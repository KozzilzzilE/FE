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
    @SerializedName("difficultyDisplayName") val difficultyDisplayName: String
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
    @SerializedName("isCompleted") val isCompleted: Boolean
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
