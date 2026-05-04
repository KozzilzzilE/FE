package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.TextPrimary

@Composable
fun ConceptSummaryBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgSurface)
            .padding(20.dp)
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            color = TextPrimary,
            lineHeight = 24.sp
        )
    }
}
