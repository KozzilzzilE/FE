package com.example.fe.feature.home.data

import java.time.LocalDate
import kotlin.random.Random

/**
 * [데모용 더미 잔디 데이터]
 *
 * 실제 계정의 연속학습(잔디) 데이터가 빈약해 보여주기 아쉬울 때,
 * 비어 있는 날들을 더미 값으로 채워 잔디를 풍부하게 보이도록 한다.
 *
 * 규칙:
 *  - 현재 연속 학습일수(스트릭)는 **정확히 2일째**가 되도록 고정한다.
 *    (오늘·어제는 채우고, **그제는 비워** 스트릭을 2에서 끊는다)
 *  - 그 이전 날들은 **군데군데 비는** 랜덤 채움으로 자연스러운 잔디 모양을 만든다.
 *  - 실제 데이터가 있는 날은 그대로 보존한다(빈 날만 채움). 단, 스트릭을 2로
 *    맞추기 위해 그제(today-2)만은 비운다.
 *
 * 데모가 끝나면 [ENABLED] 를 false 로 바꾸면 실제 데이터만 사용한다.
 */
object ContributionDummy {

    /** 데모용 더미 채움 on/off. 실서비스 전환 시 false. */
    const val ENABLED = true

    fun enrich(real: Map<LocalDate, Int>, weeksToShow: Int = 25): Map<LocalDate, Int> {
        if (!ENABLED) return real

        val today = LocalDate.now()
        val result = real.toMutableMap()

        // 같은 날엔 항상 같은 모양이 나오도록 날짜를 시드로 사용(앱 재시작/재로딩 시 깜빡임 방지)
        val rnd = Random(today.toEpochDay())

        // 화면에 보이는 범위(과거 weeksToShow주 ~ 오늘)의 빈 날을 확률적으로 채운다.
        var d = today.minusWeeks(weeksToShow.toLong())
        while (!d.isAfter(today)) {
            if ((result[d] ?: 0) == 0 && rnd.nextFloat() < 0.55f) {
                // 1~15 사이 값 → 잔디 색 단계가 골고루 분포
                result[d] = rnd.nextInt(1, 16)
            }
            d = d.plusDays(1)
        }

        // 스트릭을 정확히 2일째로 고정
        result[today] = (result[today] ?: 0).takeIf { it > 0 } ?: rnd.nextInt(4, 13)
        result[today.minusDays(1)] = (result[today.minusDays(1)] ?: 0).takeIf { it > 0 } ?: rnd.nextInt(4, 13)
        result[today.minusDays(2)] = 0 // 그제는 비워 스트릭을 2에서 끊는다

        return result
    }
}
