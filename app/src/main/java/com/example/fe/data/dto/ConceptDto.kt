package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

data class ConceptResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ConceptResult?
)

data class ConceptResult(
    @SerializedName("topicId") val topicId: Long,
    @SerializedName("count") val count: Int,
    @SerializedName("notions") val notions: List<NotionDto>
)

data class NotionDto(
    @SerializedName("notionId") val notionId: Long,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("title") val title: String,
    @SerializedName("point") val point: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("imgUrl") val imgUrl: String?,
    @SerializedName("exampleCode") val exampleCode: ExampleCodeDto?,
    @SerializedName("notionCompleted") val notionCompleted: Boolean
)

data class ExampleCodeDto(
    @SerializedName("language") val language: String,
    @SerializedName("content") val content: String
)

data class ConceptCompletionResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ConceptCompletionResult?
)

data class ConceptCompletionResult(
    @SerializedName("notionId") val notionId: Long,
    @SerializedName("userName") val userName: String,
    @SerializedName("notionCompleted") val notionCompleted: Boolean
)
