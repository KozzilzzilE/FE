package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.TopBar
import com.example.fe.feature.profile.component.SaveButtonBar

@Composable
fun EditProfileScreen(
    initialName: String = "",
    isSaving: Boolean = false,
    onBackClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {}
) {
    var name by remember(initialName) { mutableStateOf(initialName) }

    Scaffold(
        containerColor = Color(0xFFF7F8FC),
        topBar = {
            TopBar(
                title = "개인 정보 수정",
                showBackIcon = true,
                showHomeIcon = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            SaveButtonBar(
                isLoading = isSaving,
                onClick = { onSaveClick(name) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FC))
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImageSection()

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    ProfileInputField(
                        label = "이름",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "이름을 입력하세요"
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileImageSection() {
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(118.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7AA7F8),
                            Color(0xFF66D1C9)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF7F8FC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "프로필",
                    tint = Color(0xFFC4C7D0),
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .offset(x = (-2).dp, y = (-2).dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = "프로필 사진 변경",
                tint = Color(0xFF8B95A1),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2F4F8))
                .padding(horizontal = 16.dp, vertical = 15.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    color = Color(0xFF111827)
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF9CA3AF),
                            fontSize = 17.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(
        initialName = "김개발"
    )
}