package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

data class ProblemListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("topicId") val topicId: Long,
    @SerializedName("count") val count: Int,
    @SerializedName("result") val result: List<ProblemResult>?
)

data class ProblemResult(
    @SerializedName("problemId") val problemId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("difficultyDisplayName") val difficultyDisplayName: String
)
