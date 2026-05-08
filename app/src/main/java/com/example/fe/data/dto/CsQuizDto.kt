package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [CS 퀴즈 API]
 * GET /api/v1/cs-problems/random
 */

data class CsQuizResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<CsQuizResult>?
)

data class CsQuizResult(
    @SerializedName("csProblemId") val csProblemId: Int,
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: Boolean,
    @SerializedName("explanation") val explanation: String
)
