package com.example.fe.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient { // 싱글톤 객체로 생성
    private const val BASE_URL = "http://10.0.2.2:8080/" 

    private val loggingInterceptor = HttpLoggingInterceptor().apply { // 로그 기록기 생성
        level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 로그 기록
    }

    // Firebase 인증 토큰을 헤더에 추가하는 인터셉터
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val requestBuilder = chain.request().newBuilder()
        
        if (user != null) {
            try {
                // 토큰을 동기적으로 가져옴
                val task = user.getIdToken(false)
                val tokenResult = com.google.android.gms.tasks.Tasks.await(task)
                val token = tokenResult.token
                
                if (token != null) { // 토큰이 있으면 헤더에 추가
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder() // OkHttp 클라이언트 생성
        .addInterceptor(loggingInterceptor) // 로그 기록기 추가
        .addInterceptor(authInterceptor)    // 인증 인터셉터 추가
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
