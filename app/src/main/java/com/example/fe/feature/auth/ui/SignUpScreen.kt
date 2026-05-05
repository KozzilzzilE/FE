package com.example.fe.feature.auth.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.R
import com.example.fe.feature.auth.component.InputSection
import com.example.fe.feature.auth.component.SignUpLanguageDropdown
import com.example.fe.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private fun Context.findActivityFromSignUp(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityFromSignUp()
    else -> null
}

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    onSignUpComplete: (String, String, String, String) -> Unit,
    onGoogleSignUpClick: (String) -> Unit = {},
    onGithubSignUpClick: (Activity) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("Python") }
    val context = LocalContext.current

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.idToken?.let { onGoogleSignUpClick(it) }
            } catch (e: ApiException) {
                Log.e("GoogleSignUp", "Failed", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = TextPrimary, modifier = Modifier.size(20.dp))
            }
            Text(
                text = "회원 가입",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // GitHub 소셜 버튼
            SignUpSocialButton(
                text = "Github로 회원가입",
                iconResId = R.drawable.ic_github_logo
            ) {
                context.findActivityFromSignUp()?.let { onGithubSignUpClick(it) }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Google 소셜 버튼
            SignUpSocialButton(
                text = "Google로 회원가입",
                iconResId = R.drawable.ic_google_logo
            ) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail().build()
                val client = GoogleSignIn.getClient(context, gso)
                client.signOut().addOnCompleteListener { googleLauncher.launch(client.signInIntent) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 구분선
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BgElevated)
                Text("  또는  ", color = TextMuted, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = BgElevated)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 입력 폼
            SignUpFieldLabel("이름", required = true)
            Spacer(Modifier.height(6.dp))
            SignUpTextField(value = name, onValueChange = { name = it }, placeholder = "이름을 입력하세요", isFocused = name.isNotEmpty())

            Spacer(Modifier.height(16.dp))
            SignUpFieldLabel("이메일", required = true)
            Spacer(Modifier.height(6.dp))
            SignUpTextField(value = email, onValueChange = { email = it }, placeholder = "이메일을 입력하세요")

            Spacer(Modifier.height(16.dp))
            SignUpFieldLabel("비밀번호", required = true)
            Spacer(Modifier.height(6.dp))
            SignUpTextField(value = password, onValueChange = { password = it }, placeholder = "비밀번호를 입력하세요", isPassword = true)

            Spacer(Modifier.height(16.dp))
            SignUpFieldLabel("비밀번호 확인", required = true)
            Spacer(Modifier.height(6.dp))
            SignUpTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "비밀번호를 확인하세요", isPassword = true)

            Spacer(Modifier.height(16.dp))
            SignUpFieldLabel("주 사용 언어", required = true)
            Spacer(Modifier.height(6.dp))
            SignUpLanguageDropdown(
                selectedLanguage = language,
                onLanguageSelected = { language = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { onSignUpComplete(name, email, password, language) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("가입 완료", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BgPrimary)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SignUpFieldLabel(label: String, required: Boolean = false) {
    Row {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        if (required) {
            Text(" *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
        }
    }
}

@Composable
private fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isFocused: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().height(54.dp),
        placeholder = { Text(placeholder, color = TextMuted, fontSize = 15.sp) },
        visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation()
                               else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BgSurface,
            unfocusedContainerColor = BgSurface,
            focusedBorderColor = Primary,
            unfocusedBorderColor = if (isFocused) Primary else Color.Transparent,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = Primary
        )
    )
}

@Composable
private fun SignUpSocialButton(text: String, iconResId: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent)
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
fun SignUpScreenPreview() {
    SignUpScreen(onNavigateBack = {}, onSignUpComplete = { _, _, _, _ -> })
}
