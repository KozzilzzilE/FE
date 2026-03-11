package com.example.fe.feature.home.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.feature.home.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(private val apiService: ApiService) {

    suspend fun getHomeData(): HomeUiState {
        return withContext(Dispatchers.IO) {
            try {
                // apiService (RetrofitClient)를 통한 데이터 요청
                val response = apiService.getHomeData()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        val result = body.result
                        if (result != null) {
                            return@withContext HomeUiState.Success(
                                name = result.name,
                                languageName = result.languageName
                            )
                        } else {
                            Log.e("HomeRepository", "홈 화면 파싱 오류: result가 null")
                            return@withContext HomeUiState.Error("데이터가 없습니다.")
                        }
                    } else {
                        Log.e("HomeRepository", "서버 응답 오류 (isSuccess=false): ${body?.message}")
                        return@withContext HomeUiState.Error(body?.message ?: "응답 오류 발생")
                    }
                } else {
                    Log.e("HomeRepository", "HTTP 연결 실패: 코드 ${response.code()}")
                    return@withContext HomeUiState.Error("서버 연결에 실패했습니다.")
                }
            } catch (e: Exception) {
                Log.e("HomeRepository", "API Exception", e)
                return@withContext HomeUiState.Error("네트워크 예외가 발생했습니다.")
            }
        }
    }
}
