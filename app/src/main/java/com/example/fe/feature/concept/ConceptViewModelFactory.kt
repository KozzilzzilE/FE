package com.example.fe.feature.concept

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fe.api.RetrofitClient
import com.example.fe.feature.concept.data.ConceptRepository

class ConceptViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConceptViewModel::class.java)) {
            val apiService = RetrofitClient.instance
            val repository = ConceptRepository(apiService)
            @Suppress("UNCHECKED_CAST")
            return ConceptViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
