package com.example.fe.feature.solver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel

@Composable
fun ProblemSolutionTabContent(
    viewModel: SolverViewModel
) {
    // 리팩토링 VM 기준 (uiState.solution)
    val uiState by viewModel.uiState.collectAsState()

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        // 헤더 + 새로고침
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("문제 해설", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextButton(onClick = { viewModel.loadSolution() }) {
                Text("새로고침")
            }
        }

        Spacer(Modifier.height(12.dp))

        // 로딩 상태
        if (uiState.isLoadingSolution) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF3F4F6)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Text("모범 답안을 불러오는 중...", color = Color(0xFF4B5563))
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        val solution = uiState.solution
        if (solution == null) {
            // 비어있을 때 안내
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("해설이 아직 없습니다.", fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "제출 메뉴에서 '문제 해설'을 눌러 불러오거나, 새로고침을 시도해 보세요.",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
            }
            return
        }

        // 모범 답안
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFF1F5F9)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("모범 답안", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))

                val brief = solution.explanation.takeIf { it.isNotBlank() } ?: "해설이 제공되지 않았습니다."
                Text(brief, fontSize = 14.sp, color = Color(0xFF6B7280), lineHeight = 20.sp)

                Spacer(Modifier.height(16.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0E1627), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = solution.code.ifBlank { "// 정답 코드가 비어있습니다." },
                        color = Color(0xFFE6EDF7),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 상세 해설 영역 (임시)
        Text("상세 해설", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        Text(
            text = solution.explanation.ifBlank { "상세 해설이 제공되지 않았습니다." },
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color(0xFF111827)
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "※ 모범 답안은 선택한 언어(${uiState.language}) 기준입니다.",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}
