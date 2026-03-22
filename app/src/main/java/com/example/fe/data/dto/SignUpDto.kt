package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [회원가입 API]
 * POST /api/v1/auths/signup
 */
data class SignUpRequest(
    @SerializedName("firebaseToken") val firebaseToken: String,
    @SerializedName("resistered") val registered: Boolean,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)

data class SignUpResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpResult?
)

data class SignUpResult(
    @SerializedName("userId") val userId: Long,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)
