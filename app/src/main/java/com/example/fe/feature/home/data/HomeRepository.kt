package com.example.fe.feature.home.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.feature.home.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeRepository(private val apiService: ApiService) {

    suspend fun getHomeData(): HomeUiState {
        return withContext(Dispatchers.IO) {
            try {
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    return@withContext HomeUiState.Error("로그인 토큰이 없습니다.")
                }
                
                val response = apiService.getHomeData("Bearer $token")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("HomeRepository", "메인 데이터 로드 성공: code=${body?.code}, message=${body?.message}")
                    if (body != null && body.isSuccess) {
                        val result = body.result
                        if (result != null) {
                            val contributionData = result.totalSolvedDetails
                                .mapNotNull { detail ->
                                    runCatching { LocalDate.parse(detail.date) to detail.count }.getOrNull()
                                }
                                .toMap()

                            // 연속 학습 일수(스트릭) 계산 로직
                            val today = LocalDate.now()
                            var streakCount = 0
                            var checkDate = today
                            if ((contributionData[today] ?: 0) == 0) {
                                checkDate = today.minusDays(1)
                            }
                            while ((contributionData[checkDate] ?: 0) > 0) {
                                streakCount++
                                checkDate = checkDate.minusDays(1)
                            }

                            // 총 학습 일수 계산 로직
                            val totalStudyDays = contributionData.count { it.value > 0 }

                            return@withContext HomeUiState.Success(
                                name = result.name,
                                languageName = result.languageName,
                                contributionData = contributionData,
                                thisMonthSolvedCount = result.thisMonthSolvedCount,
                                streakDays = streakCount,
                                totalStudyDays = totalStudyDays
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
