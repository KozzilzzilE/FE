package com.example.fe.feature.auth.model

// 인증 상태 (성공, 실패, 로딩 등)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class SignedUp(val message: String) : AuthState() // 회원가입 완료 상태 (메세지 포함)
    data class NeedsExtraInfo(val name: String, val email: String) : AuthState() // 소셜 로그인 후 추가 정보 필요
    data class Error(val message: String) : AuthState()
}
