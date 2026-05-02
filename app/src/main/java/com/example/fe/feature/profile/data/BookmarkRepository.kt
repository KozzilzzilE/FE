package com.example.fe.feature.profile.data

import com.example.fe.api.ApiService
import com.example.fe.data.dto.BookmarkListResponse
import retrofit2.Response

class BookmarkRepository(private val apiService: ApiService) {

    suspend fun getBookmarks(token: String): Response<BookmarkListResponse> {
        return apiService.getBookmarks("Bearer $token")
    }

    suspend fun addBookmark(token: String, problemId: Long): Boolean {
        val response = apiService.addBookmark("Bearer $token", problemId)
        if (response.isSuccessful) {
            val body = response.body() ?: return false
            return body.isSuccess && body.result?.bookmarked == true
        }
        return false
    }

    suspend fun deleteBookmark(token: String, problemId: Long): Boolean {
        val response = apiService.deleteBookmark("Bearer $token", problemId)
        if (response.isSuccessful) {
            val body = response.body() ?: return false
            return body.isSuccess && body.result?.bookmarked == false
        }
        return false
    }
}
