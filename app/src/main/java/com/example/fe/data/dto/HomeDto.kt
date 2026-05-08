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
    @SerializedName("nickname") val name: String,
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("languageName") val languageName: String,
    @SerializedName("totalSolvedDetails") val totalSolvedDetails: List<SolvedDetail> = emptyList(),
    @SerializedName("thisMonthSolvedCount") val thisMonthSolvedCount: Int = 0
)

data class SolvedDetail(
    @SerializedName("date") val date: String,
    @SerializedName("count") val count: Int
)
