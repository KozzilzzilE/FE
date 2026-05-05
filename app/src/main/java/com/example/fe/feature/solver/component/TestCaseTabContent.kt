package com.example.fe.feature.solver.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.ui.theme.*

@Composable
fun TestCaseTabContent(
    viewModel: SolverViewModel
) {
    val testCases by viewModel.testCases.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    if (testCases.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("등록된 테스트 케이스가 없습니다.", color = TextMuted)
        }
        return
    }

    val currentCase = testCases.getOrNull(selectedIndex) ?: testCases.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // tcCard
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BgSurface,
                border = BorderStroke(1.dp, BgDivider)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "case ${selectedIndex + 1}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = BgDivider)
                    Spacer(Modifier.height(12.dp))

                    // 입력
                    Text(
                        text = "입력",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(BgPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = currentCase.input,
                            fontSize = 12.sp,
                            color = Color(0xFFA8A29E),
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // 출력
                    Text(
                        text = "출력",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(BgPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = currentCase.expectedOutput,
                            fontSize = 12.sp,
                            color = Color(0xFFA8A29E),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // tcTabPicker
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                testCases.forEachIndexed { index, _ ->
                    val isSelected = index == selectedIndex
                    Surface(
                        modifier = Modifier
                            .size(width = 40.dp, height = 32.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Primary else BgSurface,
                        border = if (isSelected) null else BorderStroke(1.dp, BgDivider),
                        onClick = { selectedIndex = index }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) BgPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // tcsBot - 실행하기 버튼
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.runCode() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = BgPrimary
                )
                Text(
                    text = "실행하기",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BgPrimary
                )
            }
        }
    }
}
