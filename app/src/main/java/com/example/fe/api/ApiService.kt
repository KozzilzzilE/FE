package com.example.fe.api

import com.example.fe.data.dto.UserRequest
import com.example.fe.data.dto.UserResponse
import com.example.fe.data.Problem
import com.example.fe.data.dto.LoginRequest
import com.example.fe.data.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // 회원 정보 서버에 등록 (소셜 연동 포함)
    @POST("api/v1/auths/signup")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    // 서버로 로그인 요청 (Firebase 토큰 인증용)
    @POST("api/v1/auths/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // 문제 목록 가져오기
    @GET("api/problems")
    suspend fun getProblems(): Response<List<Problem>>
}
