package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

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
