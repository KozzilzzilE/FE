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

/**
 * [사용자 메인 언어 변경 API]
 * PATCH /api/v1/users/me/languages
 */
data class UpdateLanguageRequest(
    @SerializedName("language") val language: String
)

data class UpdateLanguageResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: UpdateLanguageResult?
)

data class UpdateLanguageResult(
    @SerializedName("userId") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)

/**
 * [사용자 이름 변경 API]
 * PATCH /api/v1/users/me/names
 */
data class UpdateNicknameRequest(
    @SerializedName("nickname") val nickname: String
)

data class UpdateNicknameResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: UpdateNicknameResult?
)

data class UpdateNicknameResult(
    @SerializedName("userId") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)