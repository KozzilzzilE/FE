package com.example.fe.feature.study.practice.data

import com.example.fe.feature.study.practice.api.PracticeApi
import com.example.fe.feature.study.practice.dto.QuizItemDto

class PracticeRepository(
    private val practiceApi: PracticeApi
) {

    // 응용학습 문제 조회
    suspend fun getQuizzes(
        topicId: Long,
        language: String
    ): List<QuizItemDto> {

        // API 호출
        val response = practiceApi.getPracticeQuizzes(topicId, language)

        // API 성공 여부 확인
        if (response.isSuccess) {

            // result가 null일 수도 있으므로 안전 처리
            val result = response.result ?: return emptyList()

            // 실제 문제 리스트 반환
            return result.appliedExercises
        } else {

            // API 실패 시 message 전달
            throw Exception(response.message)
        }
    }


    // 응용 문제 완료 처리
    suspend fun completeQuiz(
        exerciseId: Long
    ) {

        val response = practiceApi.completePractice(exerciseId)

        // 실패 시 예외 발생
        if (!response.isSuccess) {
            throw Exception(response.message)
        }
    }
}