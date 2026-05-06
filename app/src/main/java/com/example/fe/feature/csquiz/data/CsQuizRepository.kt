package com.example.fe.feature.csquiz.data

import com.example.fe.feature.csquiz.model.CsQuizQuestion

/**
 * CS 퀴즈 데이터 제공 Repository
 * 현재는 로컬 샘플 데이터를 반환하며, 추후 API 연동 시 ApiService를 주입받아 확장
 */
class CsQuizRepository {

    fun getQuizQuestions(): List<CsQuizQuestion> {
        return sampleQuestions
    }

    companion object {
        private val sampleQuestions = listOf(
            CsQuizQuestion(
                1,
                "스택(Stack)은 FIFO(First In First Out) 방식으로 동작한다.",
                false,
                "스택은 LIFO(Last In First Out) 방식입니다. FIFO는 큐(Queue)의 동작 방식입니다."
            ),
            CsQuizQuestion(
                2,
                "HTTP는 상태를 유지하지 않는 무상태(Stateless) 프로토콜이다.",
                true,
                "HTTP는 기본적으로 Stateless 프로토콜로, 각 요청이 독립적으로 처리됩니다. 상태 유지를 위해 쿠키/세션을 사용합니다."
            ),
            CsQuizQuestion(
                3,
                "이진 탐색(Binary Search)은 정렬되지 않은 배열에서도 사용 가능하다.",
                false,
                "이진 탐색은 반드시 정렬된 배열에서만 사용 가능합니다. 정렬되지 않은 배열에서는 선형 탐색을 사용합니다."
            ),
            CsQuizQuestion(
                4,
                "프로세스(Process)는 스레드(Thread)보다 더 적은 메모리를 사용한다.",
                false,
                "스레드가 프로세스보다 더 적은 메모리를 사용합니다. 스레드는 프로세스의 메모리를 공유하기 때문입니다."
            ),
            CsQuizQuestion(
                5,
                "TCP는 UDP보다 신뢰성이 높지만 속도가 느리다.",
                true,
                "TCP는 연결 지향적이고 오류 검출 및 재전송을 보장하므로 신뢰성이 높지만, 이로 인해 UDP보다 속도가 느립니다."
            )
        )
    }
}
