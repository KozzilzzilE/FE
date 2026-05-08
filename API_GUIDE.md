# FE 프로젝트 API 연동 규칙 가이드라인

현재 구현된 기능들과 통신 레이어(`api/`, `data/dto/`) 코드를 분석하여, 앞으로 기능 개발 및 API 연동 시 지켜야 할 일관된 규칙을 정리했습니다.

---

## 1. 디렉토리 구조 및 역할

모든 기능(feature)은 아래와 같은 패키지 구조를 가져야 하며, 역할을 엄격하게 분리합니다.

```text
feature/
└── {기능명}/
    ├── data/
    │   └── {Feature}Repository.kt       // (Repository) API 호출 및 에러 핸들링
    ├── model/
    │   └── (필요시) 도메인 모델            // (Model) UI에서 사용할 순수 데이터 모델 (DTO와 분리할 경우)
    ├── component/
    │   └── ...                          // (UI) 재사용 가능한 UI 컴포넌트
    ├── ui/
    │   └── {Feature}Screen.kt           // (UI) 상태에 따른 뷰 렌더링
    └── {Feature}ViewModel.kt            // (ViewModel) 상태 관리 및 Repository 호출
```

---

## 2. 계층별 구현 규칙

### 2-1. Network & DTO 계층 (`app/src/main/java/com/example/fe/data/dto/`)
*   **위치**: `data/dto/` 디렉토리에 기능별로 묶어 저장 (예: `ConceptDto.kt`, `MyPageDto.kt`)
*   **Response Wrapper 패턴**: 서버의 공통 응답 구조(`isSuccess`, `code`, `message`, `result`)를 준수해야 합니다.
*   **어노테이션**: 변수명 변경이나 난독화 방지를 위해 모든 필드에 `@SerializedName`을 명시해야 합니다.
```kotlin
// 예시: data/dto/ExampleDto.kt
data class ExampleResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ExampleResult?
)

data class ExampleResult(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
```

### 2-2. API Service 계층 (`app/src/main/java/com/example/fe/api/ApiService.kt`)
*   **위치**: 모든 엔드포인트는 `ApiService.kt` 인터페이스에 정의합니다.
*   **반환형**: `Response<T>` 타입으로 감싸서 정의합니다.
*   **Header**: 인증이 필요한 API는 `@Header("Authorization") token: String`을 파라미터로 받습니다.
```kotlin
@GET("api/v1/examples")
suspend fun getExamples(
    @Header("Authorization") token: String,
    @Query("page") page: Int
): Response<ExampleResponse>
```

### 2-3. Repository 계층 (`feature/{이름}/data/{이름}Repository.kt`)
*   **목적**: API 서비스와 ViewModel 사이의 데이터 요청 및 1차적 예외 처리를 담당합니다.
*   **토큰 처리**: ViewModel에서 전달받은 토큰 문자열 앞에 `"Bearer "`를 붙여서 `ApiService`에 전달합니다.
*   **예외 처리**: 반드시 `try-catch`로 네트워크 예외를 잡고, `Log.e`로 에러를 로깅합니다. 실패 시 `null`이나 `false`를 반환합니다.
```kotlin
class ExampleRepository(private val apiService: ApiService) {
    suspend fun fetchExamples(token: String): ExampleResponse? {
        return try {
            val response = apiService.getExamples(token = "Bearer $token")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ExampleRepository", "API 에러: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ExampleRepository", "네트워크 예외: ${e.message}", e)
            null
        }
    }
}
```

### 2-4. ViewModel 계층 (`feature/{이름}/{이름}ViewModel.kt`)
*   **목적**: 비즈니스 로직 처리, 토큰 조회, UI 상태(`UiState`) 관리를 담당합니다.
*   **상태 관리**: `MutableStateFlow`를 사용하여 화면의 상태(로딩, 데이터, 에러 메세지 등)를 단일 객체로 관리합니다.
*   **토큰 조회**: `TokenManager.getAccessToken()`을 이용해 토큰을 조회하고 유효성을 검증합니다.
*   **동기화**: `viewModelScope.launch` 내에서 Repository 함수를 `suspend`로 호출합니다.
```kotlin
data class ExampleUiState(
    val isLoading: Boolean = false,
    val data: List<ExampleResult> = emptyList(),
    val error: String? = null
)

class ExampleViewModel(private val repository: ExampleRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ExampleUiState())
    val uiState = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val token = TokenManager.getAccessToken()
            if (token.isNullOrEmpty()) {
                _uiState.update { it.copy(isLoading = false, error = "로그인이 필요합니다.") }
                return@launch
            }
            
            val response = repository.fetchExamples(token)
            if (response != null && response.isSuccess) {
                _uiState.update { 
                    it.copy(isLoading = false, data = response.result ?: emptyList()) 
                }
            } else {
                _uiState.update { 
                    it.copy(isLoading = false, error = response?.message ?: "데이터 로드 실패") 
                }
            }
        }
    }
}
```

---

## 3. 요약 (체크리스트)
새로운 API 연동 기능을 만들 때 다음을 확인하세요.
- [ ] `api/ApiService.kt`에 엔드포인트를 추가했는가?
- [ ] `data/dto/` 폴더에 요청/응답 `data class`를 만들고 `@SerializedName`을 붙였는가?
- [ ] `feature/.../data/` 폴더에 `Repository`를 만들고 `try-catch`를 적용했는가? (Bearer 추가 잊지 않기)
- [ ] `ViewModel`에서 `UiState` (데이터, 로딩, 에러) 상태 관리를 `StateFlow`로 구현했는가?
- [ ] `UI (Screen)` 컴포넌트는 `ViewModel`의 상태(State)만을 구독하여 뷰를 업데이트하는가?
