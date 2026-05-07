package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [문제별 임시 코드 저장/조회 API]
 * PUT /api/v1/problems/{problemId}/temp-storages (저장/업데이트)
 * GET /api/v1/problems/{problemId}/temp-storages (조회)
 */
data class TempSaveRequestDto(
    @SerializedName("sourceCode") val sourceCode: String
)

data class TempSaveResponseDto(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: TempSaveResultDto?
)

data class TempSaveResultDto(
    @SerializedName("userCodeId") val userCodeId: Long,
    @SerializedName("sourceCode") val sourceCode: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("updatedAt") val updatedAt: String
)
