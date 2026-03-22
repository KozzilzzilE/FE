package com.example.fe.feature.concept.model

data class ConceptDetail(
    val id: Long,
    val title: String, 
    val description: String, 
    val codeExample: String?,
    val extraDescription: String? 
)

data class ConceptTopic(
    val topicId: Long,
    val topicTitle: String, 
    val concepts: List<ConceptDetail>
)
