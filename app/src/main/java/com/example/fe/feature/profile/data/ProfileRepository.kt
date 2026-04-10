package com.example.fe.feature.profile.data

import com.example.fe.api.ApiService
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.LanguageResult
import com.example.fe.data.dto.MyPageResponse

class ProfileRepository(
    private val apiService: ApiService
) {
    // 마이페이지 정보 조회
    suspend fun getMyPageInfo(): MyPageResponse {
        val token = TokenManager.getAccessToken()
            ?: throw Exception("로그인이 필요합니다.")

        val response = apiService.getMyPageInfo("Bearer $token")

        if (!response.isSuccessful) {
            throw Exception("마이페이지 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

        if (!body.isSuccess || body.result == null) {
            throw Exception(body.message)
        }

        return body
    }

    // 언어 목록 조회
    suspend fun getLanguageList(): List<LanguageResult> {
        val response = apiService.getLanguages()

        if (!response.isSuccessful) {
            throw Exception("언어 목록 조회 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

        if (!body.isSuccess || body.result == null) {
            throw Exception(body.message)
        }

        return body.result.languages
    }
}