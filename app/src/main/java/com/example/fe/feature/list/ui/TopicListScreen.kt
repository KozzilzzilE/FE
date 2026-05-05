package com.example.fe.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fe.common.BottomNavigationBar
import com.example.fe.common.bottomNavItems
import com.example.fe.navigation.Routes
import com.example.fe.feature.list.TopicViewModel
import com.example.fe.feature.list.TopicUiState
import com.example.fe.feature.list.component.TopicCard
import com.example.fe.ui.theme.*

@Composable
fun TopicListScreen(
    viewModel: TopicViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = BgPrimary,
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = "topic",
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is TopicUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            is TopicUiState.Error -> {}
            is TopicUiState.Success -> {
                val topics = state.topics
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 헤더
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "알고리즘 학습",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${topics.size}개 주제",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 토픽 목록
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        topics.forEachIndexed { index, topic ->
                            TopicCard(
                                title = topic.displayName,
                                index = index,
                                onClick = {
                                    onNavigate(Routes.step(topic.topicId, topic.displayName))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
