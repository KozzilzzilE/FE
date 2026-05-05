package com.example.fe.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.home.data.QuoteProvider
import com.example.fe.ui.theme.*

@Composable
fun QuoteCard(
    modifier: Modifier = Modifier
) {
    val selectedQuote = remember { QuoteProvider.quotes.random() }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = BorderStroke(1.dp, Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 인용 아이콘
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )

            // 명언 본문
            Text(
                text = "\"${selectedQuote.text}\"",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                lineHeight = 22.4.sp // 1.6 line-height
            )

            // 저자
            Text(
                text = "— ${selectedQuote.author}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun QuoteCardPreview() {
    QuoteCard(modifier = Modifier.padding(16.dp))
}
