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
    private val repository: AuthRepository // 데이터(API 등)를 실제로 다루는 실무자 객체
) : ViewModel() {

    // 현재의 '인증 상태(로딩 중인지, 성공인지, 에러 났는지 등)'를 저장하는 상태값
    // 외부에 직접 노출시키지 않고 내부에서만 값을 변경하기 위해 _authState로 정의함
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    
    // UI(화면)에서 구독하여 사용할 수 있는 읽기 전용 상태 변수
    val authState = _authState.asStateFlow()

    /**
     * 기본 이메일 로그인 시도
     */
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading // 1. 상태를 '로딩 중'으로 변경 (스피너 등 표시용)
            // 2. Repository에 로그인을 지시한 뒤, 그 결과(성공/에러)를 다시 상태에 업데이트
            _authState.value = repository.login(email, pass)
        }
    }

    /**
     * 기본 이메일 회원가입 시도
     */
    fun signUp(name: String, email: String, pass: String, language: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signUp(name, email, pass, language)
        }
    }

    /**
     * 구글 소셜 회원가입 시도 (구글에서 받은 인증 토큰을 전달)
     */
    fun signInWithGoogleSignUp(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGoogleSignUp(idToken)
        }
    }

    /**
     * 구글 소셜 로그인 시도 (가입된 계정인지 확인하기 위함)
     */
    fun signInWithGoogleLogin(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGoogleLogin(idToken)
        }
    }

    /**
     * 깃허브 소셜 회원가입 시도 (가입 진행을 위해 화면 액티비티 정보 필요)
     */
    fun signInWithGithubSignUp(activity: Activity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGithubSignUp(activity)
        }
    }

    /**
     * 깃허브 소셜 로그인 시도
     */
    fun signInWithGithubLogin(activity: Activity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.signInWithGithubLogin(activity)
        }
    }

    /**
     * 소셜 로그인 인증 후 앱 내 전용 추가 정보(닉네임, 언어 등) 입력 완료 시 호출
     */
    fun completeSocialSignUp(name: String, email: String, language: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _authState.value = repository.completeSocialSignUp(name, email, language)
        }
    }

    /**
     * 로그아웃 처리 (서버 토큰 블랙리스트 등록 후 로컬 정리)
     */
    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val token = com.example.fe.common.TokenManager.getAccessToken() ?: ""
            repository.logout(token)
            _authState.value = AuthState.LoggedOut
        }
    }
}
