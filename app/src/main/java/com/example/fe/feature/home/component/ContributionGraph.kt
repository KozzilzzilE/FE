package com.example.fe.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import java.time.LocalDate

/**
 * [동기부여 잔디] 깃허브 스타일의 활동 기록 그래프 컴포넌트
 * @param contributions 날짜별 활동 수치(해결한 문제 수 등) Map
 */
@Composable
fun ContributionGraph(
    contributions: Map<LocalDate, Int>,
    modifier: Modifier = Modifier
) {
    // 1. 데이터 준비: 최근 24주(약 6개월)의 날짜 리스트 생성
    val today = LocalDate.now()
    val weeksToShow = 24

    // 월요일 시작 기준 날짜 계산
    val lastSunday = today.plusDays((7 - today.dayOfWeek.value).toLong())
    val firstMonday = lastSunday.minusWeeks((weeksToShow - 1).toLong()).minusDays(6)

    val weeks = (0 until weeksToShow).map { weekIndex ->
        (0..6).map { dayIndex ->
            firstMonday.plusWeeks(weekIndex.toLong()).plusDays(dayIndex.toLong())
        }
    }

    // 2. 연속 학습 스트릭 계산 (오늘부터 역산) — 기존 로직 유지
    val streakCount = androidx.compose.runtime.remember(contributions) {
        var count = 0
        var checkDate = today
        while (contributions[checkDate] != null && contributions[checkDate]!! > 0) {
            count++
            checkDate = checkDate.minusDays(1)
        }
        count
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "${streakCount}일째 연속 학습 중 🔥",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgSurface, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row {
                // 요일 라벨
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 28.dp, end = 8.dp)
                ) {
                    DayLabel("Mon")
                    DayLabel("")
                    DayLabel("Wed")
                    DayLabel("")
                    DayLabel("Fri")
                    DayLabel("")
                    DayLabel("")
                }

                // 가로 스크롤 잔디 그리드
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(weeks) { weekDays ->
                        val firstDayOfWeek = weekDays[0]
                        val showMonthLabel = firstDayOfWeek.dayOfMonth <= 7
                        val monthName = if (showMonthLabel) {
                            firstDayOfWeek.month.getDisplayName(
                                java.time.format.TextStyle.SHORT,
                                java.util.Locale.ENGLISH
                            )
                        } else ""

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = monthName,
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier
                                    .height(24.dp)
                                    .padding(bottom = 4.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                for (date in weekDays) {
                                    val count = contributions[date] ?: 0
                                    GrassCell(count = count)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 하단 범례
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Less", fontSize = 11.sp, color = TextMuted)
            Spacer(modifier = Modifier.width(6.dp))
            GrassCell(0)
            Spacer(modifier = Modifier.width(3.dp))
            GrassCell(2)
            Spacer(modifier = Modifier.width(3.dp))
            GrassCell(6)
            Spacer(modifier = Modifier.width(3.dp))
            GrassCell(12)
            Spacer(modifier = Modifier.width(3.dp))
            GrassCell(20)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "More", fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
private fun DayLabel(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        color = TextMuted,
        modifier = Modifier.height(18.dp)
    )
}

@Composable
private fun GrassCell(count: Int) {
    val color = when {
        count == 0    -> BgElevated
        count in 1..3 -> androidx.compose.ui.graphics.Color(0x40F59E0B)
        count in 4..8 -> androidx.compose.ui.graphics.Color(0x80F59E0B)
        count in 9..15 -> androidx.compose.ui.graphics.Color(0xBFF59E0B)
        else           -> Primary
    }

    Box(
        modifier = Modifier
            .size(18.dp)
            .background(color, shape = RoundedCornerShape(3.dp))
    )
}
