package com.example.fe.feature.csquiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fe.feature.csquiz.data.CsQuizRepository
import com.example.fe.feature.csquiz.model.CsQuizQuestion

class CsQuizViewModel(
    private val repository: CsQuizRepository = CsQuizRepository()
) : ViewModel() {

    private val questions: List<CsQuizQuestion> = repository.getQuizQuestions()

    var currentIndex by mutableIntStateOf(0)
        private set
    var answered by mutableStateOf<Boolean?>(null)
        private set
    var showResult by mutableStateOf(false)
        private set
    var score by mutableIntStateOf(0)
        private set

    val currentQuestion: CsQuizQuestion
        get() = questions[currentIndex]

    val total: Int
        get() = questions.size

    fun selectAnswer(userAnswer: Boolean) {
        if (answered != null) return
        answered = userAnswer
        if (userAnswer == currentQuestion.answer) score++
    }

    fun nextQuestion() {
        if (currentIndex < total - 1) {
            currentIndex++
            answered = null
        } else {
            showResult = true
        }
    }

    fun retry() {
        currentIndex = 0
        answered = null
        showResult = false
        score = 0
    }
}
