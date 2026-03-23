package com.example.fe.feature.list.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.ProblemListResponse
import com.example.fe.data.dto.ProblemListResult
import com.example.fe.data.dto.ProblemResult
import retrofit2.Response

class ProblemRepository(private val apiService: ApiService) {

    companion object {
        private const val USE_MOCK = true
    }

    suspend fun getTopicProblems(token: String, topicId: Long): Response<ProblemListResponse> {
        if (USE_MOCK) {
            val problems = listOf(
                ProblemResult(101L, "해시를 이용한 완주하지 못한 선수", "EASY", "쉬움"),
                ProblemResult(102L, "전화번호 목록 (Trie/Hash)", "MEDIUM", "보통"),
                ProblemResult(103L, "위장 (Combination)", "MEDIUM", "보통"),
                ProblemResult(104L, "베스트앨범", "HARD", "어려움")
            )
            val mockResult = ProblemListResult(topicId, problems.size, problems)
            val mockResponse = ProblemListResponse(true, "COMMON200", "성공 (Mock)", mockResult)
            return Response.success(mockResponse)
        }
        return apiService.getTopicProblems("Bearer $token", topicId)
    }
}
