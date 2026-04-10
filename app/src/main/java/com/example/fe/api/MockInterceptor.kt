package com.example.fe.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * ============================================================
 * [MOCK 인터셉터] 가짜 서버 응답을 생성하는 OkHttp Interceptor
 * ============================================================
 *
 * 이 클래스는 앱이 서버로 보내는 모든 HTTP 요청을 가로채서,
 * 실제 네트워크 통신 없이 MockResponseData에 정의된 JSON을 반환합니다.
 *
 * ★ 실제 서버 연동 시: RetrofitClient.kt에서 이 인터셉터 등록 한 줄만
 *   제거하거나 MockConfig.USE_MOCK = false 로 바꾸면 됩니다.
 *   Repository, ViewModel 등 다른 코드는 전혀 수정할 필요 없습니다.
 *
 * ★ 동작 원리:
 *   1. 앱이 API를 호출하면 OkHttp가 이 인터셉터를 먼저 거칩니다.
 *   2. 요청 URL의 경로(path)를 확인하여 알맞은 Mock JSON을 선택합니다.
 *   3. HTTP 200 OK 응답에 JSON을 담아 앱에게 돌려줍니다.
 *   4. 앱은 실제 서버에서 응답이 온 것으로 인식하여 정상 동작합니다.
 */
class MockInterceptor : Interceptor {

    companion object {
        private const val TAG = "MOCK_SERVER" // [MOCK] Logcat 필터용 태그
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath   // 예: /api/v1/users/main
        val method = request.method           // 예: GET, POST

        // [MOCK] URL 경로에 따라 적절한 가짜 JSON 응답을 선택합니다.
        val (responseJson, description) = matchResponse(path, method)

        // [MOCK] 가짜 응답을 사용 중임을 Logcat에 명확히 표시합니다.
        Log.d(TAG, "🟡 [MOCK 응답] $method $path → $description")

        // [MOCK] 실제 네트워크 통신 없이 가짜 응답을 생성하여 반환합니다.
        return Response.Builder()
            .code(200)
            .message("OK (Mock)")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .body(responseJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    /**
     * [MOCK] URL 경로와 HTTP 메서드를 기반으로 적절한 Mock 응답 JSON을 반환합니다.
     *
     * @return Pair<JSON 문자열, 설명 문자열>
     */
    private fun matchResponse(path: String, method: String): Pair<String, String> {
        return when {
            // ── 인증 ──
            path.contains("/auths/login") && method == "POST" ->
                Pair(MockResponseData.LOGIN, "로그인 성공")

            path.contains("/auths/signup") && method == "POST" ->
                Pair(MockResponseData.SIGN_UP, "회원가입 성공")

            // ── 메인 홈 ──
            path.contains("/users/main") && method == "GET" ->
                Pair(MockResponseData.HOME, "홈 화면 데이터")

            // ── 주제 목록 ──
            path.contains("/topics") && !path.contains("/problems") &&
                    !path.contains("/notions") && !path.contains("/applications") &&
                    method == "GET" ->
                Pair(MockResponseData.TOPICS, "주제 목록 (12개)")

            // ── 개념 학습 ──
            path.contains("/notions") && !path.contains("/completions") && method == "GET" ->
                Pair(MockResponseData.NOTIONS, "개념 학습 데이터 (10페이지)")

            // ── 개념 학습 완료 ──
            path.contains("/notions/completions") && method == "POST" ->
                Pair(MockResponseData.NOTION_COMPLETION, "개념 학습 완료 처리")

            // ── 응용 학습 ──
            path.contains("/applications") && !path.contains("/completions") && method == "GET" ->
                Pair(MockResponseData.PRACTICE_QUIZZES, "응용 학습 데이터")

            // ── 응용 학습 완료 ──
            path.contains("/applications/completions") && method == "POST" ->
                Pair(MockResponseData.PRACTICE_COMPLETION, "응용 학습 완료 처리")

            // ── 문제 학습 목록 ──
            path.contains("/topics/") && path.contains("/problems") && method == "GET" ->
                Pair(MockResponseData.PROBLEM_LIST, "문제 학습 목록")

            // ── 문제별 사용자 제출 기록 조회 ──
            path.contains("/problems/") && path.contains("/histories") && method == "GET" ->
                Pair(MockResponseData.PROBLEM_HISTORIES, "문제별 제출 기록 조회")

            // ── 문제 학습 상세 ──
            path.contains("/problems/") &&
                    !path.contains("/runs") &&
                    !path.contains("/submissions") &&
                    !path.contains("/solutions") &&
                    !path.contains("/histories") &&
                    !path.contains("/topics/") &&
                    method == "GET" -> {
                val idStr = path.substringAfter("/problems/").substringBefore("/")
                if (idStr == "1") {
                    Pair(MockResponseData.PROBLEM_DETAIL_1, "두 수의 합 문제 상세")
                } else {
                    Pair(MockResponseData.PROBLEM_DETAIL_DEFAULT, "문제 상세 (기본)")
                }
            }

            // ── 코드 실행 ──
            path.contains("/runs") && method == "POST" ->
                Pair(MockResponseData.PROBLEM_RUN, "코드 실행 결과")

            // ── 코드 제출 ──
            path.contains("/submissions") && method == "POST" ->
                Pair(MockResponseData.PROBLEM_SUBMIT, "코드 제출 결과")

            // ── 코드 실행 결과 조회 ──
            path.contains("/runs/") && path.contains("/results") && method == "GET" ->
                Pair(MockResponseData.PROBLEM_RUN_RESULT, "코드 실행 결과 조회")

            // ── 코드 채점 결과 조회 ──
            path.contains("/submissions/") && path.contains("/results") && method == "GET" ->
                Pair(MockResponseData.PROBLEM_SUBMISSION_RESULT, "코드 채점 결과 조회")

            // ── 모범 답안 ──
            path.contains("/solutions") && method == "GET" ->
                Pair(MockResponseData.PROBLEM_SOLUTION, "모범 답안")

            // ── 언어 목록 ──
            path.contains("/languages") && method == "GET" ->
                Pair(MockResponseData.LANGUAGES, "언어 목록 (4개)")

            else -> {
                Log.w(TAG, "⚠️ [MOCK] 매칭되는 Mock 데이터가 없습니다: $method $path")
                Pair(MockResponseData.DEFAULT_SUCCESS, "기본 응답 (매칭 없음)")
            }
        }
    }
}
