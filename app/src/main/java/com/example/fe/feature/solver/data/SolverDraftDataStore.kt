package com.example.fe.feature.solver.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.fe.feature.solver.model.SolverDraft
import kotlinx.coroutines.flow.first

private const val DRAFT_DATASTORE_NAME = "solver_draft_datastore"

private val Context.solverDraftDataStore by preferencesDataStore(
    name = DRAFT_DATASTORE_NAME
)

class SolverDraftDataStore(
    private val context: Context
) {

    private fun languageKey(problemId: Int): Preferences.Key<String> =
        stringPreferencesKey("draft_language_$problemId")

    private fun codeKey(problemId: Int): Preferences.Key<String> =
        stringPreferencesKey("draft_code_$problemId")

    private fun savedAtKey(problemId: Int): Preferences.Key<Long> =
        longPreferencesKey("draft_saved_at_$problemId")

    suspend fun saveDraft(
        problemId: Int,
        language: String,
        code: String
    ) {
        context.solverDraftDataStore.edit { preferences ->
            preferences[languageKey(problemId)] = language
            preferences[codeKey(problemId)] = code
            preferences[savedAtKey(problemId)] = System.currentTimeMillis()
        }
    }

    suspend fun saveDraft(draft: SolverDraft) {
        context.solverDraftDataStore.edit { preferences ->
            preferences[languageKey(draft.problemId)] = draft.language
            preferences[codeKey(draft.problemId)] = draft.code
            preferences[savedAtKey(draft.problemId)] = draft.savedAt
        }
    }

    suspend fun loadDraft(problemId: Int): SolverDraft? {
        val preferences = context.solverDraftDataStore.data.first()

        val language = preferences[languageKey(problemId)] ?: return null
        val code = preferences[codeKey(problemId)] ?: return null
        val savedAt = preferences[savedAtKey(problemId)] ?: 0L

        return SolverDraft(
            problemId = problemId,
            language = language,
            code = code,
            savedAt = savedAt
        )
    }

    suspend fun clearDraft(problemId: Int) {
        context.solverDraftDataStore.edit { preferences ->
            preferences.remove(languageKey(problemId))
            preferences.remove(codeKey(problemId))
            preferences.remove(savedAtKey(problemId))
        }
    }

    suspend fun hasDraft(problemId: Int): Boolean {
        val preferences = context.solverDraftDataStore.data.first()
        return preferences[codeKey(problemId)] != null
    }
}