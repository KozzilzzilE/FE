package com.example.fe.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class) // Material 3 (아직 완벽x) API 사용을 위한 어노테이션 
@Composable
fun CommonTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false, //  뒤로 갈수 있게 아이콘 생성
    navigateUp: () -> Unit = {}, // 뒤로 가기 버튼 클릭 시 실행할 함수
    actions: @Composable RowScope.() -> Unit = {} // 오른쪽 아이콘
) { // 함수 정의
    CenterAlignedTopAppBar( // 중앙 정렬 상단 바 컴포즈에서 제공
        title = { Text(text = title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = actions,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CommonTopAppBarPreview() {
    MaterialTheme {
        CommonTopAppBar(
            title = "개념 학습",
            canNavigateBack = true
        )
    }
}
