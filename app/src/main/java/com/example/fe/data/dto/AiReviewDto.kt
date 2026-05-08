package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [AI 코드 리뷰 API]
 * GET/POST /api/v1/histories/{historyId}/ai-review
 */
data class AiCodeReviewResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AiCodeReviewResultDto?
)

data class AiCodeReviewResultDto(
    @SerializedName("aiStatus") val aiStatus: String,
    @SerializedName("aiReview") val aiReview: String?,
    @SerializedName("aiImprovement") val aiImprovement: String?
)
