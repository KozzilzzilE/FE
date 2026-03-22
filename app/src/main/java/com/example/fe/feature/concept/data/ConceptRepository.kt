package com.example.fe.feature.concept.data

import android.util.Log
import com.example.fe.api.ApiService
import com.example.fe.data.dto.ConceptResponse
import com.example.fe.data.dto.ConceptResult
import com.example.fe.data.dto.NotionDto
import com.example.fe.data.dto.ExampleCodeDto

class ConceptRepository(private val apiService: ApiService) {

    companion object {
        // ============================================================
        // ★ Mock 데이터 전환 플래그 ★
        // true  → 더미 데이터 사용 (서버 없이 UI 테스트)
        // false → 실제 API 호출 (서버 연결 시 변경)
        // ============================================================
        const val USE_MOCK = false
    }

    suspend fun getConcepts(token: String, topicId: Long, language: String): ConceptResponse? {
        if (USE_MOCK) return getMockConcepts(topicId)

        return try {
            val response = apiService.getNotions(token = "Bearer $token", topicId = topicId, language = language)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ConceptRepository", "개념 학습 API 호출 에러: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "개념 학습 API 네트워크 예외: ${e.message}", e)
            null
        }
    }

    suspend fun completeConcept(token: String, notionId: Long): Boolean {
        if (USE_MOCK) {
            Log.d("ConceptRepository", "[MOCK] 개념 완료 처리: notionId=$notionId")
            return true
        }

        return try {
            val response = apiService.postNotionCompletion(token = "Bearer $token", notionId = notionId)
            if (response.isSuccessful) {
                response.body()?.isSuccess == true
            } else {
                Log.e("ConceptRepository", "개념 학습 완료 처리 에러: ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "개념 학습 완료 네트워크 예외: ${e.message}", e)
            false
        }
    }

    // ================================================================
    // Mock 데이터 (API 명세 기준)
    // API 연동 완료 후 삭제하거나 그대로 유지해도 무방
    // ================================================================
    private fun getMockConcepts(topicId: Long): ConceptResponse {
        val notions = listOf(
            NotionDto(
                notionId = 1L,
                pageNo = 1,
                title = "해시란?",
                point = "해시(Hash)는 데이터를 효율적으로 저장하고 검색하기 위한 자료구조입니다.",
                detail = "키(Key)와 값(Value)의 쌍으로 데이터를 저장하며, 평균 O(1)의 시간 복잡도로 데이터에 접근할 수 있습니다.",
                imgUrl = null,
                exampleCode = null,
                notionCompleted = true
            ),
            NotionDto(
                notionId = 2L,
                pageNo = 2,
                title = "해시 함수",
                point = "해시 함수는 임의의 크기를 가진 데이터를 고정된 크기의 값으로 변환합니다.",
                detail = "문자열이나 숫자를 입력 받아 배열의 인덱스로 사용할 수 있는 정수 값을 반환합니다.",
                imgUrl = null,
                exampleCode = ExampleCodeDto(
                    language = "JAVA",
                    content = "public int hashFunction(String key) {\n    int hash = 0;\n    for (char c : key.toCharArray()) {\n        hash = (hash + c) % TABLE_SIZE;\n    }\n    return hash;\n}"
                ),
                notionCompleted = true
            ),
            NotionDto(
                notionId = 3L,
                pageNo = 3,
                title = "실전 활용",
                point = "해시는 다양한 알고리즘 문제에서 활용됩니다.",
                detail = "중복 검사, 빈도수 계산, 두 배열의 교집합 찾기 등에 유용하게 사용됩니다.",
                imgUrl = null,
                exampleCode = null,
                notionCompleted = false
            )
        )

        return ConceptResponse(
            isSuccess = true,
            code = "LEARNING_200",
            message = "개념 학습 조회 성공 (Mock)",
            result = ConceptResult(
                topicId = topicId,
                count = notions.size,
                notions = notions
            )
        )
    }
}

