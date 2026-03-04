package com.example.fe.common

import android.content.Context
import android.content.SharedPreferences

/**
 * 앱 전역에서 서버 인증 토큰(AccessToken)을 읽고 쓰기 위한 로컬 저장소 관리자
 * 앱의 진입점(MainActivity 또는 Application)에서 init()으로 가장 먼저 초기화해야 함
 */
object TokenManager {
    private const val PREF_NAME = "FeAppPrefs"
    private const val KEY_ACCESS_TOKEN = "server_access_token"

    private var prefs: SharedPreferences? = null

    // 앱 실행 시 단 한 번 호출하여 컨텍스트를 주입
    fun init(context: Context) {
        if (prefs == null) {
            // Context.MODE_PRIVATE를 사용하여 해당 앱에서만 접근 가능하도록 보호
            prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    // 서버 토큰 저장
    fun saveAccessToken(token: String) {
        prefs?.edit()?.putString(KEY_ACCESS_TOKEN, token)?.apply()
    }

    // 서버 토큰 조회
    fun getAccessToken(): String? {
        return prefs?.getString(KEY_ACCESS_TOKEN, null)
    }

    // 서버 토큰 삭제 (로그아웃용)
    fun clearAccessToken() {
        prefs?.edit()?.remove(KEY_ACCESS_TOKEN)?.apply()
    }
}
