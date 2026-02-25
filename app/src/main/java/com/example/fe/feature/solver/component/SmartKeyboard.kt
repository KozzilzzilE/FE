package com.example.fe.feature.solver.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmartKeyboardPanel(
    onInsert: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pageCount = 3
    var page by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val keys = when (page) {
            0 -> listOf(";", ",", ".", "\"", "'", "!", "=", "+", "-", "*")
            1 -> listOf("{", "}", "[", "]", "(", ")", "<", ">", ":", "?")
            else -> listOf("/", "%", "&", "|", "^", "~", "_", "@", "#", "$")
        }

        val canPrev = page > 0
        val canNext = page < pageCount - 1

        // 상단 기호
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PageNavButton(text = "〈", enabled = canPrev) { if (canPrev) page -= 1 }
            keys.forEach { k ->
                SmallKey(text = k, modifier = Modifier.weight(1f)) { onInsert(k) }
            }
            PageNavButton(text = "〉", enabled = canNext) { if (canNext) page += 1 }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // 하단 기능 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val footerButtons = listOf("Tab", "들여쓰기", "줄바꿈", "주석")
            footerButtons.forEach { text ->
                BigKey(text, Modifier.weight(1f)) {
                    val insert = when (text) {
                        "Tab" -> "\t"
                        "들여쓰기" -> "    "
                        "줄바꿈" -> "\n"
                        else -> "// "
                    }
                    onInsert(insert)
                }
            }
        }

    }
}

@Composable
private fun PageNavButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 44.dp)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Color(0xFF111827) else Color(0xFFCBD5E1)
        )
    }
}

@Composable
private fun SmallKey(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF1F4F9),
        modifier = modifier.height(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color(0xFF111827),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BigKey(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE9EEF5),
        modifier = modifier.height(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color(0xFF4A5568),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}