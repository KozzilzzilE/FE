package com.example.fe.feature.study.practice.api

import com.example.fe.feature.study.practice.dto.PracticeCompletionResponseDto
import com.example.fe.feature.study.practice.dto.PracticeResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PracticeApi {

    // 응용학습 문제 조회
    // topicId : 알고리즘 ID
    // language : 선택 언어 (JAVA, PYTHON 등)
    @GET("/api/v1/learnings/{topicId}/applications")
    suspend fun getPracticeQuizzes(
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): PracticeResponseDto


    // 응용학습 문제 완료 처리
    // 문제를 맞췄을 때 호출
    @POST("/api/v1/learnings/applications/completions/{exerciseId}")
    suspend fun completePractice(
        @Path("exerciseId") exerciseId: Long
    ): PracticeCompletionResponseDto
}