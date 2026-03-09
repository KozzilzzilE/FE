package com.example.fe.feature.study.practice.data

import com.example.fe.feature.study.practice.api.PracticeApi
import com.example.fe.feature.study.practice.dto.QuizItemDto

class PracticeRepository(
    private val practiceApi: PracticeApi
) {

    suspend fun getQuizzes(
        topicId: Long,
        language: String
    ): List<QuizItemDto> {

        val response = practiceApi.getQuizzes(topicId, language)

        if (response.isSuccess) {
            return response.result
        } else {
            throw Exception(response.message)
        }
    }
}