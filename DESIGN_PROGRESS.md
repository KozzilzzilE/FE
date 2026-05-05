# 피그마 디자인 구현 진행 상황 (DESIGN_PROGRESS.md)

현재까지 피그마 디자인(디자인 개선안)을 바탕으로 안드로이드 UI(Jetpack Compose)에 적용된 화면들의 작업 진행 상황입니다.

## 📊 진행 요약
- **완료된 화면**: 11개
- **진행 중인 화면**: 0개
- **대기 중인 화면**: 13개

---

## 📱 화면별 상세 작업 내역

| No | 화면 이름 | 설명 | 상태 | 주요 작업 내용 |
|:---|:---|:---|:---:|:---|
| 1 | **TopicListScreen** | 알고리즘 주제 목록 | ✅ **완료** | • `TopicCard` 아이콘+배경색 추가 (이름 키워드 기반 매핑, 색상 3색 순환 폴백)<br>• `TopicListScreen` 헤더(대제목+주제수) 인라인 배치, TopBar 제거 |
| 2 | **StepSelectionScreen** | 학습 단계 선택 | ✅ **완료** | • `StepCard` 01/02/03 스텝 번호 + 좌측 컬러 패널 구조로 전면 교체<br>• `sssTopicBadge` - `getTopicIcon(topicName)` 재사용, amber 배경 배지 구현<br>• `TopBar` 제거 → 인라인 헤더(← + 토픽 배지), topicName은 네비게이션 인자로 이미 전달 확인 |
| 3 | SolveScreen | 문제 풀이 화면 | ⬜️ 대기 | - |
| 4 | SolveScreen_Editor | 코드 에디터 화면 | ⬜️ 대기 | - |
| 5 | SolveScreen_Submit | 제출 화면 | ⬜️ 대기 | - |
| 6 | ConceptListScreen | 개념 학습 목록 | ⬜️ 대기 | - |
| 7 | **ConceptSlideScreen** | 개념 슬라이드 | ✅ **완료** | • `ConceptDetailScreen.kt` 전면 재작성 — Scaffold 제거, 인라인 커스텀 헤더(`← + 제목 + 🏠`) 적용<br>• `statusBarsPadding()` 추가로 SafeArea 미가림 처리<br>• `ConceptContentCard`: 요약(point) + 예제코드 + 상세설명(Markdown) 섹션 분리<br>• `ConceptImageSection`: `HorizontalPager` + dot 인디케이터, `SubcomposeAsyncImage`(Coil) 비동기 로딩<br>• 하단 네비게이션: 페이지 dot + `< 이전` / `다음 >` / `다음 단계 >` 버튼 |
| 8 | PracticeListScreen | 응용학습 목록 | ⬜️ 대기 | - |
| 9 | **PracticeScreen** | 응용학습 풀기 | ✅ **완료** | • `totalBlanks` 기준으로 `SelAnswerSec` 빈칸 슬롯 수 수정 (기존 `blanks.size` 오류 수정)<br>• `PracticeProgressHeader`: "빈칸 채우기" 레이블 제거, 프로그레스 바 색상 `Primary`(amber)로 변경, 높이 8dp→4dp<br>• `PracticeHeaderBar` 헤더 배경색 `BgPrimary` 통일<br>• `ResultModal` 구현: 정답/오답 팝업 다이얼로그 (반투명 딤 배경, 카드 UI, 아이콘+메시지+액션 버튼) |
| 10 | ProblemList | 문제 목록 | ⬜️ 대기 | - |
| 11 | ProblemListScreen | 문제 목록 (필터 포함) | ⬜️ 대기 | - |
| 12 | **HomeScreen** | 🏠 메인 홈 | ✅ **완료** | • 상단 바(`HomeTopBar`) 로고 에셋 교체 및 안전 영역 확보<br>• `QuickMenuCard` 3D 아이콘 에셋 추출 및 오버랩 레이아웃 완벽 동기화<br>• `calCard`(`ContributionGraph`) 잔디 UI 피그마 카드 스타일 동기화 및 부가 기능 제거<br>• 하단 네비게이션 바(`BottomNavigation`) 아이콘 크기 확장 및 테두리 추가 |
| 13 | **LoginScreen** | 로그인 | ✅ **완료** | • 메인 로고 에셋 동기화<br>• 소셜 로그인 버튼(Google, GitHub) 아이콘 배치 및 스타일 피그마와 일치화<br>• `InputSection`을 `OutlinedTextField` 시스템으로 개편하여 공통화 |
| 14 | **SignUpScreen** | 회원가입 | ✅ **완료** | • 소셜 아이콘 로고 동기화<br>• 회원가입 정보 입력란(`InputSection`) 최신 피그마 디자인 적용<br>• 언어 선택 드롭다운(`b2LangLblT`, `b2LangInp`) 스타일 피그마 일치화 |
| 15 | **AdditionalInfoScreen** | 추가정보 입력 | ✅ **완료** | • `SocialSignUpScreen.kt`로 개발<br>• `aiSub` 서브 레이블 텍스트 폰트/사이즈/컬러 일치화<br>• 전체 폼 레이아웃 및 여백 피그마와 동일하게 동기화 |
| 16 | MyPage | 마이페이지 | ⬜️ 대기 | - |
| 17 | MyPageProfile | 프로필 편집 | ⬜️ 대기 | - |
| 18 | **MyPageStore (BookmarkScreen)** | 찜한 문제 목록 | ✅ **완료** | • `BookmarkCard` 레이아웃 수평 구조로 재작성 (난이도 배지 \| 제목 \| 북마크 아이콘+카운트+원형 아이콘)<br>• 트레일링 영역을 단일 `Row`로 묶어 여백(`Spacer 4dp`, `Spacer 8dp`) `ProblemCard`와 통일<br>• 카드 높이 68dp, `BgSurface` 배경, `RoundedCornerShape(14dp)` 적용 |
| 19 | MyPageLang | 언어 설정 | ⬜️ 대기 | - |
| 20 | **CsQuiz** | 🧠 CS 퀴즈 | ✅ **완료** | • CS 퀴즈 화면 레이아웃 및 뷰모델 구성 완료 |
| 21 | **CsQuizAnswer** | 🧠 CS 퀴즈 정답 | ✅ **완료** | • 정답 결과창 UI 구현 및 버튼 배치 최적화 완료 |
| 22 | ExecResultScreen | 실행 결과 | ⬜️ 대기 | - |
| 23 | TestCaseScreen | 테스트케이스 | ⬜️ 대기 | - |
| 24 | CodeAnswer / SubmissionDetailScreen | 모범 답안 / 제출 상세 | ⬜️ 대기 | - |

---

### 💡 공통 컴포넌트 업데이트 내역
해당 화면들을 구현하면서 아래 공통 컴포넌트들도 피그마의 최신 디자인 시스템에 맞게 전면 업데이트 되었습니다:
- **`InputSection`**: 입력 필드 디자인 일원화 (`OutlinedTextField` 기반)
- **`SignUpLanguageDropdown`**: 드롭다운 UI 스타일 공통화
- **`HomeTopBar` & `BottomNavigation`**: 최상/최하단 앱 내비게이션 바 레이아웃 리뉴얼
- **`MainActionsRow` & `ContributionGraph`**: 메인 홈 전용 대시보드 UI 컴포넌트 리팩토링 및 3D 에셋화
- **`TopBar`**: 배경색 `BgSurface` → `BgPrimary` 통일 (북마크, 상세목록 등 공통 헤더 전체 적용)
- **`PracticeHeaderBar`**: 배경색 `BgPrimary` 통일, 중앙 정렬 헤더 구조 확립 (응용학습 이후 화면 전체 적용)
- **`ResultModal`**: 정답/오답 결과 팝업 공통 컴포넌트 신규 추가 (`BlankSection.kt`)
