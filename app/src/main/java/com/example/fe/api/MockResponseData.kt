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
                    "detail": "### 💡 해시(Hash)의 정의\n\n**해시 함수(Hash Function)**는 임의의 길이를 갖는 문자열을 입력받아 고정된 길이의 **해시 값(Hash Value)**을 출력하는 함수입니다.\n\n---\n\n### 🚀 왜 해시를 사용할까요?\n\n일반적인 배열 검색은 **O(N)**의 시간이 걸리지만, 해시 테이블을 사용하면 해시 함수를 통해 데이터의 위치를 바로 찾을 수 있어 **평균 O(1)**의 압도적인 검색 속도를 자랑합니다.\n\n- **빠른 검사** : 데이터베이스, 캐싱 환경에서 자주 사용됩니다.\n- **보안/인증** : 비밀번호 암호화(SHA-256 등)에 필수적입니다.",
                    "imgUrl": "https://images.unsplash.com/photo-1555949963-ff9fe0c870eb?auto=format&fit=crop&q=80&w=800",
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
                    "detail": "### 🔍 해시 함수의 핵심 특성\n\n좋은 해시 함수는 다음과 같은 특성을 만족해야 합니다.\n\n- **결정성(Deterministic)** : 같은 입력에 대해 항상 같은 출력을 반환합니다.\n- **균등 분포(Uniform Distribution)** : 해시 값이 골고루 분포되어야 합니다.\n- **효율성(Efficiency)** : 빠른 계산 속도를 보장합니다.\n\n---\n\n### ⚠️ 해시 충돌 해결 방법\n\n| 방법 | 설명 | 장점 |\n|------|------|------|\n| **체이닝** | 같은 버킷에 연결 리스트로 저장 | 구현이 간단 |\n| **개방 주소법** | 빈 슬롯을 탐색하여 저장 | 메모리 효율적 |\n| **이중 해싱** | 두 번째 해시 함수로 탐색 | 클러스터링 방지 |\n\n---\n\n### 📊 복잡도 분석\n\n| 연산 | 평균 | 최악 |\n|------|------|------|\n| `검색` | **O(1)** | O(n) |\n| `삽입` | **O(1)** | O(n) |\n| `삭제` | **O(1)** | O(n) |",
                    "imgUrl": "https://images.unsplash.com/photo-1558494949-ef01091559ed?auto=format&fit=crop&q=80&w=800",
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
                    "detail": "### 💥 해시 충돌(Collision)이란?\n\n서로 다른 두 개의 입력 데이터가 **동일한 해시 값**을 가지게 되는 현상을 말합니다.\n\n> **비둘기집 원리(Pigeonhole Principle)**에 의해, 무한한 입력 데이터를 유한한 크기의 해시 테이블에 매핑할 때 충돌은 수학적으로 피할 수 없는 필연입니다!\n\n---\n\n### 🛡️ 해결 전략\n\n충돌을 완벽히 막을 수는 없지만, 다음과 같은 기법들로 훌륭하게 **우회**할 수 있습니다.\n\n1. **체이닝 (Chaining)** : 충돌난 데이터를 연결 리스트로 줄줄이 엮습니다.\n2. **개방 주소법 (Open Addressing)** : 충돌이 나면, 옆에 있는 다른 빈 방(버킷)을 배정합니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 4,
                    "pageNo": 4,
                    "title": "체이닝(Chaining) 기법",
                    "point": "같은 버킷에 연결 리스트로 데이터를 저장하는 방식입니다.",
                    "detail": "### 🔗 체이닝(Chaining) 기법\n\n가장 전통적이고 널리 쓰이는 충돌 해결법입니다.\n동일한 버킷에 여러 데이터가 들어오면, **연결 리스트(Linked List)** 구조로 이어 붙입니다.\n\n---\n\n### ✅ 장단점 분석\n\n| 요소 | 특징 |\n|------|-----|\n| **장점 1** | 해시 테이블이 꽉 차도 성능 저하가 비교적 완만합니다. (유연성) |\n| **장점 2** | 상대적으로 구현이 간단하고 직관적입니다. |\n| **단점 1** | 연결 리스트 저장을 위한 추가 메모리 포인터 공간이 필요합니다. |\n| **단점 2** | 리스트가 길어지면 최악의 경우 검색에 **O(n)**이 걸립니다. |",
                    "imgUrl": "https://images.unsplash.com/photo-1516259762381-22954d7d3ad2?auto=format&fit=crop&q=80&w=800",
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
                    "detail": "### 🚪 개방 주소법(Open Addressing)\n\n데이터를 저장할 때 지정된 해시 버킷이 이미 차있다면, 데이터 구조 내부의 **다른 비어있는 버킷**을 탐색하여 데이터를 빈 집에 넣는 방식입니다.\n\n---\n\n### 🎯 탐색 기법 종류\n\n- **선형 탐사(Linear Probing)** : 1칸씩(+1, +2, ...) 순차적으로 빈자리를 찾습니다.\n- **제곱 탐사(Quadratic Probing)** : 제곱수(+1, +4, +9...)만큼 건너뛰며 찾습니다.\n- **이중 해싱(Double Hashing)** : 충돌 시 보조 해시 함수를 한 번 더 사용하여 이동 폭을 결정합니다.\n\n> 체이닝과 달리 메모리를 추가 할당하지 않지만, 데이터가 뭉치는 **군집화(Clustering)** 문제가 발생할 수 있습니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 6,
                    "pageNo": 6,
                    "title": "Java에서의 HashMap",
                    "point": "Java의 HashMap은 체이닝 + 트리를 혼합하여 사용합니다.",
                    "detail": "### ☕️ Java의 HashMap 진화\n\nJava의 `HashMap`은 8버전부터 체이닝 아키텍처를 대폭 개선했습니다!\n\n---\n\n### 🔄 체이닝과 트리의 결합\n\n1. **초기** : 연결 리스트로 시작합니다.\n2. **변환** : 하나의 버킷에 **8개 이상의 데이터**가 쌓이면 성능 향상을 위해 **레드-블랙 트리(Red-Black Tree)**로 변환합니다.\n3. **역변환** : 데이터가 지워져서 6개 이하로 줄어들면 다시 **연결 리스트**로 되돌아갑니다.\n\n이를 통해 최악의 경우 검색 속도를 `O(N)`에서 **O(log N)**으로 획기적으로 방어했습니다.",
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
                    "detail": "### 🛠️ 핵심 메서드 완전 정복\n\n코딩 테스트에서 단골로 쓰이는 `HashMap` 메서드들입니다.\n\n| 메서드명 | 리턴 타입 | 설명 |\n|----------|-----------|------|\n| `put(K, V)` | `V` | 형태의 키-값 쌍을 저장합니다. |\n| `getOrDefault` | `V` | 키가 있으면 값을, 없으면 지정된 기본값을 반환합니다. |\n| `containsKey` | `boolean`| 맵에 특정 키가 존재하는지 조회합니다. |\n| `keySet()` | `Set<K>`  | 맵에 저장된 모든 **키(Key)** 묶음을 반환합니다. |\n\n> 💡 **Tip:** 자바에서 원소의 중복이나 빈도수를 셀 때는 `map.put(k, map.getOrDefault(k, 0) + 1)` 패턴을 통째로 외워두면 매우 유용합니다!",
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
                    "detail": "### 🏆 무적의 해시 패턴\n\n해시 맵은 시간 초과(Time out)가 걸리는 대부분의 문제에서 가장 강력한 무기가 됩니다.\n\n---\n\n### 🎯 주요 출제 테마\n\n1. **빈도수 세기** : 투표 결과 확인, 베스트셀러, 문자열 아나그램 판별\n2. **빠른 조회** : 배열을 2중 `for`문으로 돌면 시간 초과가 나는 상황에서 `O(1)`로 원소 포함 여부 확인\n3. **연관성 유지** : 이름과 번호, ID와 닉네임 등을 짝지어서 관리\n\n> 문제에 `빠른 탐색`, `빈도 세기`, `중복 방지` 키워드가 등장한다면 **1순위로 해시맵**을 떠올리세요!",
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
                    "detail": "### ⏱️ 시간 복잡도(Time Complexity) 정리\n\n데이터 양이 늘어나도 속도가 유지되는 것이 해시의 가장 큰 매력입니다.\n\n| 연산 타입 | 평균 복잡도 | 최악 복잡도 |\n|-----------|-----------|-----------|\n| 요소 **삽입** | O(1) | O(N) |\n| 요소 **삭제** | O(1) | O(N) |\n| 요소 **조회** | O(1) | O(N) |\n\n---\n\n### 🤔 왜 최악이 O(N)일까요?\n\n가장 운이 나빠서 **모든 저장 데이터가 한 버킷(칸)**에서 연쇄적으로 충돌하여 체이닝으로 엮이게 되면, 결국 연결 리스트를 처음부터 끝까지 다 타고 들어가야 하기 때문입니다.",
                    "imgUrl": null,
                    "exampleCode": null,
                    "notionCompleted": false
                },
                {
                    "notionId": 10,
                    "pageNo": 10,
                    "title": "해시 정리 및 마무리",
                    "point": "해시는 빠른 검색이 필요할 때 가장 먼저 고려하세요.",
                    "detail": "### 🎉 개념 완성!\n\n해시(Hash)의 핵심 이론을 모두 마스터하셨습니다.\n\n---\n\n### 📝 1분 핵심 요약 체크\n- [x] 해시 테이블은 임의의 원소를 **고정된 길이**로 맵핑한다.\n- [x] 정보는 **키-값(Key-Value)** 쌍으로 유일하게 다뤄진다.\n- [x] 충돌에 대비해 **체이닝**이나 **개방 주소법**을 쓴다.\n- [x] 배열이나 리스트와 달리 검색/수정 삭제가 무려 **O(1)**로 처리된다.\n\n> 이론 정복을 축하합니다! 이제 직접 문제를 풀면서 개념을 내 것으로 만들 차례입니다. 하단의 **'다음 단계로 이동'** 버튼을 통해 응용 학습으로 떠나봅시다!",
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
                    "codeTemplate": "Map<Character, Integer> map = new ____<>();\nfor (char c : str.toCharArray()) {\n    map.____(c, map.getOrDefault(c, 0) + 1);\n}\nreturn map;",
                    "appliedCompleted": false,
                    "totalBlanks": 2,
                    "blanks": [
                        { "content": "HashMap", "answer": 1 },
                        { "content": "put", "answer": 2 }
                    ]
                },
                {
                    "exerciseId": 2,
                    "title": "HashSet으로 중복 원소 제거",
                    "description": "정수 배열에서 중복된 원소를 제거하고 고유한 원소만 남기는 코드의 빈칸을 채워보세요.",
                    "codeTemplate": "Set<Integer> set = new ____<>();\nfor (int n : nums) {\n    set.____(n);\n}\nreturn new ArrayList<>(set);",
                    "appliedCompleted": false,
                    "totalBlanks": 2,
                    "blanks": [
                        { "content": "HashSet", "answer": 1 },
                        { "content": "add", "answer": 2 }
                    ]
                },
                {
                    "exerciseId": 3,
                    "title": "두 배열의 교집합 찾기",
                    "description": "두 배열에서 공통으로 존재하는 원소를 찾는 코드의 빈칸을 채워보세요.",
                    "codeTemplate": "Set<Integer> set = new HashSet<>();\nfor (int n : nums1) set.____(n);\nList<Integer> result = new ArrayList<>();\nfor (int n : nums2) {\n    if (set.____(n)) result.add(n);\n}\nreturn result;",
                    "appliedCompleted": true,
                    "totalBlanks": 2,
                    "blanks": [
                        { "content": "add", "answer": 1 },
                        { "content": "contains", "answer": 2 }
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
                    "level": "쉬움",
                    "category": "Hash",
                    "isSolved": true
                },
                {
                    "problemId": 2,
                    "title": "올바른 괄호",
                    "level": "보통",
                    "category": "Stack",
                    "isSolved": false
                },
                {
                    "problemId": 3,
                    "title": "프로세스",
                    "level": "어려움",
                    "category": "Queue",
                    "isSolved": false
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
            "description": "정수 배열 nums와 정수 target이 주어집니다. 합이 target이 되는 두 수의 인덱스를 반환하세요.\n각 입력에는 정확히 하나의 해답이 있으며, 같은 원소를 두 번 사용할 수 없습니다.",
            "constraint": "2 ≤ nums.length ≤ 10⁴\n-10⁹ ≤ nums[i] ≤ 10⁹\n-10⁹ ≤ target ≤ 10⁹\n유효한 답이 하나 이상 존재합니다.",
            "testCases": [
                { "input": "nums = [2, 7, 11, 15], target = 9", "output": "[0, 1]" },
                { "input": "nums = [3, 2, 4], target = 6", "output": "[1, 2]" },
                { "input": "nums = [3, 3], target = 6", "output": "[0, 1]" }
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
            "description": "'(' 와 ')' 로만 이루어진 문자열 s가 주어집니다. s가 올바른 괄호이면 true를, 올바르지 않으면 false를 반환하세요.",
            "constraint": "0 < s.length ≤ 100000",
            "testCases": [
                { "input": "s = \"(())()\"", "output": "true" },
                { "input": "s = \")()(\"", "output": "false" }
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
            "solutionCode": "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) return new int[]{ map.get(complement), i };\n            map.put(nums[i], i);\n        }\n        return new int[]{};\n    }\n}",
            "solutionText": "해시맵을 사용하면 O(n)으로 해결할 수 있습니다.\n\n각 숫자를 순회하면서, `target - 현재값`이 이미 맵에 있는지 확인합니다.\n있다면 그 인덱스와 현재 인덱스를 반환합니다.",
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
}
