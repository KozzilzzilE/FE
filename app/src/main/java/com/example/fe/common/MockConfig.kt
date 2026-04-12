package com.example.fe.common

/**
 * ============================================================
 * [MOCK 설정] 전역 Mock 모드 스위치
 * ============================================================
 *
 * ★ 실제 서버 연동 시: USE_MOCK = false 로 변경하세요.
 * ★ 서버 없이 테스트 시: USE_MOCK = true 로 설정하세요.
 *
 * 이 값이 true이면 MockInterceptor가 모든 API 요청을 가로채서
 * MockResponseData에 정의된 가짜 JSON을 반환합니다.
 * 기존 Repository/ViewModel 코드는 전혀 수정할 필요 없습니다.
 */
object MockConfig {
    // ★★★ 이 한 줄만 바꾸면 Mock ↔ 실서버 전환 ★★★
    const val USE_MOCK = false
}
