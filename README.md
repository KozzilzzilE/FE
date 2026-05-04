  # API Specifications
  
  
  ## 1. 인증 (Authentication)

  ### 회원가입 (Sign Up)
  - **Method**: `POST`
  - **URL**: `/api/v1/auths/signup`
  - **Request Body**:
    ```json
    { 
      "firebaseToken": "String", 
      "email": "String", 
      "nickname": "String", 
      "language": "String" 
    }
    ```
  - **Response Body**:
   ```json
  { 
    "isSuccess": true, 
    "code": "AUTH_200", 
    "message": "회원가입이 완료되었습니다.", 
    "result": { 
      "userId": 8, 
      "email": "String", 
      "nickname": "String", 
      "language": null 
    } 
  }
  ```

  ### 로그인 (Login)
  - **Method**: `POST`
  - **URL**: `/api/v1/auths/login`
  - **Request Body**:
  ```json
  { "firebaseToken": "String" }
  ```
  - **Response Body**:
  ```json
    {
      "isSuccess": true,
      "code": "AUTH_201",
      "message": "로그인 성공",
      "result": {
        "accessToken": "String",
        "nickname": "String",
        "language": "String"
      }
    }
  ```

   ### 로그아웃 (Logout)
  - **Method**: `POST`
  - **URL**: `/api/v1/auths/logout`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
  ```json
  {
    "isSuccess": true,
    "code": "AUTH_302",
    "message": "로그아웃 성공했습니다.",
    "result": null
  }
  ```

---

  ## 2. 사용자 (User)

  ### 마이페이지 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/users/main`
  - ***Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "USER_200",
      "message": "메인 화면 정보 조회 성공했습니다.",
      "result": {
      "nickname": "String",
      "languageId": 2,
      "languageName": "JAVA",
      "totalSolvedDetails": [
        { "date": "2026-04-01", "count": 2 }
      ],
      "thisMonthSolvedCount": 2
    }
    }
    ```

  ### 사용자 언어 변경
  - **Method**: `PATCH`
  - **URL**: `/api/v1/users/me/languages`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Request Body**:
    ```json
    { "language": "JAVA" }
    ```
  - **Response Body**:
  ```json
    {
      "isSuccess": true,
      "code": "USER_201",
      "message": "메인 언어가 성공적으로 변경되었습니다.",
      "result": {
        "userId": 8,
        "nickname": "String",
        "language": "JAVA"
      }
    }
  ```

  ### 사용자 이름 변경
  - **Method**: `PATCH`
  - **URL**: `/api/v1/users/me/names`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Request Body**:
    ```json
    { "nickname": "새닉네임" }
    ```
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "USER_201",
      "message": "사용자 정보 변경이 완료되었습니다.",
      "result": {
      "userId": 8,
      "nickname": "새닉네임",
      "language": "JAVA"
    }
    }
    ```

    ---
    
  ## 3. 찜 (Bookmark)
  
  ### 찜 추가
  - **Method**: `POST`
  - **URL**: `/api/v1/bookmarks/problems/{problemId}`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "BOOKMARK_200",
      "message": "찜 추가 성공",
      "result": { "bookmarked": true }
    }
    ```
    
  ### 찜 삭제
  - **Method**: `DELETE`
  - **URL**: `/api/v1/bookmarks/problems/{problemId}`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "BOOKMARK_201",
      "message": "찜 삭제 성공",
      "result": { "bookmarked": false }
    }
    ```
    
  ### 찜 목록 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/bookmarks/problems`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "BOOKMARK_202",
      "message": "찜 목록 조회 성공",
      "result": [
        {
        "problemId": 5,
        "title": "문제 제목",
        "difficulty": "EASY",
        "difficultyDisplayName": "쉬움",
        "bookmarkCount": 1
      }
    ]
    }
    ```

    ---
    
  ## 4. 주제 목록
  
  ### 알고리즘 주제 목록 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/topics`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "TOPIC_200",
      "message": "알고리즘 목록 조회 성공",
      "result": {
        "count": 3,
        "topics": [
          { "topicId": 1, "name": "STACK", "displayName": "스택" }
        ]
      }
    }
    ```

    ---
    
  ## 5. 개념 학습
  
  ### 개념 학습 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/learnings/{topicId}/notions`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "LEARNING_200",
      "message": "개념 학습 조회 성공",
      "result": {
        "topicId": 1,
        "count": 3,
        "notions": [
          {
            "notionId": 7,
            "pageNo": 1,
            "title": "해시 함수",
            "point": "내용",
            "detail": "상세",
            "imgUrl": "String",
            "exampleCode": {
              "language": "JAVA",
              "content": "code..."
            },
            "notionCompleted": true
          }
        ]
      }
    }
    ```
    
  ### 개념 학습 완료 처리
  - **Method**: `POST`
  - **URL**: `/api/v1/learnings/notions/completions/{notionId}`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "LEARNING_201",
      "message": "개념 학습 완료 DB 변경 성공했습니다.",
      "result": {
        "notionId": 7,
        "userName": "String",
        "notionCompleted": true
      }
    }
    ```

    ---
    
  ## 6. 응용 학습
  
  ### 응용 학습 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/learnings/{topicId}/applications`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "LEARNING_202",
      "message": "응용 학습 조회 성공",
      "result": {
        "count": 3,
        "appliedExercises": [
          {
            "exerciseId": 2,
            "title": "문제",
            "description": "설명",
            "codeTemplate": "code",
            "appliedCompleted": false,
            "totalBlanks": 5,
            "blanks": [
              { "content": "보기", "answer": 1 }
            ]
          }
        ]
      }
    }
    ```
    
  #### 응용 학습 완료 처리
  - **Method**: `POST`
  - **URL**: `/api/v1/learnings/applications/completions/{exerciseId}`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "LEARNING_203",
      "message": "응용 학습 완료로 DB 변경 성공했습니다.",
      "result": {
        "exerciseId": 2,
        "userName": "String",
        "appliedCompleted": true
      }
    }
    ```

    ---
    
  ## 7. 문제 학습

  ### 문제 목록 조회 (특정 알고리즘 주제)
  - **Method**: `GET`
  - **URL**: `/api/v1/topics/{topicId}/problems`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "TOPIC_202",
      "message": "문제 목록 조회 성공",
      "result": {
        "topicId": 2,
        "count": 2,
        "problems": [
          {
            "problemId": 2,
            "title": "문제 제목",
            "difficulty": "EASY",
            "difficultyDisplayName": "쉬움",
            "isCompleted": true,
            "bookmarkCount": 3,
            "isBookmark": false
          }
        ]
      }
    }
    ```
    
  ### 문제 상세 정보 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/problems/{problemId}`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "PROBLEM_200",
      "message": "문제 상세 정보 조회 성공",
      "result": {
        "problemId": 2,
        "title": "문제 제목",
        "description": "문제 설명",
        "constraint": "제한 조건",
        "testCases": [
          {
            "input": "입력값",
            "output": "출력값"
          }
        ],
        "isCompleted": false,
        "bookmarkCount": 3,
        "isBookmark": false,
        "timeLimit": 0.5,
        "memoryLimit": 256000
      }
    }
    ```
    
  ### 모범 답안 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/problems/{problemId}/solutions`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "PROBLEM_201",
      "message": "모범 답안 조회 성공",
      "result": {
        "lineSolution": "해시맵을 사용하여 O(n) 시간 복잡도로 해결할 수 있습니다.",
        "solutionText": "1. 핵심 아이디어: ...\n2. 시간 복잡도: ...\n3. 공간 복잡도: ...",
        "language": "JAVA",
        "solutionCode": "import java.util.*;\nclass Solution { ... }"
      }
    }
    ```
    
  ### 코드 실행 요청
  - **Method**: `POST`
  - **URL**: `/api/v1/problems/{problemId}/runs`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "PROBLEM_202",
      "message": "문제 코드 실행 요청 성공",
      "result": [
        {
          "token": "12bfae4c-fe94-4db8-914b-3c66c248d22f"
        },
        {
          "token": "f8ae2eaa-9e59-4ae2-b10e-8fb5efe77f5e"
        }
      ]
    }
    ```
    
  ### 코드 실행 결과 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/problems/runs/{token}/results`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "PROBLEM_204",
      "message": "코드 실행 결과 조회 성공",
      "result": {
        "status": "SUCCESS",
        "output": "실행 결과",
        "error": null,
        "executionTime": 0.01,
        "memory": 128
      }
    }
    ```
    
  ### 코드 채점 요청
  - **Method**: `POST`
  - **URL**: `/api/v1/problems/{problemId}/submissions`
  - **Query Parameter**: `language=JAVA`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "PROBLEM_203",
      "message": "문제 코드 채점 요청 성공",
      "result": {
        "submissionId": "49e8d137-1885-41a3-ab69-5b31072dc38b"
      }
    }
    ```
    
  ### 코드 채점 결과 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/problems/submissions/{historyId}/results`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {  
      "isSuccess": true,
      "code": "PROBLEM_205",
      "message": "채점 결과 조회 성공",
      "result": {
        "status": "ACCEPTED",
        "score": 100,
        "executionTime": 0.02,
        "memory": 130,
        "failedTestCase": null
      }
    }
    ```

    ---
        
  ## 8. CS 문제

  #### CS 문제 랜덤 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/cs-problems/random`
  - **Query Parameter**: `count=5`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "CS_PROBLEM_200",
      "message": "CS 문제 랜덤 조회 성공",
      "result": [
        {
          "csProblemId": 1,
          "question": "질문 내용",
          "answer": true,
          "explanation": "해설 내용"
        }
      ]
    }
    ```

    ---
    
  ## 9. AI 코드 리뷰
  
  ### AI 코드 리뷰 요청
  - **Method**: `POST`
  - **URL**: `/api/v1/histories/{historyId}/ai-review`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "AI_CODE_REVIEW_200",
      "message": "AI 코드 리뷰 요청 성공",
      "result": {
        "historyId": 2,
        "aiStatus": "PROCESSING"
      }
    }
    ```
    
  ### AI 코드 리뷰 결과 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/histories/{historyId}/ai-review`
  - **Header**: `Authorization: Bearer {accessToken}`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "AI_CODE_REVIEW_201",
      "message": "AI 코드 리뷰 결과 조회 성공",
      "result": {
        "historyId": 2,
        "aiStatus": "ACCEPTED",
        "aiReview": "코드 리뷰 내용",
        "aiImprovement": "개선 사항",
        "aiCode": "개선된 코드"
      }
    }
    ```

    ---
    
  ## 10. 기타 (Etc)

  ### 언어 목록 조회
  - **Method**: `GET`
  - **URL**: `/api/v1/languages/lists`
  - **Response Body**:
    ```json
    {
      "isSuccess": true,
      "code": "LANGUAGE_200",
      "message": "언어 목록 조회 성공",
      "result": {
        "count": 2,
        "languages": [
          { "languageId": 2, "name": "JAVA" },
          { "languageId": 3, "name": "C++" }
        ]
      }
    }
    ```
    ---
    
