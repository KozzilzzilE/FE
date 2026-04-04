package com.example.fe.feature.profile.data

import com.example.fe.api.ApiService
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.MyPageResponseDto

class ProfileRepository(
    private val apiService: ApiService
) {
    suspend fun getMyPageInfo(): MyPageResponseDto {
        val token = TokenManager.getAccessToken()
            ?: throw IllegalStateException("토큰이 없습니다.")

        val response = apiService.getMyPageInfo("Bearer $token")

        if (response.isSuccessful) {
            return response.body()
                ?: throw IllegalStateException("응답 본문이 비어 있습니다.")
        } else {
            throw IllegalStateException("마이페이지 정보 조회 실패")
        }
    }
}