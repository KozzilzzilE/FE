package com.example.fe.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Activity
import com.example.fe.data.dto.SignUpRequest
import com.example.fe.data.dto.LoginRequest
import com.example.fe.api.RetrofitClient
import com.example.fe.feature.auth.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.fe.common.TokenManager

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
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("이메일과 비밀번호를 입력해주세요.")
            return
        }
        
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        user.getIdToken(true).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val firebaseToken = tokenTask.result?.token ?: ""
                                loginToServer(firebaseToken)
                            } else {
                                _authState.value = AuthState.Error("토큰 발급 실패")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error("유저 정보를 찾을 수 없습니다.")
                    }
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
                    val user = task.result?.user
                    if (user != null) {
                        user.getIdToken(true).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val firebaseToken = tokenTask.result?.token ?: ""
                                // Firebase 가입 성공 시, 서버에 추가 정보와 토큰을 함께 전송
                                registerUserToServer(firebaseToken, name, email, language) { isSuccess, message ->
                                    if (isSuccess) {
                                        auth.signOut()
                                        _authState.value = AuthState.SignedUp(message)
                                    } else {
                                        _authState.value = AuthState.Error(message)
                                    }
                                }
                            } else {
                                _authState.value = AuthState.Error("토큰 발급 실패")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error("유저 정보를 찾을 수 없습니다.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    // [회원가입용] 구글 소셜 연동 -> 추가 정보 필요 (NeedsExtraInfo) 방출
    fun signInWithGoogleSignUp(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    val user = result?.user
                    if (user != null) {
                        val name = user.displayName ?: ""
                        val email = user.email ?: ""
                        _authState.value = AuthState.NeedsExtraInfo(name, email)
                    } else {
                        _authState.value = AuthState.Error("유저 정보를 가져올 수 없습니다.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Google Sign-In failed")
                }
            }
    }

    // [로그인용] 구글 소셜 연동 -> 즉시 성공 (Success) 방출
    fun signInWithGoogleLogin(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    val user = result?.user
                    if (user != null) {
                        // 로그인 시 서버에 토큰 유효성 검사 요청
                        user.getIdToken(true).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val firebaseToken = tokenTask.result?.token ?: ""
                                loginToServer(firebaseToken)
                            } else {
                                _authState.value = AuthState.Error("구글 토큰 발급 실패")
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

    // [회원가입용] 깃허브 소셜 연동 -> 추가 정보 필요 (NeedsExtraInfo) 방출
    fun signInWithGithubSignUp(activity: Activity) {
        _authState.value = AuthState.Loading
        val provider = OAuthProvider.newBuilder("github.com")

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val name = user.displayName ?: ""
                    val email = user.email ?: ""
                    _authState.value = AuthState.NeedsExtraInfo(name, email)
                } else {
                    _authState.value = AuthState.Error("유저 정보를 가져올 수 없습니다.")
                }
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "GitHub Sign-In failed")
            }
    }

    // [로그인용] 깃허브 소셜 연동 -> 즉시 성공 (Success) 방출
    fun signInWithGithubLogin(activity: Activity) {
        _authState.value = AuthState.Loading
        val provider = OAuthProvider.newBuilder("github.com")

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    user.getIdToken(true).addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val firebaseToken = tokenTask.result?.token ?: ""
                            loginToServer(firebaseToken)
                        } else {
                            _authState.value = AuthState.Error("깃허브 토큰 발급 실패")
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

    // 소셜 로그인 회원의 추가 정보 취합 후 최종 백엔드 저장
    fun completeSocialSignUp(name: String, email: String, language: String) {
        _authState.value = AuthState.Loading
        val user = auth.currentUser
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener { tokenTask ->
                if (tokenTask.isSuccessful) {
                    val firebaseToken = tokenTask.result?.token ?: ""
                    registerUserToServer(firebaseToken, name, email, language) { isSuccess, message ->
                        if (isSuccess) {
                            // 저장 성공 시 회원가입 완료 알림을 띄우기 위해 SignedUp 상태 발행
                            _authState.value = AuthState.SignedUp(message)
                        } else {
                            _authState.value = AuthState.Error("서버 등록 실패: $message")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error("Firebase 토큰 획득 실패")
                }
            }
        } else {
            _authState.value = AuthState.Error("로그인된 사용자 정보가 없습니다.")
        }
    }

    /**
     * 우리 서버 DB에 유저 정보를 등록하는 공통 로직 (로컬/소셜 로그인에서 공동 사용)
     */
    private fun registerUserToServer(firebaseToken: String, name: String, email: String, language: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.signUp(
                    SignUpRequest(
                        firebaseToken = firebaseToken,
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

    /**
     * 우리 서버 DB에 로그인 유효성을 검사하는 로직 (기존 회원의 로컬/소셜 로그인용)
     */
    private fun loginToServer(firebaseToken: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.login(
                    LoginRequest(firebaseToken = firebaseToken)
                )
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val loginResult = response.body()?.result
                    if (loginResult != null && loginResult.accessToken.isNotEmpty()) {
                        // 서버 토큰을 앱 로컬 금고(SharedPreferences)에 영구 저장합니다.
                        TokenManager.saveAccessToken(loginResult.accessToken)
                        
                        // 서버 인증 성공 시 최종 클라이언트 로그인 승인
                        _authState.value = AuthState.Success
                    } else {
                        // 결과 객체가 없거나 토큰이 비어있으면 에러
                        auth.signOut()
                        _authState.value = AuthState.Error("서버 토큰 발급 실패")
                    }
                } else {
                    // 서버에서 인증 실패 (미가입 상태 등) 처리
                    auth.signOut()
                    _authState.value = AuthState.Error(response.body()?.message ?: "서버 로그인 인증 실패")
                }
            } catch (e: Exception) {
                auth.signOut()
                _authState.value = AuthState.Error(e.message ?: "네트워크 오류 발생")
            }
        }
    }

    // 로그아웃 (필요시 호출)
    fun logout() {
        auth.signOut()
        TokenManager.clearAccessToken() // 서버 토큰도 함께 날립니다
        _authState.value = AuthState.Idle
    }
}
