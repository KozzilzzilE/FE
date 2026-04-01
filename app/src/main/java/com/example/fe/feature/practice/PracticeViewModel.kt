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
                val token = com.example.fe.common.TokenManager.getAccessToken() ?: "mock_token_for_dev"
                
                if (token == null) { // 실제 서버 연동 시에는 가짜 토큰 제거 후 null 체크 필요
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

                // 로컬 상태 업데이트: 해당 퀴즈를 완료 처리
                val updatedQuizzes = _uiState.value.quizzes.toMutableList()
                updatedQuizzes[quizIndex] = quiz.copy(appliedCompleted = true)
                _uiState.value = _uiState.value.copy(quizzes = updatedQuizzes)

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "완료 처리에 실패했습니다.")
            }
        }
    }

    // 다음 버튼 클릭 시: 현재 퀴즈 완료 처리 후 다음 문제로 이동
    fun nextQuizAndComplete(
        currentIndex: Int,
        onMoveNext: () -> Unit
    ) {
        val quiz = _uiState.value.quizzes.getOrNull(currentIndex)

        // 이미 완료된 퀴즈면 바로 다음으로 이동
        if (quiz == null || quiz.appliedCompleted) {
            onMoveNext()
            return
        }

        // 미완료 퀴즈면 완료 API 호출 후 다음으로 이동
        completeQuiz(
            quizIndex = currentIndex,
            onSuccess = { onMoveNext() },
            onError = { onMoveNext() } // 에러가 나도 이동은 허용
        )
    }

    // 마지막 페이지에서 "다음 단계" 클릭 시: 현재 퀴즈 완료 처리 후 단계 전환
    fun completeCurrentQuizAndGoNext(
        currentIndex: Int,
        onComplete: () -> Unit
    ) {
        nextQuizAndComplete(currentIndex, onComplete)
    }
}