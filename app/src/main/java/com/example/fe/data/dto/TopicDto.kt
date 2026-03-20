package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [알고리즘 주제 목록 API]
 * GET /api/v1/topics
 */
data class TopicResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("count") val count: Int,
    @SerializedName("result") val result: List<TopicResult>?
)

data class TopicResult(
    @SerializedName("topicId") val topicId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("displayName") val displayName: String
)
