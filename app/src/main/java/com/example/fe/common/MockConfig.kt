package com.example.fe.common

/**
 * 전역 Mock 모드 설정
 * 서버 연결이 안 된 상태에서 테스트할 때 true로 설정합니다.
 */
object MockConfig {
    const val USE_MOCK = true
    
    // 개별 제어가 필요한 경우를 위해 세분화 가능
    const val USE_MOCK_HOME = USE_MOCK
    const val USE_MOCK_CONCEPT = USE_MOCK
    const val USE_MOCK_PRACTICE = USE_MOCK
    const val USE_MOCK_PROBLEM = USE_MOCK
    const val USE_MOCK_SOLVER = USE_MOCK
}
