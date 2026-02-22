package com.example.fe.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Activity
import com.example.fe.data.dto.UserRequest
import com.example.fe.api.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
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
                    registerUserToServer(name, email, language) { isSuccess, message ->
                        if (isSuccess) {
                            auth.signOut()
                            _authState.value = AuthState.SignedUp(message)
                        } else {
                            _authState.value = AuthState.Error(message)
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    // 구글 소셜 로그인
    fun signInWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // 소셜 로그인 성공 시, 서버에 유저 정보 등록 시도 (디폴트 언어 "Java" 사용)
                        val name = user.displayName ?: "User"
                        val email = user.email ?: ""
                        
                        registerUserToServer(name, email, "Java") { isSuccess, message ->
                            if (isSuccess) {
                                _authState.value = AuthState.Success
                            } else {
                                _authState.value = AuthState.Error("서버 등록 실패: $message")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error("유저 정보를 가져올 수 없습니다.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Google Sign-In failed")
                }
            }
    }

    // 깃허브 소셜 로그인
    fun signInWithGithub(activity: Activity) {
        _authState.value = AuthState.Loading
        val provider = OAuthProvider.newBuilder("github.com")

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val name = user.displayName ?: "User"
                    val email = user.email ?: ""
                    
                    registerUserToServer(name, email, "Java") { isSuccess, message ->
                        if (isSuccess) {
                            _authState.value = AuthState.Success
                        } else {
                            _authState.value = AuthState.Error("서버 등록 실패: $message")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error("유저 정보를 가져올 수 없습니다.")
                }
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "GitHub Sign-In failed")
            }
    }

    /**
     * 우리 서버 DB에 유저 정보를 등록하는 공통 로직 (로컬/소셜 로그인에서 공동 사용)
     */
    private fun registerUserToServer(name: String, email: String, language: String, onComplete: (Boolean, String) -> Unit) {
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
                    onComplete(true, response.body()?.message ?: "회원가입 완료")
                } else {
                    onComplete(false, response.body()?.message ?: "서버 등록 실패")
                }
            } catch (e: Exception) {
                onComplete(false, "네트워크 오류: ${e.message}")
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
