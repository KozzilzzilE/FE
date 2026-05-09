package com.example.fe.data.dto

import com.google.gson.annotations.SerializedName

/**
 * [찜 추가/삭제 API]
 * POST /api/v1/bookmarks/problems/{problemId}
 * DELETE /api/v1/bookmarks/problems/{problemId}
 */
data class BookmarkResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookmarkResult?
)

data class BookmarkResult(
    @SerializedName("bookmarked") val bookmarked: Boolean
)

/**
 * [찜 목록 조회 API]
 * GET /api/v1/bookmarks/problems
 */
data class BookmarkListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<BookmarkItem>?
)

data class BookmarkItem(
    @SerializedName("problemId") val problemId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("difficultyDisplayName") val difficultyDisplayName: String?,
    @SerializedName("bookmarkCount") val bookmarkCount: Int
)
