package com.example.fe.feature.study.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.study.practice.data.PracticeRepository
import com.example.fe.feature.study.practice.dto.QuizItemDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PracticeUiState(
    val isLoading: Boolean = false,
    val quizzes: List<QuizItemDto> = emptyList(),
    val error: String? = null
)

class PracticeViewModel(
    private val repository: PracticeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    fun loadQuizzes(
        topicId: Long,
        language: String = "JAVA"
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val quizzes = repository.getQuizzes(topicId, language)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    quizzes = quizzes
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "응용 퀴즈를 불러오지 못했습니다."
                )
            }
        }
    }

    fun checkAnswers(
        quizIndex: Int,
        userAnswers: List<String>
    ): Boolean {
        val quiz = _uiState.value.quizzes.getOrNull(quizIndex) ?: return false

        if (quiz.blanks.size != userAnswers.size) return false

        return quiz.blanks.mapIndexed { index, blank ->
            userAnswers[index].trim() == blank.content.trim()
        }.all { it }
    }
}