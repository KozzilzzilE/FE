package com.example.fe.feature.study.practice.api

import com.example.fe.feature.study.practice.dto.QuizResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PracticeApi {

    @GET("/api/v1/topics/{topicId}/quizzes")
    suspend fun getQuizzes(
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): QuizResponseDto
}