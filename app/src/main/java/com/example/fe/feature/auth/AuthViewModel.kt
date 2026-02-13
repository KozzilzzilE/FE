package com.example.fe.feature.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() { // 인증 관련 로직을 담당
    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Firebase 인증 인스턴스

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle) // 인증 상태 관리
    val authState = _authState.asStateFlow() // 인증 상태 관리

    // 로그인
    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass) // Firebase 로그인
            .addOnCompleteListener { task -> // 로그인 결과 처리
                if (task.isSuccessful) { // 로그인 성공시 
                    _authState.value = AuthState.Success 
                } else { // 로그인 실패시
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed") 
                }
            }
    }

    // 회원가입
    fun signUp(email: String, pass: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }
}

// 인증 상태 (성공, 실패, 로딩 등)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
