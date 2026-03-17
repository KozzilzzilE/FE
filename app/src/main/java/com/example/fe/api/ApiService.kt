package com.example.fe.api

import com.example.fe.data.Problem
import com.example.fe.data.dto.SignUpRequest
import com.example.fe.data.dto.SignUpResponse
import com.example.fe.data.dto.LoginRequest
import com.example.fe.data.dto.LoginResponse
import com.example.fe.data.dto.HomeResponse
import com.example.fe.data.dto.PracticeCompletionResponseDto
import com.example.fe.data.dto.PracticeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

    // --- [응용학습 Practice] ---

    // 응용학습 문제 조회
    @GET("/api/v1/learnings/{topicId}/applications")
    suspend fun getPracticeQuizzes(
        @retrofit2.http.Path("topicId") topicId: Long,
        @retrofit2.http.Query("language") language: String
    ): Response<PracticeResponseDto>

    // 응용학습 문제 완료 처리
    @POST("/api/v1/learnings/applications/completions/{exerciseId}")
    suspend fun completePractice(
        @retrofit2.http.Path("exerciseId") exerciseId: Long
    ): PracticeCompletionResponseDto
}
