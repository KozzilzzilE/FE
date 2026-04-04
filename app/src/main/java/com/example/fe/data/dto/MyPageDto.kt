package com.example.fe.data.dto

data class MyPageResponseDto(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: MyPageResultDto
)

data class MyPageResultDto(
    val name: String,
    val languageId: Long,
    val languageName: String
)