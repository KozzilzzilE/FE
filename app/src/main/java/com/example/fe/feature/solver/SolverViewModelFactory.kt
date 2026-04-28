package com.example.fe.feature.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fe.feature.solver.data.SolverDraftDataStore
import com.example.fe.feature.solver.data.SolverRepository

class SolverViewModelFactory(
    private val repository: SolverRepository,
    private val draftDataStore: SolverDraftDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolverViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolverViewModel(repository, draftDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}