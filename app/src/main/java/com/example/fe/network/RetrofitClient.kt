package com.example.fe.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient { // 싱글톤 객체로 생성
    private const val BASE_URL = "http://10.0.2.2:8080/" 

    private val loggingInterceptor = HttpLoggingInterceptor().apply { // 로그 기록기 생성
        level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 로그 기록
    }

    private val okHttpClient = OkHttpClient.Builder() // OkHttp 클라이언트 생성
        .addInterceptor(loggingInterceptor) // 로그 기록기 추가
        .build()

    val instance: ApiService by lazy { // Retrofit 인스턴스 생성, 사용할때 만들어짐
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON을 Kotlin 객체로 변환하기 위한 라이브러리
            .client(okHttpClient)
            .build()
        retrofit.create(ApiService::class.java) // ApiService 인터페이스 구현체 생성
    }
}
