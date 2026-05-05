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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

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
            .padding(16.dp)
    ) {
        // 헤더 + 새로고침
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("문제 해설", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            TextButton(onClick = { viewModel.loadSolution() }) {
                Text("새로고침", color = Primary)
            }
        }

        Spacer(Modifier.height(12.dp))

        // 로딩 상태
        if (uiState.isLoadingSolution) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = Primary
                    )
                    Text("모범 답안을 불러오는 중...", color = TextSecondary)
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        val solution = uiState.solution
        if (solution == null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "해설이 아직 없습니다.",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "제출 메뉴에서 '문제 해설'을 눌러 불러오거나, 새로고침을 시도해 보세요.",
                        fontSize = 13.sp,
                        color = TextMuted,
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
            color = BgSurface
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("모범 답안", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                Spacer(Modifier.height(8.dp))

                val brief = solution.explanation.takeIf { it.isNotBlank() } ?: "해설이 제공되지 않았습니다."
                Text(brief, fontSize = 14.sp, color = TextSecondary, lineHeight = 20.sp)

                Spacer(Modifier.height(16.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(CodeBgDark, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = solution.code.ifBlank { "// 정답 코드가 비어있습니다." },
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 상세 해설 영역
        Text("상세 해설", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
        Spacer(Modifier.height(12.dp))

        Text(
            text = solution.explanation.ifBlank { "상세 해설이 제공되지 않았습니다." },
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = TextSecondary
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "※ 모범 답안은 선택한 언어(${uiState.language}) 기준입니다.",
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}
