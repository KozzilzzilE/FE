package com.example.fe.feature.solver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.Primary

@Composable
fun DraftSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "임시저장",
        color = Primary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(
                color = Color(0x26F59E0B),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}
