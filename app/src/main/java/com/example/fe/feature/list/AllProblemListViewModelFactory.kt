package com.example.fe.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fe.feature.list.data.ProblemRepository

/**
 * AllProblemListViewModel 생성을 위한 Factory
 */
class AllProblemListViewModelFactory(private val repository: ProblemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllProblemListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllProblemListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
