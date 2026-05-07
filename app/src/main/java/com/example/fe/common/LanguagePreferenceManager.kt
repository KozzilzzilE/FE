package com.example.fe.common

import android.content.Context

object LanguagePreferenceManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "preferred_language"

    fun saveLanguage(context: Context, language: String) {
        TokenManager.savePreferredLanguage(language)
    }

    fun getLanguage(context: Context): String {
        return TokenManager.getPreferredLanguage() ?: "JAVA"
    }
}