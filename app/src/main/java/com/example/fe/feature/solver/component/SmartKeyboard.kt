// =====================================
// SmartKeyboardPanel.kt (수정본 전체)
// =====================================
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

/**
 * [SmartKeyboardPanel]
 * 에디터에서 자주 사용하는 기호와 특수 기능을 빠르게 입력할 수 있는 패널입니다.
 * 시스템 키보드 상단에 밀착되어 작동하도록 설계되었습니다.
 */
@Composable
fun SmartKeyboardPanel(
    onInsert: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pageCount = 3
    var page by remember { mutableStateOf(0) }

    // ✅ 핵심 수정:
    // - padding(bottom = 8.dp) 제거 (하단 흰 공백 원인)
    // - background(Color.White) 제거 (바깥 Surface가 배경 담당)
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

        // 1) 상단 기호 열
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

        // 2) 하단 기능 버튼 열
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

        // ✅ 필요하면 아주 조금만
        // Spacer(Modifier.height(4.dp))
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