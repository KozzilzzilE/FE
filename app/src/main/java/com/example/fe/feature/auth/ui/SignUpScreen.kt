package com.example.fe.feature.auth.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation
import com.example.fe.feature.auth.component.InputSection
import com.example.fe.feature.auth.component.SocialLoginButton

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.example.fe.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    onSignUpComplete: (String, String, String, String) -> Unit, // 이름, 이메일, 비번, 사용언어
    onGoogleSignUpClick: (String) -> Unit = {},
    onGithubSignUpClick: (Activity) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // 비밀번호 확인
    
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("Java", "Python", "JavaScript", "C++")
    
    val context = LocalContext.current
    
    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    onGoogleSignUpClick(idToken)
                } else {
                    Log.e("GoogleSignUp", "idToken is null")
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignUp", "Google Sign In Failed", e)
            }
        }
    }

    val scrollState = rememberScrollState() // 스크롤 기능 구현을 위함

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) { // 화살표 아이콘
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "회원 가입",
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
                .verticalScroll(scrollState)
        ) {
            // 1. 소셜 로그인 버튼들
            SocialLoginButton(text = "Github로 회원가입", iconResId =  null, onClick = {
                val activity = context as? Activity
                if (activity != null) {
                    onGithubSignUpClick(activity)
                } else {
                    Log.e("GithubSignUp", "Context is not an Activity")
                }
            }) // 아이콘은 일단 생략
            Spacer(modifier = Modifier.height(12.dp))
            SocialLoginButton(text = "Google로 회원가입", iconResId = null, onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            })

            Spacer(modifier = Modifier.height(24.dp))

            // 2. 구분선 (또는)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
                Text(
                    text = "또는",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. 입력 폼
            InputSection(label = "이름", placeholder = "이름을 입력해주세요", value = name, onValueChange = { name = it }, required = true)
            Spacer(modifier = Modifier.height(16.dp))
            
            InputSection(label = "이메일", placeholder = "이메일을 입력해주세요", value = email, onValueChange = { email = it }, required = true)
            Spacer(modifier = Modifier.height(16.dp))
            
            InputSection(label = "비밀번호", placeholder = "비밀번호", value = password, onValueChange = { password = it }, isPassword = true, required = true)
            Spacer(modifier = Modifier.height(16.dp))
            
            InputSection(label = "비밀번호 확인", placeholder = "비밀번호 확인", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true, required = true)
            
            Spacer(modifier = Modifier.height(24.dp))

            // 4. 사용 언어 (드롭다운)
            Text(text = "사용 언어", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .run { 
                            shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp)) 
                                .background(Color.White, RoundedCornerShape(8.dp)) // 그림자 위 배경
                        }
                        .background(Color.White) // 배경 확실히
                        .clickable { expanded = true }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceBetween,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Text(if (language.isEmpty()) "Java" else language, fontSize = 16.sp)
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

            Spacer(modifier = Modifier.height(32.dp))

            // 5. 완료 버튼
            Button(
                onClick = { onSignUpComplete(name, email, password, language.ifEmpty { "Java" }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp), // 내부 여백 제거
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent 
                )
            ) {
                // 그라데이션 버튼 배경
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
                    Text("회원 가입 완료", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onNavigateBack = {}, onSignUpComplete = { _, _, _, _ -> }, onGoogleSignUpClick = {}, onGithubSignUpClick = {})
}
