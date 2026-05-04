package com.example.fe.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary

@Composable
fun MoveButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isPrimary: Boolean = true,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isPrimary) Primary else BgSurface
    val contentColor = if (isPrimary) BgPrimary else TextPrimary
    val borderColor = if (isPrimary) androidx.compose.ui.graphics.Color.Transparent else BgDivider

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, if (enabled) borderColor else borderColor.copy(alpha = 0.4f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isPrimary) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (isPrimary) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MoveButtonBar(
    onNextStepClick: () -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onNavigate: (String) -> Unit,
    isFirstPage: Boolean,
    isLastPage: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            MoveButton(
                text = "이전",
                onClick = onPrevClick,
                enabled = !isFirstPage,
                isPrimary = false
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            if (isLastPage) {
                MoveButton(text = "다음 단계", onClick = onNextStepClick, isPrimary = true)
            } else {
                MoveButton(text = "다음", onClick = onNextClick, isPrimary = true)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun MoveButtonBarPreview() {
    MoveButtonBar(
        onNextStepClick = {},
        onNextClick = {},
        onPrevClick = {},
        onNavigate = {},
        isFirstPage = false,
        isLastPage = false
    )
}
