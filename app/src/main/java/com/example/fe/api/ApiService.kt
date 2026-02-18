package com.example.fe.api

import com.example.fe.data.dto.UserRequest
import com.example.fe.data.dto.UserResponse
import com.example.fe.data.Problem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // 회원 정보 등록
    @POST("api/v1/auths/signup")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    // 문제 목록 가져오기
    @GET("api/problems")
    suspend fun getProblems(): Response<List<Problem>>
}
