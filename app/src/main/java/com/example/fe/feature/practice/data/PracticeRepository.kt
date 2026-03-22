package com.example.fe.feature.practice.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.data.dto.BlankDto
import com.example.fe.data.dto.QuizItemDto

class PracticeRepository(
    private val apiService: ApiService
) {

    companion object {
        // ============================================================
        // ★ Mock 데이터 전환 플래그 ★
        // true  → 더미 데이터 사용 (서버 없이 UI 테스트)
        // false → 실제 API 호출 (서버 연결 시 변경)
        // ============================================================
        const val USE_MOCK = false
    }

    // 응용학습 문제 조회
    suspend fun getQuizzes(
        token: String,
        topicId: Long,
        language: String = "JAVA"
    ): List<QuizItemDto> {
        if (USE_MOCK) return getMockQuizzes()

        val response = apiService.getPracticeQuizzes("Bearer $token", topicId, language)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

            if (body.isSuccess) {
                val result = body.result ?: return emptyList()
                return result.appliedExercises
            } else {
                throw Exception(body.message)
            }
        } else {
            throw Exception("응용학습 조회 실패: ${response.code()}")
        }
    }

    // 응용 문제 완료 처리
    suspend fun completeQuiz(
        token: String,
        exerciseId: Long
    ) {
        if (USE_MOCK) {
            Log.d("PracticeRepository", "[MOCK] 응용 학습 완료 처리: exerciseId=$exerciseId")
            return
        }

        val response = apiService.completePractice("Bearer $token", exerciseId)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("응답 본문이 비어 있습니다.")

            if (!body.isSuccess) {
                throw Exception(body.message)
            }
        } else {
            throw Exception("응용학습 완료 처리 실패: ${response.code()}")
        }
    }

    // ================================================================
    // Mock 데이터 (API 명세 기준)
    // API 연동 완료 후 삭제하거나 그대로 유지해도 무방
    // ================================================================
    private fun getMockQuizzes(): List<QuizItemDto> {
        return listOf(
            QuizItemDto(
                exerciseId = 1L,
                title = "HashMap으로 문자 개수 세기",
                description = "문자열에서 각 문자의 개수를 세는 코드의 빈칸을 채워보세요.",
                codeTemplate = "Map<Character, Integer> map = new ____<>();\nfor (char c : str.toCharArray()) {\n    map.____(c, map.getOrDefault(c, 0) + 1);\n}\nreturn map;",
                appliedCompleted = false,
                totalBlanks = 2,
                blanks = listOf(
                    BlankDto(content = "HashMap", answer = 1),
                    BlankDto(content = "put", answer = 2)
                )
            ),
            QuizItemDto(
                exerciseId = 2L,
                title = "두 배열의 교집합 찾기",
                description = "두 배열에서 공통으로 존재하는 원소를 찾는 코드의 빈칸을 채워보세요.",
                codeTemplate = "Set<Integer> set = new HashSet<>();\nfor (int n : nums1) set.____(n);\nList<Integer> result = new ArrayList<>();\nfor (int n : nums2) {\n    if (set.____(n)) result.add(n);\n}\nreturn result;",
                appliedCompleted = true,
                totalBlanks = 2,
                blanks = listOf(
                    BlankDto(content = "add", answer = 1),
                    BlankDto(content = "contains", answer = 2)
                )
            ),
            QuizItemDto(
                exerciseId = 3L,
                title = "HashMap으로 문자 개수 세기 2",
                description = "문자열에서 각 문자의 개수를 세는 코드의 빈칸을 채워보세요.",
                codeTemplate = "Map<Character, Integer> map = new ____<>();\nfor (char c : str.toCharArray()) {\n    map.____(c, map.getOrDefault(c, 0) + 1);\n}\nreturn map;",
                appliedCompleted = false,
                totalBlanks = 2,
                blanks = listOf(
                    BlankDto(content = "HashMap", answer = 1),
                    BlankDto(content = "put", answer = 2)
                )
            ),
            QuizItemDto(
                exerciseId = 3L,
                title = "HashMap으로 문자 개수 세기 3",
                description = "문자열에서 각 문자의 개수를 세는 코드의 빈칸을 채워보세요.",
                codeTemplate = "Map<Character, Integer> map = new ____<>();\nfor (char c : str.toCharArray()) {\n    map.____(c, map.getOrDefault(c, 0) + 1);\n}\nreturn map;",
                appliedCompleted = false,
                totalBlanks = 2,
                blanks = listOf(
                    BlankDto(content = "HashMap", answer = 1),
                    BlankDto(content = "put", answer = 2)
                )
            )
        )
    }
}