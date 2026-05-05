package com.example.fe.feature.auth.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.TextPrimary

@Composable
fun SocialLoginButton(text: String, iconResId: Int?, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BgDivider),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = BgSurface)
    ) {
        // 아이콘 + 텍스트 Row
        Text(text, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
