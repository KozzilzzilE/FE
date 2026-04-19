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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

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
    
    // 월요일 시작 기준 날짜 계산: 이번 주 일요일(마지막 날) 찾기
    val lastSunday = today.plusDays((7 - today.dayOfWeek.value).toLong())
    // 24주 전 월요일 찾기
    val firstMonday = lastSunday.minusWeeks((weeksToShow - 1).toLong()).minusDays(6)

    // 주차별로 데이터를 묶음 (List<List<LocalDate>>): 각 리스트는 월(0) ~ 일(6)
    val weeks = (0 until weeksToShow).map { weekIndex ->
        (0..6).map { dayIndex ->
            firstMonday.plusWeeks(weekIndex.toLong()).plusDays(dayIndex.toLong())
        }
    }

    // 2. 연속 학습 스트릭 계산 (오늘부터 역산)
    val streakCount = androidx.compose.runtime.remember(contributions) {
        var count = 0
        var checkDate = today
        
        // 만약 오늘 아직 한 문제가 없다면 어제부터 체크 시작 (스트릭 유지)
        if ((contributions[today] ?: 0) == 0) {
            checkDate = today.minusDays(1)
        }
        
        while ((contributions[checkDate] ?: 0) > 0) {
            count++
            checkDate = checkDate.minusDays(1)
        }
        count
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // 스트릭 정보 (HomeScreen에서 통합된 디자인)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 14.dp) // 왼쪽에서 24dp 밀어냄
                    .size(40.dp)
                    .background(Color(0xFFEAF1FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "이번 달 학습일",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 21.sp, fontWeight = FontWeight.Black)) {
                            append("${streakCount}일 ")
                        }
                        withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))) {
                            append("연속")
                        }
                        withStyle(style = SpanStyle(fontSize = 16.sp)) {
                            append(" 🔥")
                        }
                    },
                    color = Color(0xFF1F2937),
                    lineHeight = 32.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row {
                // 1. 왼쪽 요일 라벨 (18dp 사이즈에 맞춰 정렬)
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

                // 2. 가로 스크롤 영역 (월 라벨 + 잔디 그리드 통합)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(weeks) { weekDays ->
                        val firstDayOfWeek = weekDays[0]
                        val showMonthLabel = firstDayOfWeek.dayOfMonth <= 7
                        val monthName = if (showMonthLabel) {
                            firstDayOfWeek.month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH)
                        } else ""

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = monthName,
                                fontSize = 11.sp,
                                color = Color.Gray,
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
        
        // 하단 범례 (Legend)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Less", fontSize = 11.sp, color = Color.Gray)
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
            Text(text = "More", fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun DayLabel(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        color = Color.Gray,
        modifier = Modifier.height(18.dp) // 잔디 사이즈와 동일하게 18dp
    )
}

@Composable
private fun GrassCell(count: Int) {
    val color = when {
        count == 0 -> Color(0xFFEBEDF0)
        count in 1..3 -> Color(0xFFC6DBFB)
        count in 4..8 -> Color(0xFF86B1F4)
        count in 9..15 -> Color(0xFF4A90E2)
        else -> Color(0xFF1E52A7)
    }

    Box(
        modifier = Modifier
            .size(18.dp) // 14dp -> 18dp 확대
            .background(color, shape = RoundedCornerShape(3.dp))
    )
}
