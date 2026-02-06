// 개념 학습, 응용 학습, 문제 학습에서 사용되는 이전, 다음 버튼 이 포함된 하단 네비게이션

package com.example.fe.common


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// 버튼들에는 onClick 이벤트가 필요
// String(이전,다음,다음단계)만 필요
@Composable
fun MoveButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isPrimary: Boolean = true, // true면 파란색(다음 or 다음단계), false면 흰색(이전) , 다음단계 버튼도 스타일 다르게 설정 필요한가?
    modifier: Modifier = Modifier
) {
    // 1. 색상 및 테두리 설정 (isPrimary에 따라 분기)
    // Primary(다음): 파란 배경, 흰 글씨, 테두리 없음
    // Secondary(이전): 흰 배경, 검은 글씨, 회색 테두리
    val containerColor = if (isPrimary) Color(0xFF4A90E2) else Color.White
    val contentColor = if (isPrimary) Color.White else Color.Black
    val borderColor = if (isPrimary) Color.Transparent else Color(0xFFE0E0E0)
    
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp), // 버튼 높이를 조금 키움 (터치하기 편하게)
        shape = RoundedCornerShape(12.dp), // 둥근 모서리 (이미지 디자인 반영)
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            // 비활성화 상태일 때도 색감만 투명하게 조절
            disabledContainerColor = if (isPrimary) Color(0xFF4A90E2).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.5f),
            disabledContentColor = if (isPrimary) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
        ),
        // 이전 버튼일 때만 테두리를 그룸
        border = BorderStroke(1.dp, if (enabled) borderColor else borderColor.copy(alpha = 0.5f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
             verticalAlignment = Alignment.CenterVertically,
             horizontalArrangement = Arrangement.Center
        ) {
            // 2. 아이콘 배치 로직
            // 이전 버튼(Secondary)이면 왼쪽에 '<' 화살표 추가
            if (!isPrimary) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            
            // 다음 버튼(Primary)이면 오른쪽에 '>' 화살표 추가
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
    

    // 총 경우의 수는 4개
    // 1. 첫번쨰 페이지일 경우 -> 이전(비활성), 다음(활성)
    // 2. 마지막 페이지일 경우 -> 이전(활성), 다음 단계(활성)
    // 3. 중간 페이지일 경우 -> 이전(활성), 다음(활성)
    // 4. 단일 페이지일 경우 -> 이전(비활성), 다음 단계(활성)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // 전체 여백
        horizontalArrangement = Arrangement.spacedBy(12.dp) // 버튼 사이 간격 12dp
    ) {
        // [왼쪽 영역] 이전 버튼 배치
        // weight(1f)를 주어 화면 너비의 50%를 차지하게 함
        Box(modifier = Modifier.weight(1f)) {
             MoveButton(
                text = "이전",
                onClick = onPrevClick,
                enabled = !isFirstPage, // 첫 페이지면 비활성화 (흐리게 보임)
                isPrimary = false       // 하얀색 스타일 적용
            )
        }

        // [오른쪽 영역] 다음 또는 다음 단계 버튼 배치
        Box(modifier = Modifier.weight(1f)) {
            // 마지막 페이지라면 '다음' 대신 '다음 단계' 버튼을 보여줌
            if (isLastPage) {
                MoveButton(
                    text = "다음 단계",
                    onClick = onNextStepClick,
                    isPrimary = true // 파란색 스타일 적용
                )
            } else {
                MoveButton(
                    text = "다음",
                    onClick = onNextClick,
                    isPrimary = true // 파란색 스타일 적용
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoveButtonBarPreview() {
    // DB에서 가져온 데이터라고 가정
    val currentPage = 3
    val totalPage = 5

    MoveButtonBar(
        onNextStepClick = {},
        onNextClick = {},
        onPrevClick = {},
        onNavigate = {},
        // 페이지 번호로 상태 결정
        isFirstPage = currentPage == 1,
        isLastPage = currentPage == totalPage
    )
}
