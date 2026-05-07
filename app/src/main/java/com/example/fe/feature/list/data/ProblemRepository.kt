package com.example.fe.feature.list.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.ProblemListResponse
import com.example.fe.data.dto.AllProblemListResponse
import retrofit2.Response

class ProblemRepository(private val apiService: ApiService) {

    suspend fun getTopicProblems(token: String, topicId: Long): Response<ProblemListResponse> {
        // [MOCK] Interceptor를 사용하므로 더 이상 내부 Mock 소스 코드는 필요하지 않습니다.
        // 모든 요청은 ApiService를 통해 나가고, MockInterceptor가 가로챕니다.
        return apiService.getTopicProblems("Bearer $token", topicId)
    }

    suspend fun getAllProblems(token: String, page: Int, difficulty: String? = null): Response<AllProblemListResponse> {
        return apiService.getAllProblems("Bearer $token", page, size = 20, difficulty = difficulty)
    }

    suspend fun toggleBookmark(token: String, problemId: Long, isCurrentlyBookmarked: Boolean): Boolean {
        val response = if (isCurrentlyBookmarked) {
            apiService.deleteBookmark("Bearer $token", problemId)
        } else {
            apiService.addBookmark("Bearer $token", problemId)
        }

        if (!response.isSuccessful) {
            throw Exception("북마크 변경 실패: ${response.code()}")
        }

        val body = response.body() ?: throw Exception("응답 데이터가 없습니다.")
        if (!body.isSuccess) {
            throw Exception(body.message)
        }

        return body.result?.bookmarked ?: !isCurrentlyBookmarked
    }
}
