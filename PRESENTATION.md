# POCKETCO — 발표 자료 (프론트엔드)

> 알고리즘 학습의 모든 단계를 하나의 앱에서 — 개념부터 AI 피드백까지

---

## 목차

1. [서비스 소개](#1-서비스-소개)
2. [핵심 기능](#2-핵심-기능)
3. [기술 스택 & 아키텍처](#3-기술-스택--아키텍처)
4. [주요 구현 포인트](#4-주요-구현-포인트)
5. [차별점](#5-차별점)
6. [발표 흐름 (5분)](#6-발표-흐름-5분)

---

## 1. 서비스 소개

**POCKETCO**는 알고리즘을 처음 배우는 학생부터 코딩 테스트를 준비하는 취준생까지,  
학습의 전 단계를 하나의 앱 안에서 끊김 없이 경험할 수 있는 모바일 학습 플랫폼입니다.

### 타겟 사용자
- 알고리즘 개념이 부족한 CS 입문자
- 코딩 테스트를 준비 중인 취업 준비생
- 꾸준한 학습 루틴이 필요한 개발자 지망생

---

## 2. 핵심 기능

### 학습 플로우 (4단계)

```
개념 학습 → 응용 학습 → 실전 문제 풀이 → AI 코드 리뷰
```

| 단계 | 기능 | 설명 |
|------|------|------|
| 1 | **개념 학습** | 슬라이드 형식으로 알고리즘 개념 학습 |
| 2 | **응용 학습** | 빈칸 채우기 문제로 개념 확인 |
| 3 | **실전 문제** | 코드 에디터로 직접 코드 작성 & 제출 |
| 4 | **AI 코드 리뷰** | 제출한 코드에 대한 AI 피드백 제공 |

### 부가 기능

- **CS 퀴즈** — OX 형식의 CS 지식 퀴즈
- **학습 통계** — GitHub 스타일 잔디 그래프, 연속 학습 스트릭
- **즐겨찾기** — 문제 북마크 및 모아보기
- **제출 기록** — 전체 제출 이력 및 정답/오답 확인

---

## 3. 기술 스택 & 아키텍처

### 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 | Kotlin |
| UI | Jetpack Compose |
| 아키텍처 | MVVM (ViewModel + StateFlow) |
| 네트워크 | Retrofit2 + OkHttp |
| 비동기 | Kotlin Coroutines |
| 코드 에디터 | Sora Editor |
| 이미지 로딩 | Coil |
| 인증 | Google OAuth (소셜 로그인) |

### 아키텍처 구조

```
UI Layer (Composable Screen)
    │
    ▼
ViewModel (StateFlow, UiState)
    │
    ▼
Repository
    │
    ▼
Retrofit API Service
```

- **단방향 데이터 흐름** : ViewModel의 StateFlow → collectAsState() → UI 자동 갱신
- **기능별 패키지 분리** : `feature/home`, `feature/solver`, `feature/concept` 등 독립적 구조

---

## 4. 주요 구현 포인트

### 4-1. 코드 에디터 (Sora Editor 통합)

Jetpack Compose의 `AndroidView`로 네이티브 뷰를 통합해 실제 IDE에 가까운 편집 환경 구현

**주요 기능**
- 언어별 문법 하이라이팅 (Java / C++ / Python / JavaScript)
- 언어별 기본 템플릿 자동 삽입
- 스마트 중괄호 자동완성 (`{` + Enter → 들여쓰기 자동 처리)
- 커서 이동 버튼 (좌/우)

---

### 4-2. AI 코드 리뷰 (폴링 방식)

코드 제출 후 AI 분석이 완료될 때까지 2초 간격으로 서버에 상태를 확인

```kotlin
private fun startPollingAiReview(historyId: Long) {
    viewModelScope.launch {
        while (true) {
            val result = apiService.getAiReview(token, historyId)
            if (result.aiStatus == "ACCEPTED" || result.aiStatus == "SYSTEM_ERROR") break
            delay(2000)
        }
        _isReviewLoading.value = false
    }
}
```

---

### 4-3. 학습 통계 — 잔디 그래프 & 스트릭

**서버에서 오는 데이터**

```json
"totalSolvedDetails": [
  { "date": "2025-05-01", "count": 3 },
  { "date": "2025-05-02", "count": 0 },
  { "date": "2025-05-04", "count": 7 }
]
```

날짜별 제출 횟수 리스트. Repository에서 `Map<LocalDate, Int>`로 변환:

```kotlin
val contributionData = result.totalSolvedDetails
    .mapNotNull { detail ->
        runCatching { LocalDate.parse(detail.date) to detail.count }.getOrNull()
    }
    .toMap()
```

**스트릭 계산 로직**

오늘 제출이 없으면 어제부터 카운트 시작 — 아직 오늘 학습을 안 했어도 스트릭이 끊기지 않도록:

```kotlin
var checkDate = today
if ((contributionData[today] ?: 0) == 0) {
    checkDate = today.minusDays(1)  // 오늘 제출 없으면 어제부터 시작
}
while ((contributionData[checkDate] ?: 0) > 0) {
    streakCount++
    checkDate = checkDate.minusDays(1)  // 하루씩 거슬러 올라가며 연속 확인
}
```

**잔디 그래프 렌더링**

24주(168일)를 7×24 격자로 표시. `LazyRow` + `LaunchedEffect`로 최신 날짜(오른쪽 끝)로 자동 스크롤:

```kotlin
LaunchedEffect(weeks.size) {
    listState.scrollToItem(weeks.size - 1)  // 항상 오늘 날짜가 보이도록
}
```

**셀 색상 — 제출 횟수에 따라 4단계**

```kotlin
val color = when {
    count == 0    -> BgElevated               // 회색 (미학습)
    count in 1..2 -> Color(0x40F59E0B)        // 연한 노란색
    count in 3..5 -> Color(0x80F59E0B)        // 중간
    count in 6..8 -> Color(0xBFF59E0B)        // 진한
    else          -> Primary                  // 가장 진함 (9회+)
}
```

오늘 날짜 셀은 중앙에 노란 점을 추가로 표시.

---

### 4-4. 자동 로그인 & 상태 복원

- 앱 실행 시 저장된 토큰으로 자동 로그인
- 화면 복귀 시 (`ON_RESUME`) 사용자 정보 자동 갱신

```kotlin
DisposableEffect(lifecycle) {
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) viewModel.fetchHomeData()
    }
    lifecycle.addObserver(observer)
    onDispose { lifecycle.removeObserver(observer) }
}
```

---

### 4-5. 스마트 키보드

모바일에서 코딩할 때 가장 불편한 점 — 특수문자 입력. 이를 해결하기 위해 에디터 위에 슬라이딩 보조 키보드를 구현했습니다.

**구조**

`HorizontalPager`로 3페이지 구성. 각 페이지는 개발에 자주 쓰이는 특수문자 그룹:

| 페이지 | 키 |
|--------|-----|
| 1 | `;` `,` `.` `"` `'` `!` `=` `+` `-` `*` |
| 2 | `{` `}` `[` `]` `(` `)` `<` `>` `:` `?` |
| 3 | `/` `%` `&` `\|` `^` `~` `_` `@` `#` |

**페이지 인디케이터 — 애니메이션 도트**

현재 페이지는 가로 14dp 알약 모양, 나머지는 4dp 원형으로 표시. `animateDpAsState`로 크기 전환 애니메이션 적용.

**액션 키**

- **Tab** → `\t` 삽입
- **줄바꿈** → `\n` 삽입
- **주석** → 선택 언어에 맞는 prefix 삽입 (`//` 또는 `#`)

```kotlin
// 언어별 주석 prefix (CodeTemplates.kt)
fun commentPrefix(language: String): String = when (language.uppercase()) {
    "PYTHON" -> "# "
    else     -> "// "
}
```

---

### 4-6. 탭 스와이프 (HorizontalPager 연동)

문제 보기 / 에디터 / 제출 탭을 좌우 스와이프로 이동할 수 있도록 구현했습니다.

**양방향 동기화**

탭 클릭 → 페이지 이동, 스와이프 → 탭 활성화. 두 `LaunchedEffect`로 상태를 연결:

```kotlin
// 탭 클릭 → 페이지 이동
LaunchedEffect(selectedTab) {
    pagerState.animateScrollToPage(selectedTab)
}

// 스와이프 → 탭 싱크
LaunchedEffect(pagerState.currentPage) {
    selectedTab = pagerState.currentPage
}
```

**에디터 포커스 시 스와이프 잠금**

에디터에 커서가 있을 때 스와이프하면 텍스트 선택과 충돌. `userScrollEnabled`로 제어:

```kotlin
HorizontalPager(
    state = pagerState,
    userScrollEnabled = !editorHasCursor  // 에디터 포커스 중엔 스와이프 비활성화
)
```

---

### 4-7. 응용학습 — 빈칸 채우기

서버에서 받은 코드 템플릿의 `____`(밑줄 2개 이상)을 파싱해 클릭 가능한 빈칸 슬롯으로 렌더링합니다.

**빈칸 파싱**

```kotlin
// parseLine(): 정규식으로 각 줄을 텍스트 / 빈칸 파트로 분리
private fun parseLine(line: String): List<LinePart> {
    val regex = Regex("_{2,}")
    // 매칭된 __ 구간 → isBlank=true, 나머지 → isBlank=false
}
```

**문법 하이라이팅 + 빈칸 동시 렌더링**

전체 코드를 먼저 `highlight()`로 한 번에 하이라이팅한 뒤, `globalOffset`으로 각 텍스트 파트의 위치를 추적해 `AnnotatedString`을 잘라서 붙여줍니다. 빈칸 위치에는 `BlankSlot` 컴포저블을 삽입.

**정답 확인 로직**

서버 응답의 `blanks` 리스트에서 `answer != null`인 항목만 필터링 후 `answer` 값 순서로 정렬해 정답 순서를 복원:

```kotlin
fun getCorrectAnswersByOrder(quiz: QuizItemDto): List<String> {
    return quiz.blanks
        ?.filter { it.answer != null }
        ?.sortedBy { it.answer }    // answer 값이 빈칸 순서
        ?.map { it.content }
        ?: emptyList()
}
```

**UX 흐름**

```
빈칸 클릭 → 해당 슬롯 선택(파란 테두리)
    ↓
하단 선택지 칩 클릭 → 해당 빈칸에 답 채워짐
    ↓
제출 → 정답/오답 모달 (오답 시 "다시 풀기" / "정답 확인" 선택)
```

---

### 4-8. 개념학습 — 슬라이드 페이지네이션

서버에서 받은 개념 목록을 `pageNo` 기준으로 정렬해 슬라이드 형식으로 한 장씩 보여줍니다.

**데이터 흐름**

```kotlin
// ViewModel: pageNo 순 정렬 후 상태 저장
val sortedNotions = response.result?.notions?.sortedBy { it.pageNo } ?: emptyList()
_uiState.update { it.copy(concepts = sortedNotions, currentIndex = safeIndex) }
```

**콘텐츠 구조 (슬라이드 1장)**

각 `NotionDto`는 3가지 레이어로 구성:

| 영역 | 내용 |
|------|------|
| `point` | 핵심 개념 설명 텍스트 |
| `exampleCode` | 예제 코드 (있을 때만 표시) |
| `detail` | 상세 보충 설명 |

**진행률 표시 — 애니메이션 프로그레스 바**

```kotlin
val animatedProgress by animateFloatAsState(
    targetValue = (currentIndex + 1).toFloat() / total.toFloat(),
    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
)
LinearProgressIndicator(progress = { animatedProgress }, strokeCap = StrokeCap.Round)
```

페이지 이동 시 프로그레스 바가 500ms 동안 부드럽게 채워지는 효과.

**학습 완료 서버 전송**

"다음 >" 클릭 시 현재 슬라이드를 완료 처리하고 다음 페이지로 이동. 마지막 페이지에서는 완료 API 응답 결과와 무관하게 즉시 다음 단계로 이동해 UX를 끊기지 않게 처리:

```kotlin
fun completeCurrentNotionAndGoNext(onComplete: () -> Unit) {
    viewModelScope.launch {
        repository.completeConcept(token, notionId)  // 서버 전송 (fire-and-forget)
        onComplete()  // 결과 기다리지 않고 바로 이동
    }
}
```

**하단 버튼 고정 (Gradient Fade)**

스크롤 콘텐츠 위에 이전/다음 버튼이 떠있는 형태. 콘텐츠가 버튼 뒤로 자연스럽게 사라지도록 `Brush.verticalGradient`로 페이드 처리:

```kotlin
Box(modifier = Modifier
    .align(Alignment.BottomCenter)
    .background(Brush.verticalGradient(
        colors = listOf(Color.Transparent, BgPrimary.copy(0.8f), BgPrimary)
    ))
)
```

---

## 7. 향후 개선 계획

### 코드 에디터

| 항목 | 현재 | 개선 방향 |
|------|------|-----------|
| 문법 하이라이팅 | Java 고정 | 선택 언어에 맞는 Language 동적 적용 |
| 커서 점프 방지 | `delay(300ms)` 고정 | 실제 타이핑 속도 기반 debounce |
| 커서 스크롤 계산 | 폰트 크기 추정값 | Sora Editor 실제 레이아웃 메트릭 사용 |
| 자동완성 | 중괄호만 지원 | 키워드·변수명 자동완성 확장 |

### AI 코드 리뷰

| 항목 | 현재 | 개선 방향 |
|------|------|-----------|
| 통신 방식 | 2초 간격 폴링 | WebSocket으로 실시간 수신 |
| 피드백 표시 | 텍스트 전체 출력 | 코드 라인별 인라인 코멘트 |

### 학습 경험

- 오프라인 캐싱 — 네트워크 없이 최근 학습 내용 열람
- 언어별 맞춤 빈칸 문제 — 현재 JAVA 고정, Python/C++ 확장 예정
- 푸시 알림 — 연속 학습 스트릭 유지 알림

---

## 5. 차별점

| | POCKETCO | 기존 서비스 (백준, 프로그래머스 등) |
|--|----------|--------------------------------------|
| 학습 단계 | 개념 → 응용 → 실전 → AI 리뷰 | 문제 풀이 위주 |
| AI 피드백 | 제출 코드 자동 분석 | 없음 |
| 학습 동기부여 | 잔디, 스트릭, 통계 | 제한적 |
| 플랫폼 | 모바일 (Android) | 웹 중심 |

---

## 6. 발표 흐름 (5분)

| 시간 | 내용 |
|------|------|
| 0:00 ~ 0:30 | 서비스 한 줄 소개 + 타겟 사용자 |
| 0:30 ~ 2:00 | 핵심 기능 데모 (학습 플로우 → 코드 에디터 → AI 리뷰) |
| 2:00 ~ 3:30 | 기술 구현 포인트 (아키텍처, 에디터 통합, 폴링) |
| 3:30 ~ 4:30 | 차별점 & 학습 동기부여 요소 |
| 4:30 ~ 5:00 | 백엔드 연동 현황 + 향후 계획 |

---

> 백엔드 파트 합산 시 "API 설계 & 서버 아키텍처" 섹션 추가 예정
