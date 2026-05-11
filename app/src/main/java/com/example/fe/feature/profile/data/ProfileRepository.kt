package com.example.fe.feature.profile.data

import com.example.fe.api.ApiService
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.LanguageResult
import com.example.fe.data.dto.MyPageResponse
import com.example.fe.data.dto.UpdateLanguageRequest
import com.example.fe.data.dto.UpdateLanguageResponse
import com.example.fe.data.dto.UpdateNicknameRequest
import com.example.fe.data.dto.UpdateNicknameResponse
import com.example.fe.data.dto.UpdateProfileRequest
import com.example.fe.data.dto.UpdateProfileResponse

class ProfileRepository(
    private val apiService: ApiService
) {
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

    suspend fun updateLanguage(language: String): UpdateLanguageResponse {
        val token = TokenManager.getAccessToken()
            ?: throw Exception("로그인이 필요합니다.")

        val response = apiService.updateLanguage(
            "Bearer $token",
            UpdateLanguageRequest(language)
        )

        if (!response.isSuccessful) {
            throw Exception("언어 변경 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

        if (!body.isSuccess || body.result == null) {
            throw Exception(body.message)
        }

        return body
    }

    suspend fun updateNickname(nickname: String): UpdateNicknameResponse {
        val token = TokenManager.getAccessToken()
            ?: throw Exception("로그인이 필요합니다.")

        val response = apiService.updateNickname(
            "Bearer $token",
            UpdateNicknameRequest(nickname)
        )

        if (!response.isSuccessful) {
            throw Exception("이름 변경 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

        if (!body.isSuccess || body.result == null) {
            throw Exception(body.message)
        }

        return body
    }

    suspend fun updateProfile(nickname: String, profileId: Int?): UpdateProfileResponse {
        val token = TokenManager.getAccessToken()
            ?: throw Exception("로그인이 필요합니다.")

        val response = apiService.updateProfile(
            "Bearer $token",
            UpdateProfileRequest(nickname, profileId)
        )

        if (!response.isSuccessful) {
            throw Exception("프로필 변경 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

        if (!body.isSuccess || body.result == null) {
            throw Exception(body.message)
        }

        return body
    }
}