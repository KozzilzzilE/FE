package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [마이페이지 조회 API]
 * GET /api/v1/users/main
 */

data class MyPageResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MyPageResult?
)

data class MyPageResult(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("languageName") val languageName: String
)