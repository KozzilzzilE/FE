package com.example.fe.feature.csquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fe.common.TokenManager
import com.example.fe.feature.csquiz.data.CsQuizRepository
import com.example.fe.feature.csquiz.model.CsQuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CsQuizUiState {
    object Loading : CsQuizUiState()
    data class Error(val message: String) : CsQuizUiState()
    data class Quiz(
        val questions: List<CsQuizQuestion>,
        val currentIndex: Int = 0,
        val answered: Boolean? = null,
        val score: Int = 0,
        val showResult: Boolean = false
    ) : CsQuizUiState() {
        val currentQuestion: CsQuizQuestion get() = questions[currentIndex]
        val total: Int get() = questions.size
    }
}

class CsQuizViewModel(
    private val repository: CsQuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CsQuizUiState>(CsQuizUiState.Loading)
    val uiState: StateFlow<CsQuizUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = CsQuizUiState.Loading

            val token = TokenManager.getAccessToken()
            if (token.isNullOrEmpty()) {
                _uiState.value = CsQuizUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            val questions = repository.getQuizQuestions(token)
            _uiState.value = if (!questions.isNullOrEmpty()) {
                CsQuizUiState.Quiz(questions = questions)
            } else {
                CsQuizUiState.Error("문제를 불러오는 데 실패했습니다.")
            }
        }
    }

    fun selectAnswer(userAnswer: Boolean) {
        val current = _uiState.value as? CsQuizUiState.Quiz ?: return
        if (current.answered != null) return
        val correct = userAnswer == current.currentQuestion.answer
        _uiState.value = current.copy(
            answered = userAnswer,
            score = if (correct) current.score + 1 else current.score
        )
    }

    fun nextQuestion() {
        val current = _uiState.value as? CsQuizUiState.Quiz ?: return
        _uiState.value = if (current.currentIndex < current.total - 1) {
            current.copy(currentIndex = current.currentIndex + 1, answered = null)
        } else {
            current.copy(showResult = true)
        }
    }

    // 같은 문제 다시 풀기
    fun retry() {
        val current = _uiState.value as? CsQuizUiState.Quiz ?: return
        _uiState.value = current.copy(
            currentIndex = 0,
            answered = null,
            score = 0,
            showResult = false
        )
    }

    // 새로운 랜덤 문제 불러오기
    fun nextQuiz() {
        loadQuestions()
    }
}

class CsQuizViewModelFactory(
    private val repository: CsQuizRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CsQuizViewModel(repository) as T
    }
}
