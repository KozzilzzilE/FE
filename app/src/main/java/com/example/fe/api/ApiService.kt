package com.example.fe.api

import com.example.fe.data.Problem
import com.example.fe.data.dto.SignUpRequest
import com.example.fe.data.dto.SignUpResponse
import com.example.fe.data.dto.LoginRequest
import com.example.fe.data.dto.LoginResponse
import com.example.fe.data.dto.HomeResponse

import com.example.fe.data.dto.TopicResponse
import com.example.fe.data.dto.ConceptResponse
import com.example.fe.data.dto.PracticeResponseDto
import com.example.fe.data.dto.PracticeCompletionResponseDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // 회원가입 : 회원 정보 서버에 등록 (소셜 연동 포함)
    @POST("api/v1/auths/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    // 로그인 : 서버로 로그인 요청 (Firebase 토큰 인증용)
    @POST("api/v1/auths/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // 문제 목록 가져오기
    @GET("api/problems")
    suspend fun getProblems(): Response<List<Problem>>

    // 메인화면 : 홈 화면 데이터 가져오기
    @GET("api/v1/users/main")
    suspend fun getHomeData(): Response<HomeResponse>


    // 알고리즘 주제 목록 조회
    @GET("api/v1/topics")
    suspend fun getTopics(): Response<TopicResponse>

    // 개념 학습(notions) 조회 (특정 알고리즘 주제 및 언어)
    @GET("api/v1/learnings/{topicId}/notions")
    suspend fun getNotions(
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): Response<ConceptResponse>

    // 개념 학습 완료 처리
    @POST("api/v1/learnings/notions/completions/{notionId}")
    suspend fun postNotionCompletion(
        @Path("notionId") notionId: Long
    ): Response<com.example.fe.data.dto.ConceptCompletionResponse>


    // --- [응용학습 Practice] ---

    // 응용학습 문제 조회
    @GET("/api/v1/learnings/{topicId}/applications")
    suspend fun getPracticeQuizzes(
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): Response<PracticeResponseDto>

    // 응용학습 문제 완료 처리
    @POST("/api/v1/learnings/applications/completions/{exerciseId}")
    suspend fun completePractice(
        @Path("exerciseId") exerciseId: Long
    ): Response<PracticeCompletionResponseDto>
}
