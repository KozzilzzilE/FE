package com.example.fe.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.api.RetrofitClient
import com.example.fe.data.dto.HomeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val name: String, val languageName: String) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // 화면에 처음 진입(ViewModel 생성)할 때 데이터를 바로 가져옵니다.
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // RetrofitClient의 instance를 사용하여 데이터 비동기 요청
                val response = RetrofitClient.instance.getHomeData()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        val result = body.result
                        if (result != null) {
                            _uiState.value = HomeUiState.Success(
                                name = result.name,
                                languageName = result.languageName
                            )
                        } else {
                            Log.e("HomeViewModel", "홈 화면 파싱 오류: result가 null")
                            _uiState.value = HomeUiState.Error("")
                        }
                    } else {
                        Log.e("HomeViewModel", "서버 응답 오류 (isSuccess=false): ${body?.message}")
                        _uiState.value = HomeUiState.Error("")
                    }
                } else {
                    Log.e("HomeViewModel", "HTTP 연결 실패: 코드 ${response.code()}")
                    _uiState.value = HomeUiState.Error("")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "API Exception", e)
                _uiState.value = HomeUiState.Error("")
            }
        }
    }
}
