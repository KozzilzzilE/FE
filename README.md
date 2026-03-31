# API Specifications


## 1. 인증 (Authentication)

### 회원가입 (Sign Up)
- **Method**: `POST`
- **URL**: `/api/v1/auths/signup`
- **Request Body**:
  ```json
  { 
    "firebaseToken" : "String", 
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
      "userId": 5, 
      "email": "String", 
      "nickname": "String", 
      "language": "String" 
    } 
  }
  ```

  ### 로그인 (Login)
- **Method**: `POST`
- **URL**: `/api/v1/auths/login`
- **Request Body**:
  ```json
  { "firebaseToken" : "String" }
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

---

## 2. 사용자

### 메인 화면 정보 조회
- **Method**: `GET`
- **URL**: `/api/v1/users/main`
- **Header**: `Authorization: Bearer {accessToken}`
- **Response Body**:
  ```json
  {
    "isSuccess": true,
    "code": "USER_200",
    "message": "메인 화면 정보 조회에 성공했습니다.",
    "result": {
      "name": "String",
      "languageId": "Long",
      "languageName": "String"
    }
  }
  ```

---

## 3. 주제 목록 

### 알고리즘 주제 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/topics`
- **Header**: `Authorization: Bearer {accessToken}`
- **Response Body**:
  ```json
  {
    "isSuccess": true,
    "code": "TOPIC_200",
    "message": "알고리즘 목록 조회 성공",
    "result": {
      "count": 12,
      "topics": [
        { "topicId" : 1, "name" : "HASH", "displayName" : "해시" },
        ...
      ]
    }
  }
  ```

## 4. 개념 학습

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
          "notionId" : 1,
          "pageNo" : 1,
          "title" : "String",
          "point" : "String",
          "detail" : "String",
          "imgUrl" : "String (Nullable)",
          "exampleCode" : {
            "language" : "String",
            "content" : "String"
          },
          "notionCompleted" : true
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
    "message": "개념 학습 완료로 변경 성공",
    "result": {
      "notionId": 1,
      "userName": "사용자",
      "notionCompleted": true
    }
  }
  ```

---

## 5. 응용 학습

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
      "count": 1,
      "appliedExercises": [
        {
          "exerciseId": 101,
          "title": "String",
          "description": "String",
          "codeTemplate": "String",
          "appliedCompleted": false,
          "totalBlanks": 5,
          "blanks": [
            { "content": "String", "answer": 1 },
            { "content": "String", "answer": null }
          ]
        }
      ]
    }
  }
  ```

### 응용 학습 완료 처리
- **Method**: `POST`
- **URL**: `/api/v1/learnings/applications/completions/{exerciseId}`
- **Header**: `Authorization: Bearer {accessToken}`
- **Response Body**:
  ```json
  {
    "isSuccess": true,
    "code": "LEARNING_203",
    "message": "응용 학습 완료로 변경 성공",
    "result": {
      "exerciseId": 1,
      "userName": "사용자",
      "appliedCompleted": true
    }
  }
  ```

---

## 6. 문제 학습

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
      "topicId" : 1,
      "count" : 5,
      "problems": [
        {
          "problemId" : 1,
          "title" : "String",
          "difficulty" : "String",
          "difficultyDisplayName" : "String"
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
      "exerciseId": 2,
      "title": "두 수의 합 (Two Sum)",
      "description": "정수 배열 nums와 정수 target이 주어졌을 때...",
      "constraint": "2 ≤ nums.length ≤ 10^4...",
      "testCases": [
        {
          "input": "2 7 11 15 9",
          "output": "0 1"
        }
      ],
      "isCompleted": false
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
      "lineSolution" : "해시맵을 사용하여 O(n) 시간 복잡도로 해결할 수 있습니다.",
      "solutionText": "1. 핵심아이디어 : ...\n2. 시간 복잡도 : ...\n3. 공간복잡도 : ...",
      "language" : "JAVA",
      "solutionCode" : "import java.util.*;\n\nclass Solution { ... }"
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
  
  ### 코드 실행 요청
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
  
---

## 7. 기타 (Etc)

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
      "count" : 3,
      "languages" : [
        { "languageId" : 1, "name" : "JAVA" },
        { "languageId" : 2, "name" : "C++" },
        { "languageId" : 3, "name" : "PYTHON" }
      ]
    }
  }
  ```
---
