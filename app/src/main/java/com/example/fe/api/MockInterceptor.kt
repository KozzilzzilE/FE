package com.example.fe.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject

/**
 * 실시간 찜 상태를 메모리에 저장하는 객체 (가짜 데이터베이스 역할)
 */
object MockDataStore {
    val bookmarkedProblems = mutableSetOf<Long>(1L) // 기본으로 1번 문제는 찜 상태로 시작
}

/**
 * ============================================================
 * [MOCK 인터셉터] 가짜 서버 응답을 생성하는 OkHttp Interceptor
 * ============================================================
 */
class MockInterceptor : Interceptor {

    companion object {
        private const val TAG = "MOCK_SERVER"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val method = request.method
        val language = request.url.queryParameter("language") ?: "JAVA"
        val difficultyParam = request.url.queryParameter("difficulty") // 난이도 파라미터 추출

        val requestBodyString = request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        }.orEmpty()

        // 1. 기존의 상세한 정적 Mock 응답 가져오기
        val (responseJson, description) = matchResponse(
            path = path,
            method = method,
            language = language,
            requestBodyString = requestBodyString
        )

        var finalJson = responseJson

        // 2. [찜 기능 추가] 메모리 상태를 반영하여 JSON 동적 조작
        try {
            if (path.contains("/bookmarks/problems/") && (method == "POST" || method == "DELETE")) {
                val problemId = path.substringAfterLast("/").toLongOrNull()
                if (problemId != null) {
                    if (method == "POST") MockDataStore.bookmarkedProblems.add(problemId)
                    else MockDataStore.bookmarkedProblems.remove(problemId)
                    val isAdd = method == "POST"
                    finalJson = """{"isSuccess":true,"code":"COMMON200","message":"찜 ${if(isAdd) "추가" else "삭제"} 성공","result":{"problemId":$problemId,"bookmarked":$isAdd}}"""
                }
            } else if (path.endsWith("/bookmarks/problems") && method == "GET") {
                val allProblemsObj = JSONObject(MockResponseData.PROBLEM_LIST)
                val allProblemsArray = allProblemsObj.getJSONObject("result").getJSONArray("problems")
                val resultArray = JSONArray()
                for (i in 0 until allProblemsArray.length()) {
                    val p = allProblemsArray.getJSONObject(i)
                    val pid = p.getLong("problemId")
                    if (MockDataStore.bookmarkedProblems.contains(pid)) {
                        val item = JSONObject()
                        item.put("problemId", pid)
                        item.put("title", p.getString("title"))
                        item.put("difficulty", p.getString("difficulty"))
                        item.put("bookmarkCount", p.optInt("bookmarkCount", 0) + 1)
                        resultArray.put(item)
                    }
                }
                finalJson = JSONObject().apply {
                    put("isSuccess", true); put("code", "COMMON200"); put("message", "성공"); put("result", resultArray)
                }.toString()
            } else if (path.contains("/problems") && method == "GET") {
                val jsonObject = JSONObject(finalJson)
                // 상세 페이지 ID 추출 (URL 기반이 가장 정확)
                val pidFromUrl = path.substringAfter("/problems/").substringBefore("/").substringBefore("?").toLongOrNull()
                
                val result = jsonObject.optJSONObject("result")
                val problems = result?.optJSONArray("problems")
                
                if (problems != null) { // 1. 목록인 경우
                    val filteredArray = JSONArray()
                    for (i in 0 until problems.length()) {
                        val p = problems.getJSONObject(i)
                        val pid = p.getLong("problemId")
                        val pDiff = p.optString("difficulty", "")
                        if (difficultyParam == null || pDiff.uppercase() == difficultyParam.uppercase()) {
                            p.put("isBookmark", MockDataStore.bookmarkedProblems.contains(pid))
                            filteredArray.put(p)
                        }
                    }
                    result.put("problems", filteredArray)
                    finalJson = jsonObject.toString()
                } else { // 2. 상세 페이지이거나 단일 문제 정보인 경우
                    val target = result ?: jsonObject // result 객체가 없으면 최상위에 주입
                    val pid = when {
                        target.has("problemId") -> target.getLong("problemId")
                        target.has("exerciseId") -> target.getLong("exerciseId")
                        else -> pidFromUrl
                    }
                    
                    if (pid != null) {
                        val isBookmarked = MockDataStore.bookmarkedProblems.contains(pid)
                        target.put("isBookmark", isBookmarked)
                        if (isBookmarked) {
                            target.put("bookmarkCount", target.optInt("bookmarkCount", 0) + 1)
                        }
                        finalJson = jsonObject.toString()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Mock JSON 조작 중 오류", e)
        }

        Log.d(TAG, "🟡 [MOCK 응답] $method $path ($description)")

        return Response.Builder()
            .code(200)
            .message("OK (Mock)")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .body(finalJson.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun matchResponse(
        path: String,
        method: String,
        language: String,
        requestBodyString: String
    ): Pair<String, String> {
        return when {
            // ── 인증 ──
            path.contains("/auths/login") && method == "POST" -> Pair(MockResponseData.getLogin(), "로그인 성공")
            path.contains("/auths/signup") && method == "POST" -> Pair(MockResponseData.SIGN_UP, "회원가입 성공")

            // ── 메인 홈 / 마이페이지 조회 ──
            path.contains("/users/main") && method == "GET" -> Pair(MockResponseData.getMyPage(), "메인 정보 조회")

            // ── 사용자 설정 변경 ──
            path.contains("/users/me/languages") && method == "PATCH" -> {
                val lang = extractJsonValue(requestBodyString, "language") ?: MockResponseData.currentLanguage
                Pair(MockResponseData.updateLanguage(lang), "언어 변경")
            }
            path.contains("/users/me/names") && method == "PATCH" -> {
                val nick = extractJsonValue(requestBodyString, "nickname") ?: MockResponseData.currentNickname
                Pair(MockResponseData.updateNickname(nick), "이름 변경")
            }

            // ── 주제 목록 ──
            path.contains("/topics") && !path.contains("/problems") && !path.contains("/notions") && !path.contains("/applications") && method == "GET" ->
                Pair(MockResponseData.TOPICS, "주제 목록")

            // ── 개념 학습 ──
            path.contains("/notions") && !path.contains("/completions") && method == "GET" -> {
                val res = when (language.uppercase()) {
                    "PYTHON" -> MockResponseData.NOTIONS_PYTHON
                    "C++", "CPP" -> MockResponseData.NOTIONS_CPP
                    "JAVASCRIPT" -> MockResponseData.NOTIONS_JAVASCRIPT
                    else -> MockResponseData.NOTIONS
                }
                Pair(res, "개념 학습 ($language)")
            }

            // ── 개념 학습 완료 ──
            path.contains("/notions/completions") && method == "POST" -> Pair(MockResponseData.NOTION_COMPLETION, "개념 완료")

            // ── 응용 학습 ──
            path.contains("/applications") && !path.contains("/completions") && method == "GET" -> {
                val res = when (language.uppercase()) {
                    "PYTHON" -> MockResponseData.PRACTICE_QUIZZES_PYTHON
                    "C++", "CPP" -> MockResponseData.PRACTICE_QUIZZES_CPP
                    "JAVASCRIPT" -> MockResponseData.PRACTICE_QUIZZES_JAVASCRIPT
                    else -> MockResponseData.PRACTICE_QUIZZES
                }
                Pair(res, "응용 학습 ($language)")
            }

            // ── 응용 학습 완료 ──
            path.contains("/applications/completions") && method == "POST" -> Pair(MockResponseData.PRACTICE_COMPLETION, "응용 완료")

            // ── 문제 학습 목록 (주제별) ──
            path.endsWith("/problems") && path.contains("/topics/") && method == "GET" -> Pair(MockResponseData.PROBLEM_LIST, "주제별 문제 목록")

            // ── 전체 문제 목록 ──
            path.endsWith("/problems") && !path.contains("/topics/") && method == "GET" -> Pair(MockResponseData.PROBLEM_LIST, "전체 문제 목록")

            // ── 문제별 사용자 제출 기록 조회 ──
            path.contains("/problems/") && path.contains("/histories") && method == "GET" -> Pair(MockResponseData.PROBLEM_HISTORIES, "제출 기록 조회")

            // ── 문제 학습 상세 ──
            path.contains("/problems/") && !path.contains("/runs") && !path.contains("/submissions") && !path.contains("/solutions") && !path.contains("/histories") && method == "GET" -> {
                val idStr = path.substringAfter("/problems/").substringBefore("/").substringBefore("?")
                val res = when {
                    idStr == "1" && language.uppercase() == "PYTHON" -> MockResponseData.PROBLEM_DETAIL_1_PYTHON
                    idStr == "1" && (language.uppercase() == "C++" || language.uppercase() == "CPP") -> MockResponseData.PROBLEM_DETAIL_1_CPP
                    idStr == "1" && language.uppercase() == "JAVASCRIPT" -> MockResponseData.PROBLEM_DETAIL_1_JAVASCRIPT
                    idStr == "1" -> MockResponseData.PROBLEM_DETAIL_1
                    language.uppercase() == "PYTHON" -> MockResponseData.PROBLEM_DETAIL_DEFAULT_PYTHON
                    language.uppercase() == "C++" || language.uppercase() == "CPP" -> MockResponseData.PROBLEM_DETAIL_DEFAULT_CPP
                    language.uppercase() == "JAVASCRIPT" -> MockResponseData.PROBLEM_DETAIL_DEFAULT_JAVASCRIPT
                    else -> MockResponseData.PROBLEM_DETAIL_DEFAULT
                }
                Pair(res, "문제 상세 ($language)")
            }

            // ── 코드 실행 및 제출 ──
            path.contains("/runs") && method == "POST" -> Pair(MockResponseData.PROBLEM_RUN, "코드 실행")
            path.contains("/runs") && path.contains("/results") && method == "GET" -> Pair(MockResponseData.PROBLEM_RUN_RESULT, "코드 실행 결과 조회")
            path.contains("/submissions") && method == "POST" -> Pair(MockResponseData.PROBLEM_SUBMIT, "코드 제출 결과")
            path.contains("/submissions") && path.contains("/results") && method == "GET" -> Pair(MockResponseData.PROBLEM_SUBMISSION_RESULT, "코드 채점 결과 조회")
            
            // ── 모범 답안 (언어별 분기 추가) ──
            path.contains("/solutions") && method == "GET" -> {
                val res = when (language.uppercase()) {
                    "PYTHON" -> MockResponseData.PROBLEM_SOLUTION_PYTHON
                    "C++", "CPP" -> MockResponseData.PROBLEM_SOLUTION_CPP
                    "JAVASCRIPT" -> MockResponseData.PROBLEM_SOLUTION_JAVASCRIPT
                    else -> MockResponseData.PROBLEM_SOLUTION
                }
                Pair(res, "모범 답안 ($language)")
            }

            // ── 언어 목록 ──
            path.contains("/languages") && method == "GET" -> Pair(MockResponseData.LANGUAGES, "언어 목록")
            
            // ── 찜(북마크) ──
            path.contains("/bookmarks/problems") && method == "GET" -> Pair(MockResponseData.BOOKMARK_LIST, "찜 목록 조회")
            path.contains("/bookmarks/problems") && method == "POST" -> Pair(MockResponseData.BOOKMARK_ADD_SUCCESS, "찜 추가")
            path.contains("/bookmarks/problems") && method == "DELETE" -> Pair(MockResponseData.BOOKMARK_DELETE_SUCCESS, "찜 삭제")

            else -> Pair(MockResponseData.DEFAULT_SUCCESS, "기본 성공 응답")
        }
    }

    private fun extractJsonValue(json: String, key: String): String? {
        val regex = """"$key"\s*:\s*"([^"]*)"""".toRegex()
        return regex.find(json)?.groupValues?.getOrNull(1)
    }
}