package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.TopBar
import com.example.fe.feature.profile.component.SaveButtonBar
import com.example.fe.ui.theme.*

// 아바타 옵션 (API 연동 전 placeholder — 동물 이모지 + 색상)
private val avatarOptions = listOf(
    "👤" to Color(0xFF292524),
    "🐶" to Color(0xFF3B2F1E),
    "🐱" to Color(0xFF2A1F2A),
    "🐰" to Color(0xFF2A2032),
    "🦝" to Color(0xFF1F2A1F),
    "🐹" to Color(0xFF2A201A),
    "🌟" to Color(0xFF1A1F2A)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialName: String = "",
    isSaving: Boolean = false,
    onBackClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {}
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var selectedAvatarIndex by remember { mutableIntStateOf(0) }
    var showImagePicker by remember { mutableStateOf(false) }
    var pendingAvatarIndex by remember { mutableIntStateOf(0) }

    if (showImagePicker) {
        ModalBottomSheet(
            onDismissRequest = { showImagePicker = false },
            containerColor = BgPrimary,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = null
        ) {
            ProfileImagePickerSheet(
                selectedIndex = pendingAvatarIndex,
                onSelectIndex = { pendingAvatarIndex = it },
                onApply = {
                    selectedAvatarIndex = pendingAvatarIndex
                    showImagePicker = false
                },
                onDismiss = { showImagePicker = false }
            )
        }
    }

    Scaffold(
        containerColor = BgPrimary,
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
                .background(BgPrimary)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImageSection(
                avatarIndex = selectedAvatarIndex,
                onClick = {
                    pendingAvatarIndex = selectedAvatarIndex
                    showImagePicker = true
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(BgSurface)
                    .padding(horizontal = 20.dp, vertical = 22.dp),
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

@Composable
private fun ProfileImageSection(
    avatarIndex: Int,
    onClick: () -> Unit
) {
    val (emoji, bgColor) = avatarOptions[avatarIndex]

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        // 외곽 앰버 테두리 원
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF59E0B)),
            contentAlignment = Alignment.Center
        ) {
            // 내부 아바타 원
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                if (avatarIndex == 0) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "프로필",
                        tint = TextSecondary,
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    Text(text = emoji, fontSize = 48.sp)
                }
            }
        }

        // 카메라 편집 버튼
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFF59E0B), Color(0xFFE8A825))
                    )
                )
                .border(3.dp, BgPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = "프로필 사진 변경",
                tint = Color(0xFF0A0A0A),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ProfileImagePickerSheet(
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "프로필 이미지 선택",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "닫기",
                    tint = TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        HorizontalDivider(color = BgSurface)

        // 아바타 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(avatarOptions) { index, (emoji, bgColor) ->
                AvatarOptionItem(
                    emoji = emoji,
                    bgColor = bgColor,
                    isSelected = index == selectedIndex,
                    isFirst = index == 0,
                    onClick = { onSelectIndex(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 적용 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFF59E0B), Color(0xFFE8A825))
                    )
                )
                .clickable { onApply() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "적용",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1917)
            )
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun AvatarOptionItem(
    emoji: String,
    bgColor: Color,
    isSelected: Boolean,
    isFirst: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(bgColor)
                .then(
                    if (isSelected) Modifier.border(2.5.dp, Color(0xFFF59E0B), CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isFirst) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(44.dp)
                )
            } else {
                Text(text = emoji, fontSize = 40.sp)
            }
        }

        // 선택 체크 배지
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF59E0B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color(0xFF1C1917),
                    modifier = Modifier.size(14.dp)
                )
            }
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
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BgElevated)
                .padding(horizontal = 16.dp, vertical = 15.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 17.sp, color = TextPrimary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(text = placeholder, color = TextMuted, fontSize = 17.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}
