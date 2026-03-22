package com.example.fe.feature.concept

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fe.data.dto.NotionDto
import com.example.fe.feature.concept.data.ConceptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConceptUiState(
    val isLoading: Boolean = false,
    val topicTitle: String = "",
    val concepts: List<NotionDto> = emptyList(),
    val currentIndex: Int = 0,
    val error: String? = null
)

class ConceptViewModel(private val repository: ConceptRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    fun loadConcepts(topicId: Long, language: String = "JAVA", initialIndex: Int = 0) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val token = com.example.fe.common.TokenManager.getAccessToken()
            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "로그인 토큰이 없습니다.") }
                return@launch
            }
            
            val response = repository.getConcepts(token, topicId, language)
            
            if (response != null && response.isSuccess) {
                // 응답 데이터에서 notions 추출 및 pageNo 기준 정렬
                val sortedNotions = response.result?.notions?.sortedBy { it.pageNo } ?: emptyList()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        topicTitle = "알고리즘 개념", 
                        concepts = sortedNotions,
                        currentIndex = initialIndex
                    )
                }
            } else {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = response?.message ?: "데이터를 불러오는 데 실패했습니다."
                    )
                }
            }
        }
    }

    fun nextChapter() {
        val currentConcepts = _uiState.value.concepts
        val currentIndex = _uiState.value.currentIndex
        val maxIndex = currentConcepts.size - 1
        
        // 현재 인덱스가 범위 안이고 데이터가 있는 경우
        if (currentConcepts.isNotEmpty() && currentIndex in currentConcepts.indices) {
            val currentNotion = currentConcepts[currentIndex]
            
            // 만약 현재 개념이 완료되지 않았다면 완료 처리 서버 전송
            if (!currentNotion.notionCompleted) {
                viewModelScope.launch {
                    val token = com.example.fe.common.TokenManager.getAccessToken()
                    if (token != null) {
                        val success = repository.completeConcept(token, currentNotion.notionId)
                        if (success) {
                            // 로컬 상태를 업데이트하여 다음 렌더링 시 완료 반영
                            val updatedConcepts = currentConcepts.toMutableList()
                            updatedConcepts[currentIndex] = currentNotion.copy(notionCompleted = true)
                            _uiState.update { it.copy(concepts = updatedConcepts) }
                        }
                    }
                }
            }
        }

        if (currentIndex < maxIndex) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
        }
    }

    // "다음 단계" 버튼 클릭 시 마지막 페이지 완료 처리용 함수
    fun completeCurrentNotionAndGoNext(onComplete: () -> Unit) {
        val currentConcepts = _uiState.value.concepts
        val currentIndex = _uiState.value.currentIndex
        
        if (currentConcepts.isNotEmpty() && currentIndex in currentConcepts.indices) {
            val currentNotion = currentConcepts[currentIndex]
            
            if (!currentNotion.notionCompleted) {
                viewModelScope.launch {
                    val token = com.example.fe.common.TokenManager.getAccessToken()
                    if (token != null) {
                        val success = repository.completeConcept(token, currentNotion.notionId)
                        if (success) {
                            val updatedConcepts = currentConcepts.toMutableList()
                            updatedConcepts[currentIndex] = currentNotion.copy(notionCompleted = true)
                            _uiState.update { it.copy(concepts = updatedConcepts) }
                        }
                    }
                    // 성공 여부와 무관하게 일단 다음 단계로 넘어가도록 처리
                    onComplete()
                }
            } else {
                onComplete()
            }
        } else {
            onComplete()
        }
    }

    fun prevChapter() {
        if (_uiState.value.currentIndex > 0) {
            _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
        }
    }
}
