package com.example.fe.common

import android.content.Context

object LanguagePreferenceManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "preferred_language"

    fun saveLanguage(context: Context, language: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "JAVA") ?: "JAVA"
    }
}