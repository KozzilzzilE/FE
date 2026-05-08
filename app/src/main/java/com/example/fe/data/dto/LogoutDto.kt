package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [로그아웃 API]
 * POST /api/v1/auths/logout
 */

data class LogoutResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)
