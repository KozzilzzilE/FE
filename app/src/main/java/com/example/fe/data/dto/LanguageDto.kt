package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [언어 목록 조회 API]
 * GET /api/v1/languages/lists
 */
data class LanguageResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LanguageListResult?
)

data class LanguageListResult(
    @SerializedName("count") val count: Int,
    @SerializedName("languages") val languages: List<LanguageResult>
)

data class LanguageResult(
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("name") val name: String
)
