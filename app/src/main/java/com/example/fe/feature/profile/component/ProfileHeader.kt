package com.example.fe.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextMuted
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

@Composable
fun ProfileHeader(
    userName: String,
    bio: String,
    level: Int,
    profileImgUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(BgSurface)
                    .border(width = 3.dp, color = Primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (profileImgUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profileImgUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "프로필",
                        tint = TextMuted,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = (-2).dp, y = (-2).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.toString(),
                    color = BgPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
