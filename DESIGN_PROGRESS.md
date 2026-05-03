# PocketCo 피그마 디자인 적용 작업 현황

> 브랜치: `design`  
> 피그마: [디자인 개선안](https://www.figma.com/design/2jgj6jIRRfjk1LI86nSI20/)  
> 마지막 업데이트: 2026-05-03

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

---

## 진행 예정 작업 🔜

### 4. 인증 (나머지)
- [ ] **`feature/auth/ui/SocialSignUpScreen.kt`** → `AdditionalInfoScreen` 디자인 적용
  - 추가 정보 입력 (이름, 이메일, 주 사용 언어)

### 5. 홈 화면
- [ ] **`feature/home/ui/HomeScreen.kt`**
  - 상단: 로고 + 프로필 아이콘
  - 인사말 + 빠른 메뉴 3칸 (학습하기 / 즐겨찾기 / CS 퀴즈)
  - 연속 학습 스트릭 그래프 (기존 ContributionGraph 다크 테마 적용)
  - 명언 카드 (하단)
- [ ] **`feature/home/component/HomeTopBar.kt`** — 로고 이미지 + 프로필 버튼
- [ ] **`feature/home/component/ContributionGraph.kt`** — 다크 테마 색상 적용

### 6. 학습 목록
- [ ] **`feature/list/ui/TopicListScreen.kt`**
  - 아이콘 배지 + 주제명 + 화살표 카드 리스트
  - 하단 학습 탭 활성화 상태
- [ ] **`feature/list/ui/ProblemListScreen.kt`** — 다크 테마 적용
- [ ] **`feature/step/ui/StepSelectionScreen.kt`** — 단계 선택 카드 디자인

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

## 작업 방식

1. 피그마 API로 각 화면의 색상/텍스트 스타일 추출
2. 기존 코드 구조 (ViewModel, Repository) 유지
3. UI 레이어만 교체 (비즈니스 로직 변경 없음)
4. 모든 변경사항은 `design` 브랜치에서 작업
