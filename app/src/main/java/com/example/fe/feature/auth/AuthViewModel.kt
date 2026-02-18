package com.example.fe.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.data.dto.UserRequest
import com.example.fe.api.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() { // 인증 관련 로직을 담당
    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Firebase 인증 인스턴스

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle) // 인증 상태 관리
    val authState = _authState.asStateFlow()

    init {
        // 앱이 켜질 때 이미 로그인된 유저가 있는지 확인 (자동 로그인)
//        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            // 이미 로그인된 상태라면 바로 성공 상태로 전환
            _authState.value = AuthState.Success
        }
    }

    // 로그인
    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    // 회원가입
    fun signUp(name: String, email: String, pass: String, language: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Firebase 가입 성공 시, 서버에 추가 정보 등록
                    viewModelScope.launch {
                        try {
                            val response = RetrofitClient.instance.signUp(
                                UserRequest(
                                    email = email,
                                    nickname = name,
                                    language = language
                                )
                            )
                            if (response.isSuccessful && response.body()?.isSuccess == true) {
                                auth.signOut() 
                                _authState.value = AuthState.SignedUp(response.body()?.message ?: "회원가입 완료")
                            } else {
                                _authState.value = AuthState.Error(response.body()?.message ?: "서버 등록 실패")
                            }
                            } catch (e: Exception) {
                                _authState.value = AuthState.Error("네트워크 오류: ${e.message}")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                    }
                }
    }

    // 로그아웃 (필요시 호출)
    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }
}

// 인증 상태 (성공, 실패, 로딩 등)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class SignedUp(val message: String) : AuthState() // 회원가입 완료 상태 (메세지 포함)
    data class Error(val message: String) : AuthState()
}
