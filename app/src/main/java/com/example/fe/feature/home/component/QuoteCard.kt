package com.example.fe.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Quote(val text: String, val author: String)

@Composable
fun QuoteCard(
    modifier: Modifier = Modifier
) {
    // 명언 리스트
    val quotes = listOf(
        Quote("작은 코드 한 줄이 위대한 프로그램의 시작입니다.", "에이다 러브레이스"),
        Quote("완벽함보다 완료함이 낫습니다.", "셰릴 샌드버그"),
        Quote("실패는 다시 시작할 수 있는 기회일 뿐입니다.", "헨리 포드"),
        Quote("열정 없이 이루어진 위대한 일은 없습니다.", "랄프 왈도 에머슨"),
        Quote("미래를 예측하는 가장 좋은 방법은 미래를 창조하는 것입니다.", "피터 드러커")
    )

    // 랜덤 선택
    val selectedQuote = remember { quotes.random() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF4FF) // 아주 연한 블루 배경
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // "TODAY'S QUOTE" 뱃지
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "TODAY'S QUOTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A90E2)
                )
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // 명언 본문
            Text(
                text = "\"${selectedQuote.text}\"",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                lineHeight = 26.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 저자
            Text(
                text = "- ${selectedQuote.author}",
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F9FC)
@Composable
fun QuoteCardPreview() {
    QuoteCard()
}
