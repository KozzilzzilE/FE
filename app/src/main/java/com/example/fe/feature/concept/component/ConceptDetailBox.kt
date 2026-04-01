package com.example.fe.feature.concept.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

/**
 * 개념 학습 화면의 상세 설명 박스
 * 서버에서 Markdown 형식으로 전달되는 detail 텍스트를 렌더링합니다.
 *
 * ★ 지원되는 Markdown 요소:
 *   - 제목(###), 굵은 글씨(**bold**), 이모지
 *   - 표(Table), 리스트(- item), 구분선(---)
 *   - 코드 블록(```code```)
 */
@Composable
fun ConceptDetailBox(text: String) {
    Column {
        Text(
            text = "상세 설명",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7A828A),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F8FF))
                .padding(20.dp)
        ) {
            Markdown(
                content = text,
                colors = markdownColor(
                    text = Color(0xFF333333),
                    codeText = Color(0xFF1A1F27),
                    codeBackground = Color(0xFFE8EFF5),
                    dividerColor = Color(0xFFD0D7DE)
                ),
                typography = markdownTypography(
                    h3 = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF1A1F27),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    paragraph = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF333333),
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    ),
                    code = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF1A1F27),
                        fontSize = 13.sp
                    )
                )
            )
        }
    }
}
