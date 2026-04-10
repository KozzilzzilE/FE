package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.TopBar
import com.example.fe.data.dto.LanguageResult
import com.example.fe.feature.profile.component.SaveButtonBar

data class LanguageOptionUi(
    val id: Int,
    val name: String,
    val description: String
)

@Composable
fun LanguageSettingScreen(
    initialLanguage: String = "Python",
    languages: List<LanguageResult>,
    isSaving: Boolean = false,
    onBackClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {}
) {
    val languageOptions = remember(languages) {
        languages.map {
            LanguageOptionUi(
                id = it.languageId,
                name = it.languageName,
                description = getLanguageDescription(it.languageName)
            )
        }
    }

    var selectedLanguage by remember(initialLanguage) { mutableStateOf(initialLanguage) }

    LaunchedEffect(initialLanguage) {
        selectedLanguage = initialLanguage
    }

    Scaffold(
        containerColor = Color(0xFFF7F8FC),
        topBar = {
            TopBar(
                title = "선호 언어 설정",
                showBackIcon = true,
                showHomeIcon = false,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            SaveButtonBar(
                isLoading = isSaving,
                onClick = { onSaveClick(selectedLanguage) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FC))
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "문제 풀이 시 기본으로 선택될 프로그래밍 언어를 설정해 주세요.",
                color = Color(0xFF6B7280),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                languageOptions.forEach { language ->
                    LanguageOptionCard(
                        title = language.name,
                        description = language.description,
                        isSelected = selectedLanguage.equals(language.name, ignoreCase = true),
                        onClick = {
                            selectedLanguage = language.name
                        }
                    )
                }
            }
        }
    }
}

private fun getLanguageDescription(name: String): String {
    return when (name.uppercase()) {
        "PYTHON" -> "알고리즘 코딩 테스트 추천"
        "JAVA" -> "엔터프라이즈 환경 추천"
        "C++", "CPP" -> "가장 빠른 실행 속도"
        "JAVASCRIPT" -> "프론트엔드/백엔드 범용"
        "GO" -> "효율적인 동시성 처리"
        "KOTLIN" -> "안드로이드 개발 추천"
        else -> "선호 언어로 사용할 수 있어요"
    }
}

@Composable
private fun LanguageOptionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF7AA7F8) else Color(0xFFE5E7EB)
    val containerColor = if (isSelected) Color(0xFFF3F7FF) else Color.White
    val titleColor = if (isSelected) Color(0xFF5B8DEF) else Color(0xFF1F2937)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    color = Color(0xFF9CA3AF),
                    fontSize = 13.sp
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(
                            color = Color(0xFF6E93E6),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "선택됨",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LanguageSettingScreenPreview() {
    LanguageSettingScreen(
        initialLanguage = "Python",
        languages = listOf(
            LanguageResult(languageId = 1, languageName = "PYTHON"),
            LanguageResult(languageId = 2, languageName = "JAVA"),
            LanguageResult(languageId = 3, languageName = "C++")
        )
    )
}