package com.example.fe.feature.solver.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.model.TestCase

@Composable
fun TestCaseTabContent(
    viewModel: SolverViewModel
) {
    val testCases by viewModel.testCases.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 상단 헤더 (추가 버튼 제거)
        Text(
            text = "테스트케이스 관리",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF374151)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 테스트케이스 카드 리스트
        testCases.forEachIndexed { index, testCase ->
            TestCaseItem(
                number = index + 1,
                testCase = testCase,
                onUpdate = { input, output ->
                    viewModel.updateTestCase(testCase.id, input, output)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun TestCaseItem(
    number: Int,
    testCase: TestCase,
    onUpdate: (String?, String?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6EEF7)),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // 제목
            Text(
                text = "테스트 $number",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("입력", fontSize = 13.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(8.dp))
            TestCaseInputField(
                value = testCase.input,
                onValueChange = { onUpdate(it, null) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("예상 출력", fontSize = 13.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(8.dp))
            TestCaseInputField(
                value = testCase.expectedOutput,
                onValueChange = { onUpdate(null, it) }
            )
        }
    }
}

@Composable
private fun TestCaseInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF1F2937),
                fontFamily = FontFamily.Monospace
            )
        )
    }
}
