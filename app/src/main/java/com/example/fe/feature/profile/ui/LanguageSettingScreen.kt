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
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.TopBar
import com.example.fe.data.dto.LanguageResult
import com.example.fe.feature.profile.component.SaveButtonBar
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgElevated
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

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
        containerColor = BgPrimary,
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
                .background(BgPrimary)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "문제 풀이 시 기본으로 선택될 프로그래밍 언어를 설정해 주세요.",
                color = TextSecondary,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                languageOptions.forEach { language ->
                    LanguageOptionCard(
                        title = language.name,
                        description = language.description,
                        isSelected = selectedLanguage.equals(language.name, ignoreCase = true),
                        onClick = { selectedLanguage = language.name }
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) BgSurface else BgSurface,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Primary else BgDivider
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isSelected) Primary else TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = description, color = TextMuted, fontSize = 13.sp)
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "선택됨",
                        tint = BgPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
