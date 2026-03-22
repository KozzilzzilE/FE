package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [홈 화면 API]
 * GET /api/v1/users/main
 */

data class HomeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: HomeResult?
)

data class HomeResult(
    @SerializedName("name") val name: String,
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("languageName") val languageName: String
)
