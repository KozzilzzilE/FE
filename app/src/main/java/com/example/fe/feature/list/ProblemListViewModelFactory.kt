package com.example.fe.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fe.feature.list.data.ProblemRepository

class ProblemListViewModelFactory(
    private val repository: ProblemRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProblemListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProblemListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
