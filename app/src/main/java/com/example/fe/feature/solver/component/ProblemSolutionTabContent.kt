package com.example.fe.feature.solver.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.concept.component.CodeExampleBox
import com.example.fe.feature.concept.component.ConceptDetailBox
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.ui.theme.*

@Composable
fun ProblemSolutionTabContent(
    viewModel: SolverViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 로딩 상태
        if (uiState.isLoadingSolution) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = Primary
                    )
                    Text("해설을 불러오는 중...", color = TextSecondary, fontSize = 14.sp)
                }
            }
        }

        val solution = uiState.solution
        if (solution == null && !uiState.isLoadingSolution) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "해설이 아직 없습니다.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "제출 후 '문제 해설' 탭에서 모범 답안과 상세 설명을 확인하실 수 있습니다.",
                        fontSize = 13.sp,
                        color = Color(0xFFA8A29E),
                        lineHeight = 20.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadSolution() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("해설 불러오기", color = BgPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
            return
        }

        if (solution != null) {
            // 1. 모범 답안 카드
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "모범 답안",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(10.dp))
                    if (solution.code.isNotBlank()) {
                        CodeExampleBox(code = solution.code)
                    } else {
                        Text(
                            text = "// 정답 코드가 비어있습니다.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // 2. 문제 해설 카드
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "문제 해설",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(10.dp))
                    if (solution.explanation.isNotBlank()) {
                        ConceptDetailBox(text = solution.explanation)
                    } else {
                        Text(
                            text = "상세 해설이 제공되지 않았습니다.",
                            fontSize = 13.sp,
                            color = TextMuted,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(40.dp)) // 하단 여백
        }
    }
}
