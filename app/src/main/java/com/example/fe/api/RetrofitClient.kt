package com.example.fe.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.fe.common.TokenManager

object RetrofitClient { // 싱글톤 객체로 생성
    private const val BASE_URL = com.example.fe.BuildConfig.BASE_URL

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (com.example.fe.BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    // 서버에서 발급한 전용 토큰(AccessToken)을 헤더에 추가하는 인터셉터
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        
        // 로컬 금고에서 서버 토큰을 꺼냅니다.
        val serverToken = TokenManager.getAccessToken()
        
        // 토큰이 존재할 경우에만(로그인 된 상태) 요청 헤더에 삽입
        if (!serverToken.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $serverToken")
        }
        
        chain.proceed(requestBuilder.build())
    }

    private val customLoggingInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request()
        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        if (com.example.fe.BuildConfig.DEBUG) {
            val timeStr = String.format("%.1f", (t2 - t1) / 1e6)
            if (response.isSuccessful) {
                val bodyString = response.peekBody(2048).string()
                android.util.Log.d(
                    "API_SUCCESS",
                    "✅ [${request.method}] ${request.url} (${timeStr}ms)\n${response.code}\n$bodyString"
                )
            } else {
                android.util.Log.e(
                    "API_ERROR",
                    "❌ [${request.method}] ${request.url} (${timeStr}ms)\n${response.code} ${response.message}"
                )
            }
        }

        // 토큰 만료(401) 감지 시 로컬 토큰 지우고 알림 발생
        if (response.code == 401) {
            TokenManager.clearAccessToken()
            TokenManager.emitTokenExpired()
        }

        response
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        addInterceptor(customLoggingInterceptor)
        addInterceptor(authInterceptor)
    }.build()

    val instance: ApiService by lazy { // Retrofit 인스턴스 생성, 사용할때 만들어짐
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON을 Kotlin 객체로 변환하기 위한 라이브러리
            .client(okHttpClient)
            .build()
        retrofit.create(ApiService::class.java) // ApiService 인터페이스 구현체 생성
    }
}
