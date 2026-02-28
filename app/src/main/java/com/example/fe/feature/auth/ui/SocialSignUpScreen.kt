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
    var expanded by remember { mutableStateOf(false) }
    
    val languages = listOf("Java", "Python", "JavaScript", "C++")

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "추가 정보 입력",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
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
            
            Text("원활한 서비스 이용을 위해\n추가 정보를 입력해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            
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
            Text(text = "주 사용 언어", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .run { 
                            shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp)) 
                                .background(Color.White, RoundedCornerShape(8.dp))
                        }
                        .background(Color.White)
                        .clickable { expanded = true }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceBetween,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Text(
                             text = if (language.isEmpty()) "언어를 선택해주세요" else language, 
                             fontSize = 16.sp, 
                             color = if(language.isEmpty()) Color.Gray else Color.Black
                         )
                         Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                     }
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(Color.White)
                ) {
                    languages.forEach { selectedLanguage ->
                        DropdownMenuItem(
                            text = { Text(selectedLanguage) },
                            onClick = { 
                                language = selectedLanguage
                                expanded = false 
                            }
                        )
                    }
                }
            }

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
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4A90E2), Color(0xFF50C9C3))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("가입 완료", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
