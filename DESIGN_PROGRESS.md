# PocketCo 피그마 디자인 적용 작업 현황

> 브랜치: `design`  
> 피그마: [디자인 개선안](https://www.figma.com/design/2jgj6jIRRfjk1LI86nSI20/)  
> 마지막 업데이트: 2026-05-04

---

## 디자인 시스템 (피그마에서 추출)

### 색상 팔레트
| 역할 | 색상 코드 | 설명 |
|------|-----------|------|
| `BgPrimary` | `#1C1917` | 메인 배경 (따뜻한 검정) |
| `BgSurface` | `#292524` | 카드 / 서피스 |
| `BgElevated` | `#44403C` | 높은 레벨 서피스 |
| `BgDivider` | `#57534E` | 구분선 |
| `TextPrimary` | `#FAFAF9` | 기본 텍스트 |
| `TextSecondary` | `#A8A29E` | 보조 텍스트 |
| `TextMuted` | `#78716C` | 흐린 텍스트 |
| `Primary` | `#F59E0B` | 메인 강조색 (Amber) |
| `Success` | `#22C55E` | 정답 / 성공 |
| `Error` | `#FB2C36` | 오답 / 에러 |
| `Blue` | `#3B82F6` | 파란색 강조 |
| `Cyan` | `#06B6D4` | 코드 하이라이트 |

### 타이포그래피
| 스타일 | 크기 | 굵기 | 용도 |
|--------|------|------|------|
| 제목 (Title) | 24sp | Bold (700) | 화면 제목 |
| 헤더 (Header) | 17~18sp | SemiBold (600) | 섹션 헤더 |
| 본문 (Body) | 14~15sp | Regular (400) | 일반 텍스트 |
| 캡션 (Caption) | 11~13sp | Medium (500) | 보조 설명 |
| 폰트 패밀리 | Inter | - | 전체 앱 |

---

## 완료된 작업 ✅

### 1. 디자인 토큰 (기반)
- **`ui/theme/Color.kt`** — 피그마 색상 팔레트로 전면 교체
- **`ui/theme/Theme.kt`** — 다크 테마 고정, Dynamic Color 제거

### 2. 공통 컴포넌트
- **`common/BottomNavigation.kt`** — Pill 스타일 하단 탭바 구현
  - 선택된 탭: `Primary(#F59E0B)` 배경 + 검정 텍스트
  - 미선택 탭: 배경 없음 + `TextMuted` 텍스트
  - 전체 컨테이너: `BgSurface` 배경, 36dp 라운드

### 3. 인증 화면
- **`feature/auth/ui/LoginScreen.kt`** — 완전 재작성
  - 다크 배경, 로고 중앙, 이메일/비번 입력, Amber 로그인 버튼
  - Google/GitHub 소셜 버튼 (다크 서피스)
- **`feature/auth/ui/SignUpScreen.kt`** — 완전 재작성
  - 소셜 회원가입 버튼 → 입력폼 순서
  - 오렌지 필드 테두리 (포커스 시), Amber 완료 버튼

### 4. 홈 화면 (Phase 1 완료 ✅ — 2026-05-04)
- **`feature/home/component/HomeTopBar.kt`** — 다크 테마 재작성
  - 좌측: Amber 원형 로고 + "PocketCo" 텍스트
  - 우측: 프로필 아이콘 버튼 (`BgElevated` 배경, `onProfileClick` 콜백)
- **`feature/home/component/ContributionGraph.kt`** — 다크 테마 색상 적용
  - 잔디 색상: 파란 계열 → Amber 계열 (투명도 40%~100%)
  - 배경: `BgSurface`, 텍스트: `TextMuted`
- **`feature/home/component/LanguageDropdown.kt`** — 다크 테마 색상 적용
  - 배경: `BgSurface`, 선택값: `Primary`, 아이콘: `TextSecondary`
- **`feature/home/ui/HomeScreen.kt`** — 전체 레이아웃 개편
  - `BgPrimary` 배경, 세로 스크롤 레이아웃
  - 인사말 섹션 (ViewModel의 `name` 표시)
  - 빠른 메뉴 3칸 (학습하기→`topic`, 즐겨찾기→`my`, CS 퀴즈→`cs_quiz`)
  - 스트릭 그래프 (`ContributionGraph` 재사용)
  - 명언 카드 (랜덤 표시)
  - ViewModel·상태 관리·LaunchedEffect·BottomNav 전부 유지
- **`ui/theme/Color.kt`** — 레거시 컬러 별칭 추가
  - 다른 화면에서 쓰던 구 토큰 14개(`CardBg`, `CodeBg`, `TitleText` 등)를 신규 토큰으로 매핑하여 빌드 에러 해결

### 5. 학습 탐색 및 개념 플로우 (Phase 2 완료 ✅ — 2026-05-04)
- **`common/TopBar.kt`** — 다크 테마 (`BgSurface` 배경, `TextPrimary` 제목, `Primary` 홈 아이콘)
- **`common/NextPrevButton.kt`** — MoveButton 다크 테마 (`Primary` Amber 버튼, `BgSurface` 이전 버튼)
- **`common/ProblemCard.kt`** — DetailCard + DifficultyBadge 다크 테마
  - `BgSurface` 카드, `BgElevated` 번호 뱃지, `Primary` 번호, 투명 배경 난이도 배지
- **`feature/list/component/TopicCard.kt`** — `BgSurface` 카드, `TextPrimary` 텍스트
- **`feature/list/ui/TopicListScreen.kt`** — `BgPrimary` 배경
- **`feature/list/ui/DetailListScreen.kt`** — `BgPrimary` 배경
- **`feature/list/ui/ProblemListScreen.kt`** — `BgPrimary` 배경
- **`feature/step/component/StepCard.kt`** — `BgElevated` 아이콘 박스, `TextPrimary`/`TextSecondary`
- **`feature/step/ui/StepSelectionScreen.kt`** — `BgPrimary` 배경, `BgSurface` 카드, Blue/Success/Error 아이콘
- **`feature/concept/component/ConceptSummaryBox.kt`** — `BgSurface` + `TextPrimary`
- **`feature/concept/component/CodeExampleBox.kt`** — `CodeBgDark` + `Cyan` 코드 텍스트
- **`feature/concept/component/ConceptDetailBox.kt`** — `BgSurface`, Markdown 다크 테마
- **`feature/concept/ui/ConceptDetailScreen.kt`** — `BgPrimary`, `Primary` 밑줄, `TextPrimary` 제목

---

## 진행 예정 작업 🔜

### 6. 인증 (나머지)
- [ ] **`feature/auth/ui/SocialSignUpScreen.kt`** → `AdditionalInfoScreen` 디자인 적용
  - 추가 정보 입력 (이름, 이메일, 주 사용 언어)

### 7. 문제 풀기
- [ ] **`feature/solver/ui/SolveScreen.kt`** — 다크 에디터 + 탭바
- [ ] **`feature/solver/ui/EditorScreen.kt`** — 코드 에디터 스타일

### 8. 학습 화면
- [ ] **`feature/concept/ui/ConceptDetailScreen.kt`** — 개념 슬라이드 화면
- [ ] **`feature/practice/ui/PracticeScreen.kt`** — 빈칸 채우기 연습

---

## 신규 화면 생성 필요 🆕

피그마에는 있지만 코드에 없는 화면들:

| 화면 | 파일명 (예정) | 피그마 ID |
|------|--------------|-----------|
| 마이페이지 | `feature/mypage/ui/MyPageScreen.kt` | `76:732` |
| 개인정보 수정 | `feature/mypage/ui/MyPageProfileScreen.kt` | `76:871` |
| 즐겨찾기 | `feature/mypage/ui/MyPageStoreScreen.kt` | `76:919` |
| 언어 설정 | `feature/mypage/ui/MyPageLangScreen.kt` | `76:974` |
| CS 퀴즈 | `feature/csquiz/ui/CsQuizScreen.kt` | `82:1037` |
| CS 퀴즈 정답 | `feature/csquiz/ui/CsQuizAnswerScreen.kt` | `82:1213` |
| 제출 상세 | `feature/solver/ui/SubmissionDetailScreen.kt` | `137:155` |

---

## 라우팅 업데이트 필요 📍

`navigation/Routes.kt` 와 `navigation/AppNavGraph.kt` 에 신규 화면 라우트 추가:
- `my` → MyPageScreen
- `my/profile` → MyPageProfileScreen  
- `my/store` → MyPageStoreScreen (즐겨찾기)
- `my/lang` → MyPageLangScreen
- `cs_quiz` → CsQuizScreen
- `submission/{submissionId}` → SubmissionDetailScreen

---

## 🗺️ 향후 진행 로드맵 (Roadmap)

체계적이고 안정적인 디자인 적용을 위해 아래와 같이 5단계 페이즈(Phase)로 나누어 진행하는 것을 제안합니다.

### ~~Phase 1: 홈 화면 및 핵심 네비게이션~~ ✅ 완료 (2026-05-04)
- ~~홈 화면(`HomeScreen`) 레이아웃 개편 및 다크 테마 컴포넌트(잔디밭 그래프, 빠른 메뉴 등) 적용~~
- ~~하단 네비게이션과 메인 화면들(홈, 학습) 간의 자연스러운 라우팅 구조 확립~~

### Phase 2: 학습 탐색 및 개념/응용 플로우 (학습 코어)
- `TopicListScreen` 및 `StepSelectionScreen` 카드 UI 개선
- `ConceptDetailScreen` (개념 슬라이드) 및 `PracticeScreen` (빈칸 채우기) 다크 테마 적용
- 가독성이 매우 중요한 화면이므로 텍스트 색상(`TextPrimary`, `TextSecondary`) 대비를 중점적으로 확인

### Phase 3: 문제 풀이 및 제출 환경 (에디터 코어)
- `SolveScreen`의 3탭(문제/에디터/제출) 구조 디자인 확립
- 코드 에디터 화면의 다크 모드 최적화 (키보드 패널 포함)
- 신규 화면인 **제출 상세 화면(`SubmissionDetailScreen`)** 생성 및 연동

### Phase 4: 마이페이지 및 부가 기능 (기능 확장)
- 피그마에는 있지만 아직 없는 **마이페이지(`MyPageScreen`)** 및 하위 설정 화면들 신규 생성
- **CS 지식 OX 퀴즈 화면(`CsQuizScreen`)** 구현
- `Routes` 및 `AppNavGraph`에 신규 라우팅 최종 추가

### Phase 5: 최종 폴리싱 (Polishing)
- 컴포저블(Composable) 간 부드러운 전환 애니메이션 적용
- 피그마 시안과 대조하여 마진(Margin), 패딩(Padding), 타이포그래피 미세 조정
- 앱 전반의 다크 테마 일관성 퀄리티 보증(QA)

---

## 💡 작업 방식

1. 피그마에서 각 화면의 색상/텍스트 스타일 추출 후 `ui/theme`의 토큰 적극 활용
2. 기존 코드의 아키텍처 (ViewModel, Repository, State)는 최대한 유지
3. UI 레이어만 교체 (비즈니스 로직 변경 최소화)
4. 모든 변경사항은 `design` 브랜치에서 안전하게 작업 후 커밋
