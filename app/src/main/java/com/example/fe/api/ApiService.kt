package com.example.fe.api

import com.example.fe.data.dto.ProblemListResponse
import com.example.fe.data.dto.AllProblemListResponse
import com.example.fe.data.dto.SignUpRequest
import com.example.fe.data.dto.SignUpResponse
import com.example.fe.data.dto.LoginRequest
import com.example.fe.data.dto.LoginResponse
import com.example.fe.data.dto.HomeResponse

import com.example.fe.data.dto.TopicResponse
import com.example.fe.data.dto.ConceptResponse
import com.example.fe.data.dto.PracticeResponseDto
import com.example.fe.data.dto.PracticeCompletionResponseDto
import com.example.fe.data.dto.LanguageResponse

import com.example.fe.data.dto.MyPageResponse
import com.example.fe.data.dto.UpdateLanguageRequest
import com.example.fe.data.dto.UpdateLanguageResponse
import com.example.fe.data.dto.UpdateNicknameRequest
import com.example.fe.data.dto.UpdateNicknameResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.DELETE

interface ApiService {
    // --- [인증] ---

    // 회원가입 : 회원 정보 서버에 등록 (소셜 연동 포함)
    @POST("api/v1/auths/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    // 로그인 : 서버로 로그인 요청 (Firebase 토큰 인증용)
    @POST("api/v1/auths/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // --- [메인화면] ---

    // 메인화면 : 홈 화면 데이터 가져오기
    @GET("api/v1/users/main")
    suspend fun getHomeData(
        @retrofit2.http.Header("Authorization") token: String
    ): Response<HomeResponse>

    // --- [알고리즘 주제 목록 조회] ---

    // 알고리즘 주제 목록 조회
    @GET("api/v1/topics")
    suspend fun getTopics(
        @retrofit2.http.Header("Authorization") token: String
    ): Response<TopicResponse>
    
    // --- [개념학습] ---

    // 개념 학습(notions) 조회 (특정 알고리즘 주제 및 언어)
    @GET("api/v1/learnings/{topicId}/notions")
    suspend fun getNotions(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): Response<ConceptResponse>

    // 개념 학습 완료 처리
    @POST("api/v1/learnings/notions/completions/{notionId}")
    suspend fun postNotionCompletion(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("notionId") notionId: Long
    ): Response<com.example.fe.data.dto.ConceptCompletionResponse>

    // --- [응용학습] ---

    // 응용학습 문제 조회
    @GET("/api/v1/learnings/{topicId}/applications")
    suspend fun getPracticeQuizzes(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("topicId") topicId: Long,
        @Query("language") language: String
    ): Response<PracticeResponseDto>

    // 응용학습 문제 완료 처리
    @POST("/api/v1/learnings/applications/completions/{exerciseId}")
    suspend fun completePractice(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("exerciseId") exerciseId: Long
    ): Response<PracticeCompletionResponseDto>

    // --- [문제학습] ---
    
    // 특정 알고리즘 주제의 문제 목록 조회
    @GET("api/v1/topics/{topicId}/problems")
    suspend fun getTopicProblems(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("topicId") topicId: Long
    ): Response<ProblemListResponse>

    // 전체 문제 목록 조회
    @GET("api/v1/problems")
    suspend fun getAllProblems(
        @retrofit2.http.Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20,
        @Query("difficulty") difficulty: String? = null
    ): Response<AllProblemListResponse>

    // 문제 상세 정보 조회
    @GET("api/v1/problems/{problemId}")
    suspend fun getProblemDetail(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Query("language") language: String
    ): Response<com.example.fe.data.dto.ProblemDetailResponse>

    // 모범 답안 조회
    @GET("api/v1/problems/{problemId}/solutions")
    suspend fun getProblemSolution(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Query("language") language: String
    ): Response<com.example.fe.data.dto.SolutionResponse>

    // 문제 코드 실행 요청
    @POST("api/v1/problems/{problemId}/runs")
    suspend fun runCode(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Query("language") language: String,
        @Body request: com.example.fe.data.dto.RunRequestDto
    ): Response<com.example.fe.data.dto.RunResponseDto>

    // 문제 코드 정답 요청
    @POST("api/v1/problems/{problemId}/submissions")
    suspend fun submitCode(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Query("language") language: String,
        @Body request: com.example.fe.data.dto.SubmitRequestDto
    ): Response<com.example.fe.data.dto.SubmitResponseDto>

    // 문제 코드 실행 결과 조회
    @GET("api/v1/problems/runs/{token}/results")
    suspend fun getRunResult(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("token") runToken: String
    ): Response<com.example.fe.data.dto.RunResultResponseDto>

    // 문제 코드 채점 결과 조회
    @GET("api/v1/problems/submissions/{historyId}/results")
    suspend fun getSubmissionResult(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("historyId") historyId: Long,
        @Query("submissionId") submissionId: String
    ): Response<com.example.fe.data.dto.SubmissionResultResponseDto>

    // 문제별 사용자 제출 기록 조회
    @GET("api/v1/problems/{problemId}/histories")
    suspend fun getSubmissionHistories(
        @retrofit2.http.Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): Response<com.example.fe.data.dto.SubmissionHistoryResponseDto>

    // --- [언어] ---
    
    // 언어 목록 조회
    @GET("api/v1/languages/lists")
    suspend fun getLanguages(): Response<LanguageResponse>

    // --- [마이페이지] ---

    // 마이페이지 조회
    @GET("api/v1/users/main")
    suspend fun getMyPageInfo(
        @Header("Authorization") token: String
    ): Response<MyPageResponse>

    // 언어 변경
    @PATCH("api/v1/users/me/languages")
    suspend fun updateLanguage(
        @Header("Authorization") token: String,
        @Body request: UpdateLanguageRequest
    ): Response<UpdateLanguageResponse>

    // 이름 변경
    @PATCH("api/v1/users/me/names")
    suspend fun updateNickname(
        @Header("Authorization") token: String,
        @Body request: UpdateNicknameRequest
    ): Response<UpdateNicknameResponse>

    // --- [찜(북마크)] ---

    // 찜 목록 조회
    @GET("api/v1/bookmarks/problems")
    suspend fun getBookmarks(
        @Header("Authorization") token: String
    ): Response<com.example.fe.data.dto.BookmarkListResponse>

    // 찜 추가
    @POST("api/v1/bookmarks/problems/{problemId}")
    suspend fun addBookmark(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): Response<com.example.fe.data.dto.BookmarkResponse>

    // 찜 삭제
    @DELETE("api/v1/bookmarks/problems/{problemId}")
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): Response<com.example.fe.data.dto.BookmarkResponse>
}


