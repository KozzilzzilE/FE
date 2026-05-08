package com.example.fe.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.home.data.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val name: String,
        val languageName: String,
        val contributionData: Map<LocalDate, Int> = emptyMap(),
        val thisMonthSolvedCount: Int = 0,
        val streakDays: Int = 0,
        val totalStudyDays: Int = 0
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // 화면에 처음 진입(ViewModel 생성)할 때 데이터를 바로 가져옵니다.
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            // 깔끔하게 분리된 Repository 호출
            _uiState.value = repository.getHomeData()
        }
    }
}
