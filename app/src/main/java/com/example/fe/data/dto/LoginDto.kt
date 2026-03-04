package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [로그인 API]
 * POST /api/v1/auths/login
 */
data class LoginRequest(
    @SerializedName("firebaseToken") val firebaseToken: String
)

data class LoginResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginResult?
)

data class LoginResult(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)
