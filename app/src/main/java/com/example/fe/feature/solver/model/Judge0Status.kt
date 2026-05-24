package com.example.fe.feature.solver.model

object Judge0Status {
    // Judge0 표준 statusId
    const val IN_QUEUE               = 1
    const val PROCESSING             = 2
    const val ACCEPTED               = 3
    const val WRONG_ANSWER           = 4
    const val TIME_LIMIT_EXCEEDED    = 5
    const val COMPILATION_ERROR      = 6
    const val RUNTIME_ERROR_SIGSEGV  = 7
    const val RUNTIME_ERROR_SIGXFSZ  = 8
    const val RUNTIME_ERROR_SIGFPE   = 9
    const val RUNTIME_ERROR_SIGABRT  = 10
    const val RUNTIME_ERROR_NZEC     = 11
    const val RUNTIME_ERROR_OTHER    = 12
    const val INTERNAL_ERROR         = 13
    const val NO_RESPONSE            = -1  // 토큰 없음 / Judge0 연결 실패

    fun isStillProcessing(statusId: Int): Boolean =
        statusId == IN_QUEUE || statusId == PROCESSING

    fun isAccepted(statusId: Int): Boolean = statusId == ACCEPTED

    fun isCompilationError(statusId: Int): Boolean = statusId == COMPILATION_ERROR

    fun isRuntimeError(statusId: Int): Boolean = statusId in RUNTIME_ERROR_SIGSEGV..RUNTIME_ERROR_OTHER

    fun toKoreanLabel(statusId: Int): String = when (statusId) {
        IN_QUEUE             -> "대기 중"
        PROCESSING           -> "실행 중"
        ACCEPTED             -> "성공"
        WRONG_ANSWER         -> "오답"
        TIME_LIMIT_EXCEEDED  -> "시간 초과"
        COMPILATION_ERROR    -> "컴파일 오류"
        RUNTIME_ERROR_SIGSEGV  -> "런타임 오류"
        RUNTIME_ERROR_SIGXFSZ  -> "런타임 오류 (출력 제한 초과)"
        RUNTIME_ERROR_SIGFPE   -> "런타임 오류 (산술 오류)"
        RUNTIME_ERROR_SIGABRT  -> "런타임 오류 (비정상 종료)"
        RUNTIME_ERROR_NZEC     -> "런타임 오류 (비정상 종료)"
        RUNTIME_ERROR_OTHER    -> "런타임 오류"
        INTERNAL_ERROR         -> "채점 시스템 오류"
        NO_RESPONSE            -> "Judge0 연결 실패"
        else                   -> "알 수 없는 오류"
    }

    // 서버가 반환하는 제출 결과 문자열(ACCEPTED, WRONG_ANSWER 등) → 한국어
    fun serverStatusToKorean(status: String): String {
        val key = status.uppercase().replace(" ", "_").replace("-", "_")
        return when (key) {
            "ACCEPTED", "SUCCESS"       -> "정답"
            "WRONG_ANSWER"              -> "오답"
            "TIME_LIMIT_EXCEEDED"       -> "시간 초과"
            "COMPILATION_ERROR"         -> "컴파일 오류"
            "RUNTIME_ERROR",
            "RUNTIME_ERROR_SIGSEGV",
            "RUNTIME_ERROR_SIGXFSZ",
            "RUNTIME_ERROR_SIGFPE",
            "RUNTIME_ERROR_SIGABRT",
            "RUNTIME_ERROR_NZEC",
            "RUNTIME_ERROR_OTHER"       -> "런타임 오류"
            "INTERNAL_ERROR"            -> "채점 시스템 오류"
            "PROCESSING", "IN_QUEUE"    -> "채점 중"
            else                        -> status
        }
    }

    fun isServerStatusCorrect(status: String): Boolean {
        val key = status.uppercase().replace(" ", "_")
        return key == "ACCEPTED" || key == "SUCCESS"
    }

    fun isServerStatusProcessing(status: String): Boolean {
        val key = status.uppercase().replace(" ", "_")
        return key == "PROCESSING" || key == "IN_QUEUE"
    }
}
