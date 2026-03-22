package com.example.fe.feature.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.feature.practice.data.PracticeRepository
import com.example.fe.data.dto.QuizItemDto
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

    // 응용학습 문제 목록 불러오기
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
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "로그인 토큰이 없습니다."
                    )
                    return@launch
                }

                val quizzes = repository.getQuizzes(token, topicId, language)

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

    // 사용자가 입력한 답과 정답 비교
    fun checkAnswers(
        quizIndex: Int,
        userAnswers: List<String>
    ): Boolean {
        val quiz = _uiState.value.quizzes.getOrNull(quizIndex) ?: return false

        // blanks가 null이면 채점 불가
        val blanks = quiz.blanks ?: return false

        // 빈칸 수와 답 개수가 다르면 오답 처리
        if (blanks.size != userAnswers.size) return false

        return blanks.mapIndexed { index, blank ->
            userAnswers[index].trim() == blank.content.trim()
        }.all { it }
    }

    // 정답을 맞췄을 때 완료 처리 API 호출
    fun completeQuiz(
        quizIndex: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val quiz = _uiState.value.quizzes.getOrNull(quizIndex) ?: return

        viewModelScope.launch {
            try {
                val token = com.example.fe.common.TokenManager.getAccessToken()
                if (token == null) {
                    throw Exception("로그인 토큰이 없습니다.")
                }
                
                repository.completeQuiz(token, quiz.exerciseId)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "완료 처리에 실패했습니다.")
            }
        }
    }
}