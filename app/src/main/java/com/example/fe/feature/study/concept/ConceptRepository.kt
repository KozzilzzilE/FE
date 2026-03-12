package com.example.fe.feature.study.concept

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.data.dto.ConceptResponse

class ConceptRepository(private val apiService: ApiService) {

    suspend fun getConcepts(topicId: Long, language: String): ConceptResponse? {
        return try {
            val response = apiService.getNotions(topicId = topicId, language = language)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ConceptRepository", "개념 학습 API 호출 에러: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "개념 학습 API 네트워크 예외: ${e.message}", e)
            null
        }
    }

    suspend fun completeConcept(notionId: Long): Boolean {
        return try {
            val response = apiService.postNotionCompletion(notionId)
            if (response.isSuccessful) {
                response.body()?.isSuccess == true
            } else {
                Log.e("ConceptRepository", "개념 학습 완료 처리 에러: ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "개념 학습 완료 네트워크 예외: ${e.message}", e)
            false
        }
    }
}
