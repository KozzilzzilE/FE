package com.example.fe.feature.list.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.data.dto.ProblemListResponse
import retrofit2.Response

class ProblemRepository(private val apiService: ApiService) {

    suspend fun getTopicProblems(token: String, topicId: Long): Response<ProblemListResponse> {
        return apiService.getTopicProblems("Bearer $token", topicId)
    }
}
