package com.example.fe.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * [동기부여 잔디] 깃허브 스타일의 활동 기록 그래프 컴포넌트
 */
@Composable
fun ContributionGraph(
    contributions: Map<LocalDate, Int>,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val weeksToShow = 24

    val lastSunday = today.plusDays((7 - today.dayOfWeek.value).toLong())
    val firstMonday = lastSunday.minusWeeks((weeksToShow - 1).toLong()).minusDays(6)

    val weeks = (0 until weeksToShow).map { weekIndex ->
        (0..6).map { dayIndex ->
            firstMonday.plusWeeks(weekIndex.toLong()).plusDays(dayIndex.toLong())
        }
    }

    // 연속 학습 스트릭 계산 (팀원 로직 반영: 오늘 데이터 없으면 어제부터 체크)
    val streakCount = remember(contributions) {
        var count = 0
        var checkDate = today
        if ((contributions[today] ?: 0) == 0) {
            checkDate = today.minusDays(1)
        }
        while ((contributions[checkDate] ?: 0) > 0) {
            count++
            checkDate = checkDate.minusDays(1)
        }
        count
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BgSurface, shape = RoundedCornerShape(14.dp))
            .border(1.dp, BgElevated, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {
        // 스트릭 정보 (피그마 calCard 레이아웃)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = streakCount.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = "일 연속 학습 중",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "🔥",
                    fontSize = 18.sp
                )
            }
            Text(
                text = "최근 18주",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
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

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(weeks) { weekDays ->
                        val firstDayOfWeek = weekDays[0]
                        val showMonthLabel = firstDayOfWeek.dayOfMonth <= 7
                        val monthName = if (showMonthLabel) {
                            firstDayOfWeek.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
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
        count in 1..3 -> Color(0x40F59E0B)
        count in 4..8 -> Color(0x80F59E0B)
        count in 9..15 -> Color(0xBFF59E0B)
        else           -> Primary
    }

    Box(
        modifier = Modifier
            .size(18.dp)
            .background(color, shape = RoundedCornerShape(3.dp))
    )
}
