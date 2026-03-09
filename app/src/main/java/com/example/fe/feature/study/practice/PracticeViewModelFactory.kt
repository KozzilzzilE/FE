package com.example.fe.feature.study.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fe.feature.study.practice.data.PracticeRepository

class PracticeViewModelFactory(
    private val repository: PracticeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // PracticeViewModel 생성 요청일 때만 repository를 넣어서 생성
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PracticeViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}