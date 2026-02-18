package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * 서버에 유저 정보를 등록하기 위한 요청 객체
 */
data class UserRequest( // firebase 토큰은 헤더로
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)

/**
 * 서버 응답 처리 (필요시 확장)
 */
data class UserResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: UserResult?
)

data class UserResult(
    @SerializedName("userId") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("language") val language: String
)
