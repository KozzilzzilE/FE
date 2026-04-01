package com.example.fe.feature.list.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.ProblemListResponse
import retrofit2.Response

class ProblemRepository(private val apiService: ApiService) {

    suspend fun getTopicProblems(token: String, topicId: Long): Response<ProblemListResponse> {
        // [MOCK] Interceptor를 사용하므로 더 이상 내부 Mock 소스 코드는 필요하지 않습니다.
        // 모든 요청은 ApiService를 통해 나가고, MockInterceptor가 가로챕니다.
        return apiService.getTopicProblems("Bearer $token", topicId)
    }
}
