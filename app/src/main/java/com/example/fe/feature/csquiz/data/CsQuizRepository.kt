package com.example.fe.feature.csquiz.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.feature.csquiz.model.CsQuizQuestion

class CsQuizRepository(private val apiService: ApiService) {

    suspend fun getQuizQuestions(token: String): List<CsQuizQuestion>? {
        return try {
            val response = apiService.getRandomCsProblems("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.result?.map { result ->
                    CsQuizQuestion(
                        id = result.csProblemId,
                        question = result.question,
                        answer = result.answer,
                        explanation = result.explanation
                    )
                }
            } else {
                Log.e("CsQuizRepository", "API 에러: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("CsQuizRepository", "네트워크 예외: ${e.message}", e)
            null
        }
    }
}
