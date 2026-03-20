package com.example.fe.feature.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.auth.data.AuthRepository
import com.example.fe.feature.auth.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.login(email, pass)
        }
    }

    fun signUp(name: String, email: String, pass: String, language: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signUp(name, email, pass, language)
        }
    }

    fun signInWithGoogleSignUp(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGoogleSignUp(idToken)
        }
    }

    fun signInWithGoogleLogin(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGoogleLogin(idToken)
        }
    }

    fun signInWithGithubSignUp(activity: Activity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGithubSignUp(activity)
        }
    }

    fun signInWithGithubLogin(activity: Activity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGithubLogin(activity)
        }
    }

    fun completeSocialSignUp(name: String, email: String, language: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.completeSocialSignUp(name, email, language)
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }
}
