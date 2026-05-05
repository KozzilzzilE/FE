package com.example.fe.feature.auth.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.R
import com.example.fe.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleLoginClick: (String) -> Unit = {},
    onGithubLoginClick: (Activity) -> Unit = {},
    onSkipLoginClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { onGoogleLoginClick(it) }
            } catch (e: ApiException) {
                Log.e("GoogleLogin", "Google Sign In Failed", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 로고
        Image(
            painter = painterResource(id = R.drawable.logo_main),
            contentDescription = "PocketCo Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "모바일 알고리즘 학습 플랫폼",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 이메일 입력
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "이메일을 입력하세요",
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 입력
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "비밀번호를 입력하세요",
            isPassword = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 로그인 버튼
        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("로그인", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BgPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 구분선
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = BgElevated)
            Text(
                "  또는  ",
                color = TextMuted,
                fontSize = 13.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = BgElevated)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google 로그인
        SocialButton(
            text = "Google로 계속하기",
            iconResId = R.drawable.ic_google_logo,
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val client = GoogleSignIn.getClient(context, gso)
                client.signOut().addOnCompleteListener { launcher.launch(client.signInIntent) }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // GitHub 로그인
        SocialButton(
            text = "GitHub로 계속하기",
            iconResId = R.drawable.ic_github_logo,
            onClick = {
                context.findActivity()?.let { onGithubLoginClick(it) }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 회원가입
        TextButton(onClick = onSignUpClick) {
            Text("회원가입", color = Primary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        // 개발용
        TextButton(onClick = onSkipLoginClick) {
            Text("🛠 테스트: 로그인 건너뛰기", color = TextMuted, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        placeholder = { Text(placeholder, color = TextMuted, fontSize = 15.sp) },
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BgSurface,
            unfocusedContainerColor = BgSurface,
            focusedBorderColor = Primary,
            unfocusedBorderColor = BgElevated,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = Primary
        )
    )
}

@Composable
private fun SocialButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BgElevated)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = { _, _ -> },
        onSignUpClick = {},
        onGoogleLoginClick = {},
        onGithubLoginClick = {},
        onSkipLoginClick = {}
    )
}
