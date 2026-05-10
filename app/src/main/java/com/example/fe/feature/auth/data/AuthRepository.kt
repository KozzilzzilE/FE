package com.example.fe.feature.auth.data

import android.app.Activity
import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.common.TokenManager
import com.example.fe.data.dto.LoginRequest
import com.example.fe.data.dto.SignUpRequest
import com.example.fe.feature.auth.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val apiService: ApiService,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun login(email: String, pass: String): AuthState {
        if (email.isBlank() || pass.isBlank()) {
            return AuthState.Error("이메일과 비밀번호를 입력해주세요.")
        }

        return try {
            val taskResult = auth.signInWithEmailAndPassword(email, pass).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 찾을 수 없습니다.")
            val tokenResult = user.getIdToken(true).await()
            val firebaseToken = tokenResult.token ?: return AuthState.Error("토큰 발급 실패")
            
            loginToServer(firebaseToken)
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Login failed")
        }
    }

    suspend fun signUp(name: String, email: String, pass: String, language: String): AuthState {
        return try {
            val taskResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 찾을 수 없습니다.")
            val tokenResult = user.getIdToken(true).await()
            val firebaseToken = tokenResult.token ?: return AuthState.Error("토큰 발급 실패")

            val (isSuccess, message) = registerUserToServer(
                firebaseToken = firebaseToken,
                name = name,
                email = email,
                language = language
            )
            if (isSuccess) {
                auth.signOut()
                AuthState.SignedUp(message)
            } else {
                AuthState.Error(message)
            }
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Sign up failed")
        }
    }

    suspend fun signInWithGoogleSignUp(idToken: String): AuthState {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val taskResult = auth.signInWithCredential(credential).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 가져올 수 없습니다.")
            val name = user.displayName ?: ""
            val email = user.email ?: ""
            AuthState.NeedsExtraInfo(name, email)
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Google Sign-In failed")
        }
    }

    suspend fun signInWithGoogleLogin(idToken: String): AuthState {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val taskResult = auth.signInWithCredential(credential).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 가져올 수 없습니다.")
            val tokenResult = user.getIdToken(true).await()
            val firebaseToken = tokenResult.token ?: return AuthState.Error("구글 토큰 발급 실패")
            
            loginToServer(firebaseToken)
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Google Sign-In failed")
        }
    }

    suspend fun signInWithGithubSignUp(activity: Activity): AuthState {
        val provider = OAuthProvider.newBuilder("github.com")
        return try {
            val taskResult = auth.startActivityForSignInWithProvider(activity, provider.build()).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 가져올 수 없습니다.")
            val name = user.displayName ?: ""
            val email = user.email ?: ""
            AuthState.NeedsExtraInfo(name, email)
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "GitHub Sign-In failed")
        }
    }

    suspend fun signInWithGithubLogin(activity: Activity): AuthState {
        val provider = OAuthProvider.newBuilder("github.com")
        return try {
            val taskResult = auth.startActivityForSignInWithProvider(activity, provider.build()).await()
            val user = taskResult.user ?: return AuthState.Error("유저 정보를 가져올 수 없습니다.")
            val tokenResult = user.getIdToken(true).await()
            val firebaseToken = tokenResult.token ?: return AuthState.Error("깃허브 토큰 발급 실패")
            
            loginToServer(firebaseToken)
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "GitHub Sign-In failed")
        }
    }

    suspend fun completeSocialSignUp(name: String, email: String, language: String): AuthState {
        val user = auth.currentUser ?: return AuthState.Error("로그인된 사용자 정보가 없습니다.")
        return try {
            val tokenResult = user.getIdToken(true).await()
            val firebaseToken = tokenResult.token ?: return AuthState.Error("Firebase 토큰 획득 실패")
            
            val (isSuccess, message) = registerUserToServer(
                firebaseToken = firebaseToken,
                name = name,
                email = email,
                language = language
            )
            if (isSuccess) {
                AuthState.SignedUp(message)
            } else {
                AuthState.Error("서버 등록 실패: $message")
            }
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "소셜 가입 완료 실패")
        }
    }

    private suspend fun registerUserToServer(
        firebaseToken: String,
        name: String,
        email: String,
        language: String
    ): Pair<Boolean, String> {
        return try {
            val response = apiService.signUp(
                SignUpRequest(
                    firebaseToken = firebaseToken,
                    email = email,
                    nickname = name,
                    language = language
                )
            )
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val body = response.body()
                Log.d("AuthRepository", "회원가입 성공: code=${body?.code}, message=${body?.message}")
                Pair(true, body?.message ?: "회원가입 완료")
            } else {
                val body = response.body()
                Log.e("AuthRepository", "회원가입 실패: code=${body?.code}, message=${body?.message}")
                Pair(false, body?.message ?: "서버 등록 실패")
            }
        } catch (e: Exception) {
            Pair(false, "네트워크 오류: ${e.message}")
        }
    }

    private suspend fun loginToServer(firebaseToken: String): AuthState {
        return try {
            val response = apiService.login(LoginRequest(firebaseToken = firebaseToken))
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val body = response.body()
                Log.d("AuthRepository", "로그인 성공: code=${body?.code}, message=${body?.message}")
                val loginResult = body?.result
                if (loginResult != null && loginResult.accessToken.isNotEmpty()) {
                    TokenManager.saveAccessToken(loginResult.accessToken)
                    AuthState.Success
                } else {
                    auth.signOut()
                    AuthState.Error("서버 토큰 발급 실패")
                }
            } else {
                val body = response.body()
                Log.e("AuthRepository", "로그인 실패: code=${body?.code}, message=${body?.message}")
                auth.signOut()
                AuthState.Error(body?.message ?: "서버 로그인 인증 실패")
            }
        } catch (e: Exception) {
            auth.signOut()
            AuthState.Error(e.message ?: "네트워크 오류 발생")
        }
    }
    
    suspend fun logout(token: String): Boolean {
        return try {
            val response = apiService.logout("Bearer $token")
            if (response.isSuccessful) {
                Log.d("AuthRepository", "로그아웃 성공: code=${response.body()?.code}")
            } else {
                Log.e("AuthRepository", "로그아웃 API 에러: ${response.code()}")
            }
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "로그아웃 네트워크 예외: ${e.message}", e)
            true // API 실패여도 로컬 로그아웃은 진행
        } finally {
            auth.signOut()
            TokenManager.clearAccessToken()
        }
    }
}
