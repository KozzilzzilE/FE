package com.example.fe.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.fe.common.TopBar
import com.example.fe.data.dto.ProfileImageItem
import com.example.fe.feature.profile.component.SaveButtonBar
import com.example.fe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialName: String = "",
    currentProfileImgUrl: String? = null,
    profileImages: List<ProfileImageItem> = emptyList(),
    isLoadingImages: Boolean = false,
    selectedProfileId: Int? = null,
    selectedProfileImgUrl: String? = null,
    isSaving: Boolean = false,
    onBackClick: () -> Unit = {},
    onProfileImageSelect: (Int) -> Unit = {},
    onSaveClick: (String) -> Unit = {}
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var showImagePicker by remember { mutableStateOf(false) }
    var pendingProfileId by remember(selectedProfileId) { mutableStateOf(selectedProfileId) }

    // 선택한 이미지 URL이 있으면 우선 표시, 없으면 현재 프로필 이미지 표시
    val displayImgUrl = selectedProfileImgUrl ?: currentProfileImgUrl

    if (showImagePicker) {
        ModalBottomSheet(
            onDismissRequest = { showImagePicker = false },
            containerColor = BgPrimary,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = null
        ) {
            ProfileImagePickerSheet(
                profileImages = profileImages,
                isLoading = isLoadingImages,
                selectedProfileId = pendingProfileId,
                onSelectProfileId = { pendingProfileId = it },
                onApply = {
                    pendingProfileId?.let { onProfileImageSelect(it) }
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
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImageSection(
                imgUrl = displayImgUrl,
                onClick = {
                    pendingProfileId = selectedProfileId
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
    imgUrl: String?,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF59E0B)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(BgSurface),
                contentAlignment = Alignment.Center
            ) {
                if (imgUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imgUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(112.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "프로필",
                        tint = TextSecondary,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFE8A825)))
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
    profileImages: List<ProfileImageItem>,
    isLoading: Boolean,
    selectedProfileId: Int?,
    onSelectProfileId: (Int) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
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

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFF59E0B), strokeWidth = 2.dp)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(profileImages) { _, item ->
                    AvatarOptionItem(
                        imgUrl = item.imgUrl,
                        isSelected = item.profileId == selectedProfileId,
                        onClick = { onSelectProfileId(item.profileId) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFE8A825)))
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
    imgUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(BgSurface)
                .then(
                    if (isSelected) Modifier.border(2.5.dp, Color(0xFFF59E0B), CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

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
