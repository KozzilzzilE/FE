package com.example.fe.api

/**
 * ============================================================
 * [MOCK 데이터] 가짜 API 응답 JSON 저장소
 * ============================================================
 *
 * MockInterceptor가 API 요청을 가로챌 때 이 파일에서
 * 해당 엔드포인트의 JSON 문자열을 꺼내 응답으로 반환합니다.
 *
 * ★ 실제 서버 연동 시: 이 파일은 수정하거나 삭제할 필요 없습니다.
 *   MockConfig.USE_MOCK = false 로 바꾸면 이 파일은 참조되지 않습니다.
 *
 * ★ 데이터 수정: 아래 JSON 문자열만 수정하면 앱에 즉시 반영됩니다.
 */
object MockResponseData {

    // ─────────────────────────────────────────────
    // [MOCK 상태값] 이름/언어 변경 시 메모리에 유지되는 값
    // ─────────────────────────────────────────────
    var currentNickname: String = "이성규"
    var currentLanguage: String = "JAVA"

    // ─────────────────────────────────────────────
    // [인증] POST /api/v1/auths/login
    // ─────────────────────────────────────────────
    fun getLogin(): String = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "accessToken": "mock_access_token_PocketCo_2026",
            "nickname": "$currentNickname",
            "language": "$currentLanguage"
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [인증] POST /api/v1/auths/signup
    // ─────────────────────────────────────────────
    val SIGN_UP = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "회원가입이 완료되었습니다.",
        "result": {
            "userId": 1,
            "email": "mock@test.com",
            "nickname": "테스터",
            "language": "JAVA"
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [메인 홈] GET /api/v1/users/main
    // ─────────────────────────────────────────────
    fun getHome(): String = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "name": "$currentNickname",
            "languageId": 1,
            "languageName": "$currentLanguage"
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [마이페이지 조회] GET /api/v1/users/main
    // ─────────────────────────────────────────────
    fun getMyPage(): String = """
    {
        "isSuccess": true,
        "code": "USER_200",
        "message": "메인 화면 정보 조회 성공했습니다.",
        "result": {
            "nickname": "$currentNickname",
            "languageId": 1,
            "languageName": "$currentLanguage"
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [주제 목록] GET /api/v1/topics
    // ─────────────────────────────────────────────
    val TOPICS = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "count": 12,
            "topics": [
                { "topicId": 1, "name": "Hash", "displayName": "해시 (Hash)" },
                { "topicId": 2, "name": "StackQueue", "displayName": "스택/큐 (Stack/Queue)" },
                { "topicId": 3, "name": "Heap", "displayName": "힙 (Heap)" },
                { "topicId": 4, "name": "Sorting", "displayName": "정렬 (Sorting)" },
                { "topicId": 5, "name": "BruteForce", "displayName": "완전탐색 (Brute Force)" },
                { "topicId": 6, "name": "Greedy", "displayName": "탐욕법 (Greedy)" },
                { "topicId": 7, "name": "DynamicProgramming", "displayName": "동적계획법 (DP)" },
                { "topicId": 8, "name": "DFS_BFS", "displayName": "깊이/너비 우선탐색 (DFS/BFS)" },
                { "topicId": 9, "name": "BinarySearch", "displayName": "이분탐색 (Binary Search)" },
                { "topicId": 10, "name": "Graph", "displayName": "그래프 (Graph)" },
                { "topicId": 11, "name": "LinkedList", "displayName": "연결 리스트 (Linked List)" },
                { "topicId": 12, "name": "Tree", "displayName": "트리 (Tree)" }
            ]
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [개념 학습] GET /api/v1/learnings/{topicId}/notions
    // ─────────────────────────────────────────────
    val NOTIONS = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "topicId": 1,
            "count": 2,
            "notions": [
                {
                    "notionId": 1,
                    "pageNo": 1,
                    "title": "해시(Hash)란 무엇인가?",
                    "point": "해시는 데이터를 고정된 크기의 값으로 변환하는 함수입니다.",
                    "detail": "해시는 빠른 탐색을 위한 핵심 자료구조입니다.",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "HashMap<String, Integer> map = new HashMap<>();"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 2,
                    "pageNo": 2,
                    "title": "해시 함수의 특징",
                    "point": "좋은 해시 함수는 충돌을 최소화합니다.",
                    "detail": "좋은 해시 함수는 빠르고 균등하게 분산합니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                }
            ]
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [개념 학습 완료] POST /api/v1/learnings/notions/completions/{notionId}
    // ─────────────────────────────────────────────
    val NOTION_COMPLETION = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "학습 완료 처리되었습니다.",
        "result": {
            "notionId": 1,
            "userName": "이성규",
            "notionCompleted": true
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [언어 목록] GET /api/v1/languages/lists
    // ─────────────────────────────────────────────
    val LANGUAGES = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "count": 4,
            "languages": [
                { "languageId": 1, "name": "JAVA" },
                { "languageId": 2, "name": "PYTHON" },
                { "languageId": 3, "name": "C++" },
                { "languageId": 4, "name": "JAVASCRIPT" }
            ]
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [사용자 메인 언어 변경] PATCH /api/v1/users/me/languages
    // ─────────────────────────────────────────────
    fun updateLanguage(language: String): String {
        currentLanguage = language
        return """
        {
            "isSuccess": true,
            "code": "USER_201",
            "message": "메인 언어가 성공적으로 변경되었습니다.",
            "result": {
                "userId": 1,
                "email": "mock@test.com",
                "nickname": "$currentNickname",
                "language": "$currentLanguage"
            }
        }
        """.trimIndent()
    }

    // ─────────────────────────────────────────────
    // [사용자 이름 변경] PATCH /api/v1/users/me/names
    // ─────────────────────────────────────────────
    fun updateNickname(nickname: String): String {
        currentNickname = nickname
        return """
        {
            "isSuccess": true,
            "code": "USER_201",
            "message": "사용자 정보 변경이 완료되었습니다.",
            "result": {
                "userId": 1,
                "email": "mock@test.com",
                "nickname": "$currentNickname",
                "language": "$currentLanguage"
            }
        }
        """.trimIndent()
    }

    // ─────────────────────────────────────────────
    // [응용 학습 목록] GET /api/v1/learnings/{topicId}/applications
    // ─────────────────────────────────────────────
    val PRACTICE_QUIZZES = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "count": 3,
            "appliedExercises": [
                {
                    "exerciseId": 1,
                    "title": "HashMap으로 문자 빈도 세기",
                    "description": "문자열에서 각 문자가 몇 번 등장하는지 HashMap으로 세는 코드의 빈칸을 채워보세요.",
                    "codeTemplate": "Map<Character, Integer> map = new ____<>();",
                    "appliedCompleted": false,
                    "totalBlanks": 1,
                    "blanks": [
                        { "content": "HashMap", "answer": 1 }
                    ]
                }
            ]
        }
    }
    """.trimIndent()

    val PRACTICE_COMPLETION = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "exerciseId": 1,
            "userName": "이성규",
            "appliedCompleted": true
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [문제 학습 목록] GET /api/v1/topics/{topicId}/problems
    // ─────────────────────────────────────────────
    val PROBLEM_LIST = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "topicId": 1,
            "count": 3,
            "problems": [
                {
                    "problemId": 1,
                    "title": "두 수의 합",
                    "difficulty": "EASY",
                    "difficultyDisplayName": "쉬움"
                },
                {
                    "problemId": 2,
                    "title": "올바른 괄호",
                    "difficulty": "MEDIUM",
                    "difficultyDisplayName": "보통"
                },
                {
                    "problemId": 3,
                    "title": "프로세스",
                    "difficulty": "HARD",
                    "difficultyDisplayName": "어려움"
                }
            ]
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [문제 학습 상세] GET /api/v1/problems/{problemId}
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_1 = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "exerciseId": 1,
            "title": "두 수의 합",
            "description": "정수 배열 nums와 정수 target이 주어집니다.",
            "constraint": "2 ≤ nums.length ≤ 10⁴",
            "testCases": [
                { "input": "nums = [2, 7, 11, 15], target = 9", "output": "[0, 1]" }
            ]
        }
    }
    """.trimIndent()

    val PROBLEM_DETAIL_DEFAULT = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "exerciseId": 2,
            "title": "올바른 괄호 (Mock Data)",
            "description": "괄호 문자열이 올바른지 판별하세요.",
            "constraint": "0 < s.length ≤ 100000",
            "testCases": [
                { "input": "s = \"(())()\"", "output": "true" }
            ]
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [코드 실행] POST /api/v1/problems/{problemId}/runs
    // ─────────────────────────────────────────────
    val PROBLEM_RUN = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": [
            { "token": "mock_test_token_1" }
        ]
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [코드 제출] POST /api/v1/problems/{problemId}/submissions
    // ─────────────────────────────────────────────
    val PROBLEM_SUBMIT = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "submissionId": 999
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [모범 답안] GET /api/v1/problems/{problemId}/solutions
    // ─────────────────────────────────────────────
    val PROBLEM_SOLUTION = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "exerciseId": 1,
            "languageId": 1,
            "solutionCode": "class Solution { }",
            "solutionText": "해시맵을 사용하면 O(n)으로 해결할 수 있습니다.",
            "lineSolution": "12-18 라인을 참고하세요."
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [기본 성공 응답] 매칭되지 않는 요청의 기본 응답
    // ─────────────────────────────────────────────
    val DEFAULT_SUCCESS = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "Mock 기본 응답입니다."
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [개념 학습 - PYTHON 버전] (추가)
    // ─────────────────────────────────────────────
    val NOTIONS_PYTHON = NOTIONS

    // ─────────────────────────────────────────────
    // [응용 학습 - PYTHON 버전] (추가)
    // ─────────────────────────────────────────────
    val PRACTICE_QUIZZES_PYTHON = PRACTICE_QUIZZES

    // ─────────────────────────────────────────────
    // [문제 상세 - PYTHON 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_1_PYTHON = PROBLEM_DETAIL_1

    // ─────────────────────────────────────────────
    // [모범 답안 - PYTHON 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_SOLUTION_PYTHON = PROBLEM_SOLUTION

    // ─────────────────────────────────────────────
    // [개념 학습 - C++ 버전] (추가)
    // ─────────────────────────────────────────────
    val NOTIONS_CPP = NOTIONS

    // ─────────────────────────────────────────────
    // [개념 학습 - JAVASCRIPT 버전] (추가)
    // ─────────────────────────────────────────────
    val NOTIONS_JAVASCRIPT = NOTIONS

    // ─────────────────────────────────────────────
    // [응용 학습 - C++ 버전] (추가)
    // ─────────────────────────────────────────────
    val PRACTICE_QUIZZES_CPP = PRACTICE_QUIZZES

    // ─────────────────────────────────────────────
    // [응용 학습 - JAVASCRIPT 버전] (추가)
    // ─────────────────────────────────────────────
    val PRACTICE_QUIZZES_JAVASCRIPT = PRACTICE_QUIZZES

    // ─────────────────────────────────────────────
    // [문제 상세 - C++ 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_1_CPP = PROBLEM_DETAIL_1

    // ─────────────────────────────────────────────
    // [문제 상세 - JAVASCRIPT 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_1_JAVASCRIPT = PROBLEM_DETAIL_1

    // ─────────────────────────────────────────────
    // [기본 문제 상세 - PYTHON 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_DEFAULT_PYTHON = PROBLEM_DETAIL_DEFAULT

    // ─────────────────────────────────────────────
    // [기본 문제 상세 - C++ 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_DEFAULT_CPP = PROBLEM_DETAIL_DEFAULT

    // ─────────────────────────────────────────────
    // [기본 문제 상세 - JAVASCRIPT 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_DETAIL_DEFAULT_JAVASCRIPT = PROBLEM_DETAIL_DEFAULT

    // ─────────────────────────────────────────────
    // [모범 답안 - C++ 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_SOLUTION_CPP = PROBLEM_SOLUTION

    // ─────────────────────────────────────────────
    // [모범 답안 - JAVASCRIPT 버전] (추가)
    // ─────────────────────────────────────────────
    val PROBLEM_SOLUTION_JAVASCRIPT = PROBLEM_SOLUTION
}