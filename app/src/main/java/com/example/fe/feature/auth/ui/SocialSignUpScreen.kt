package com.example.fe.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.auth.component.InputSection
import com.example.fe.feature.auth.component.SignUpLanguageDropdown
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.TextPrimary

/*
"""
    소셜 회원 가입을 위한 추가 정보 입력 페이지
""" 
*/

@Composable
fun SocialSignUpScreen(
    initialName: String,
    initialEmail: String,
    onNavigateBack: () -> Unit,
    onSignUpComplete: (String, String, String) -> Unit // 이름, 이메일, 언어
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var language by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary, modifier = Modifier.size(20.dp))
                }
                Text(
                    text = "추가정보 입력",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "원활한 서비스 이용을 위해\n추가 정보를 입력해주세요.", 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Normal, 
                color = com.example.fe.ui.theme.TextMuted,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            InputSection(
                label = "이름",
                placeholder = "이름을 입력해주세요",
                value = name,
                onValueChange = { name = it },
                required = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            InputSection(
                label = "이메일",
                placeholder = "이메일을 입력해주세요",
                value = email,
                onValueChange = { email = it },
                required = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 사용 언어 (드롭다운)
            Row {
                Text(text = "주 사용 언어", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(text = " *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = com.example.fe.ui.theme.Primary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            SignUpLanguageDropdown(
                selectedLanguage = language,
                onLanguageSelected = { language = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 가입 완료 버튼
            Button(
                onClick = { 
                    val finalLanguage = if (language.isEmpty()) "Java" else language
                    onSignUpComplete(name, email, finalLanguage) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.fe.ui.theme.Primary)
            ) {
                Text("가입 완료", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BgPrimary)
            }
        }
    }
}
