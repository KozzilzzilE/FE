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
import com.example.fe.feature.home.data.QuoteProvider
import com.example.fe.feature.home.model.Quote
import com.example.fe.ui.theme.*

@Composable
fun QuoteCard(
    modifier: Modifier = Modifier
) {
    // 40여 개의 명언 리스트에서 랜덤 선택
    val selectedQuote = remember { QuoteProvider.quotes.random() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BgSurface // 성규님 디자인 테마 적용
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // "TODAY'S QUOTE" 뱃지
            Box(
                modifier = Modifier
                    .background(PrimaryDim15, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "TODAY'S QUOTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // 명언 본문
            Text(
                text = "\"${selectedQuote.text}\"",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 26.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 저자
            Text(
                text = "- ${selectedQuote.author}",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun QuoteCardPreview() {
    QuoteCard()
}
