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
    // [인증] POST /api/v1/auths/login
    // ─────────────────────────────────────────────
    val LOGIN = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "accessToken": "mock_access_token_PocketCo_2026",
            "nickname": "이성규",
            "language": "JAVA"
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
    val HOME = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "name": "이성규",
            "languageId": 1,
            "languageName": "JAVA"
        }
    }
    """.trimIndent()

    // ─────────────────────────────────────────────
    // [주제 목록] GET /api/v1/topics
    // 10개 이상의 알고리즘 주제 데이터
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
    // 10개 이상의 개념 학습 페이지 데이터 (해시 기준 샘플)
    // ─────────────────────────────────────────────
    val NOTIONS = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "성공입니다.",
        "result": {
            "topicId": 1,
            "count": 10,
            "notions": [
                {
                    "notionId": 1,
                    "pageNo": 1,
                    "title": "해시(Hash)란 무엇인가?",
                    "point": "해시는 데이터를 고정된 크기의 값으로 변환하는 함수입니다.",
                    "detail": "해시 함수(Hash Function)는 임의의 길이의 데이터를 고정된 길이의 해시 값으로 매핑하는 함수입니다. 해시 테이블(Hash Table)은 이 해시 함수를 사용하여 키(Key)를 값(Value)에 매핑하는 자료구조입니다. 평균적으로 O(1)의 시간 복잡도로 데이터를 검색, 삽입, 삭제할 수 있어 매우 효율적입니다.",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "import java.util.HashMap;\n\npublic class HashExample {\n    public static void main(String[] args) {\n        HashMap<String, Integer> map = new HashMap<>();\n        map.put(\"apple\", 1);\n        map.put(\"banana\", 2);\n        System.out.println(map.get(\"apple\")); // 1\n    }\n}"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 2,
                    "pageNo": 2,
                    "title": "해시 함수의 특징",
                    "point": "좋은 해시 함수는 충돌을 최소화하고 균등하게 분배합니다.",
                    "detail": "해시 함수는 다음과 같은 특징을 가져야 합니다:\n1. 결정성(Deterministic): 같은 입력에 대해 항상 같은 출력\n2. 균등 분포(Uniform Distribution): 해시 값이 골고루 분포\n3. 효율성(Efficiency): 빠른 계산 속도\n4. 최소 충돌(Minimum Collision): 서로 다른 입력이 같은 해시 값을 갖는 경우 최소화",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "// 간단한 해시 함수 구현 예제\npublic int simpleHash(String key, int tableSize) {\n    int hash = 0;\n    for (char c : key.toCharArray()) {\n        hash += c;\n    }\n    return hash % tableSize;\n}"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 3,
                    "pageNo": 3,
                    "title": "해시 충돌(Collision)",
                    "point": "서로 다른 키가 같은 해시 값을 가질 때 충돌이 발생합니다.",
                    "detail": "해시 충돌은 두 개 이상의 서로 다른 키가 동일한 해시 값으로 매핑되는 현상입니다. 비둘기집 원리(Pigeonhole Principle)에 의해 입력 데이터의 수가 해시 테이블의 크기보다 크면 충돌은 반드시 발생합니다. 이를 해결하는 대표적인 방법으로 체이닝(Chaining)과 개방 주소법(Open Addressing)이 있습니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 4,
                    "pageNo": 4,
                    "title": "체이닝(Chaining) 기법",
                    "point": "같은 버킷에 연결 리스트로 데이터를 저장하는 방식입니다.",
                    "detail": "체이닝(Separate Chaining)은 해시 충돌이 발생했을 때, 같은 해시 값을 가진 데이터들을 연결 리스트(Linked List)로 연결하여 저장하는 방법입니다. 구현이 간단하고 해시 테이블의 적재율(Load Factor)이 높아져도 성능 저하가 비교적 완만합니다.",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "// 체이닝 방식의 해시맵 개념\nclass ChainedHashMap {\n    LinkedList<Entry>[] buckets;\n    \n    void put(String key, int value) {\n        int idx = key.hashCode() % buckets.length;\n        buckets[idx].add(new Entry(key, value));\n    }\n}"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 5,
                    "pageNo": 5,
                    "title": "개방 주소법(Open Addressing)",
                    "point": "충돌 시 다른 빈 버킷을 탐색하여 데이터를 저장합니다.",
                    "detail": "개방 주소법은 충돌이 발생하면 해시 테이블 내의 다른 빈 슬롯을 찾아 데이터를 저장하는 방식입니다. 대표적으로 선형 탐사(Linear Probing), 이차 탐사(Quadratic Probing), 이중 해싱(Double Hashing) 등이 있습니다. 메모리 사용이 효율적이지만 클러스터링 문제가 발생할 수 있습니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 6,
                    "pageNo": 6,
                    "title": "Java에서의 HashMap",
                    "point": "Java의 HashMap은 체이닝 + 트리를 혼합하여 사용합니다.",
                    "detail": "Java 8부터 HashMap은 하나의 버킷에 8개 이상의 노드가 쌓이면 연결 리스트를 레드-블랙 트리(Red-Black Tree)로 변환합니다. 이를 통해 최악의 경우에도 O(log n)의 검색 성능을 보장합니다. 기본 초기 용량은 16이며, 적재율(Load Factor)이 0.75를 초과하면 자동으로 리사이징됩니다.",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "HashMap<String, Integer> scores = new HashMap<>();\nscores.put(\"Math\", 95);\nscores.put(\"English\", 88);\nscores.put(\"Science\", 92);\n\n// 값 조회 O(1)\nint mathScore = scores.get(\"Math\");\n\n// 키 존재 여부 확인\nif (scores.containsKey(\"English\")) {\n    System.out.println(\"영어 점수: \" + scores.get(\"English\"));\n}"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 7,
                    "pageNo": 7,
                    "title": "해시맵의 주요 메서드",
                    "point": "put, get, remove, containsKey 등 핵심 메서드를 익히세요.",
                    "detail": "HashMap의 자주 사용하는 메서드:\n- put(key, value): 키-값 쌍 추가\n- get(key): 키에 해당하는 값 조회\n- remove(key): 키에 해당하는 항목 삭제\n- containsKey(key): 키 존재 여부 확인\n- containsValue(value): 값 존재 여부 확인\n- size(): 저장된 항목 수 반환\n- keySet(): 모든 키의 Set 반환\n- values(): 모든 값의 Collection 반환\n- entrySet(): 모든 키-값 쌍의 Set 반환",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "HashMap<String, Integer> map = new HashMap<>();\nmap.put(\"A\", 1);\nmap.put(\"B\", 2);\nmap.put(\"C\", 3);\n\n// 순회 방법\nfor (Map.Entry<String, Integer> entry : map.entrySet()) {\n    System.out.println(entry.getKey() + \" = \" + entry.getValue());\n}\n\n// getOrDefault 활용\nint val = map.getOrDefault(\"D\", 0); // 없으면 0 반환"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 8,
                    "pageNo": 8,
                    "title": "해시 관련 대표 문제 유형",
                    "point": "해시는 빈도 수 세기, 중복 검사, 빠른 검색에 활용됩니다.",
                    "detail": "코딩 테스트에서 해시를 활용한 대표적인 문제 유형:\n1. 빈도 수 세기: 문자열의 각 문자가 몇 번 나오는지 세기\n2. 중복 검사: 배열 내 중복 원소 찾기\n3. 두 수의 합(Two Sum): 배열에서 합이 특정 값인 두 수 찾기\n4. 아나그램 검사: 두 문자열이 아나그램인지 확인\n5. 캐시 구현: LRU 캐시 등에 활용",
                    "imgUrl": null,
                    "exampleCode": {
                        "language": "JAVA",
                        "content": "// Two Sum 문제 - 해시 풀이 O(n)\npublic int[] twoSum(int[] nums, int target) {\n    HashMap<Integer, Integer> map = new HashMap<>();\n    for (int i = 0; i < nums.length; i++) {\n        int complement = target - nums[i];\n        if (map.containsKey(complement)) {\n            return new int[]{map.get(complement), i};\n        }\n        map.put(nums[i], i);\n    }\n    return new int[]{};\n}"
                    },
                    "notionCompleted": false
                },
                {
                    "notionId": 9,
                    "pageNo": 9,
                    "title": "해시의 시간 복잡도",
                    "point": "평균 O(1), 최악 O(n)의 시간 복잡도를 가집니다.",
                    "detail": "해시 테이블의 시간 복잡도:\n- 검색(Search): 평균 O(1), 최악 O(n)\n- 삽입(Insert): 평균 O(1), 최악 O(n)\n- 삭제(Delete): 평균 O(1), 최악 O(n)\n\n최악의 경우는 모든 키가 같은 버킷에 매핑되어 연결 리스트처럼 동작할 때 발생합니다. 좋은 해시 함수를 사용하면 평균적으로 O(1)을 유지할 수 있습니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 10,
                    "pageNo": 10,
                    "title": "해시 정리 및 마무리",
                    "point": "해시는 빠른 검색이 필요할 때 가장 먼저 고려하세요.",
                    "detail": "해시 자료구조 핵심 정리:\n1. 키-값 쌍으로 데이터를 저장하는 자료구조\n2. 평균 O(1)의 빠른 검색/삽입/삭제\n3. 충돌 해결법: 체이닝, 개방 주소법\n4. Java에서는 HashMap, HashSet 활용\n5. 코딩 테스트에서 빈도 수 세기, 중복 검사, 빠른 탐색에 필수\n\n이것으로 해시 기본 개념 학습을 마칩니다. 다음은 응용학습에서 실전 연습을 해보세요!",
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
    // [기본 성공 응답] 매칭되지 않는 요청의 기본 응답
    // ─────────────────────────────────────────────
    val DEFAULT_SUCCESS = """
    {
        "isSuccess": true,
        "code": "COMMON200",
        "message": "Mock 기본 응답입니다."
    }
    """.trimIndent()
}
