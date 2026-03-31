package com.example.fe.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.fe.common.TokenManager

object RetrofitClient { // 싱글톤 객체로 생성
    private const val BASE_URL = "http://223.194.135.59:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply { // 로그 기록기 생성
        level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 로그 기록
    }

    // 서버에서 발급한 전용 토큰(AccessToken)을 헤더에 추가하는 인터셉터
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        
        // 로컬 금고에서 서버 토큰을 꺼냅니다.
        val serverToken = TokenManager.getAccessToken()
        
        // 토큰이 존재할 경우에만(로그인 된 상태) 요청 헤더에 삽입
        if (!serverToken.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $serverToken")
        }
        
        chain.proceed(requestBuilder.build())
    }

    // API 요청 성공/실패 시 일관된 포맷으로 안드로이드 로그(Logcat)를 띄워주는 커스텀 인터셉터
    private val customLoggingInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request()
        val t1 = System.nanoTime()
        
        val response = chain.proceed(request)
        
        val t2 = System.nanoTime()
        val timeStr = String.format("%.1f", (t2 - t1) / 1e6)

        if (response.isSuccessful) {
            // body 스트림을 소모하지 않고 안전하게 확인 (최대 2KB)
            val bodyString = response.peekBody(2048).string()
            android.util.Log.d(
                "API_SUCCESS",
                "✅ [${request.method}] ${request.url} (${timeStr}ms)\n응답 코드: ${response.code}\n결과: $bodyString"
            )
        } else {
            android.util.Log.e(
                "API_ERROR",
                "❌ [${request.method}] ${request.url} (${timeStr}ms)\n응답 코드: ${response.code}\n에러 메시지: ${response.message}"
            )
        }
        response
    }

    private val okHttpClient = OkHttpClient.Builder() // OkHttp 클라이언트 생성
        .addInterceptor(loggingInterceptor) // 기존 상세 로그 기록기
        .addInterceptor(customLoggingInterceptor) // ★ 추가: 성공/실패 한눈에 요약하는 커스텀 로깅
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
