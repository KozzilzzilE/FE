package com.example.fe.feature.practice.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.QuizItemDto

class PracticeRepository(
    private val apiService: ApiService
) {

    // 응용학습 문제 조회
    suspend fun getQuizzes(
        token: String,
        topicId: Long,
        language: String = "JAVA"
    ): List<QuizItemDto> {
        val response = apiService.getPracticeQuizzes("Bearer $token", topicId, language)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

            if (body.isSuccess) {
                val result = body.result ?: return emptyList()
                return result.appliedExercises
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("응용학습 조회 실패: ${response.code()}")
        }
    }

    // 응용 문제 완료 처리
    suspend fun completeQuiz(
        token: String,
        exerciseId: Long
    ) {
        val response = apiService.completePractice("Bearer $token", exerciseId)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

            if (!body.isSuccess) {
                throw Exception(body.message)
            }
        } else {
            throw Exception("응용학습 완료 처리 실패: ${response.code()}")
        }
    }
}