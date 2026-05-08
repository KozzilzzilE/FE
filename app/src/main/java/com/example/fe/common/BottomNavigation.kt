package com.example.fe.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.R
import com.example.fe.ui.theme.*

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BgPrimary)
            .padding(horizontal = 21.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(BgSurface)
                .border(1.dp, BgElevated, RoundedCornerShape(36.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (selected) Primary else Color.Transparent)
                        .clickable { onNavigate(item.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = if (selected) BgPrimary else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) BgPrimary else TextMuted
                        )
                    }
                }
            }
        }
    }
}

val bottomNavItems = listOf(
    BottomNavItem("홈", R.drawable.ic_home, "home"),
    BottomNavItem("학습", R.drawable.ic_book, "topic"),
    BottomNavItem("문제", R.drawable.ic_list, "problem"),
    BottomNavItem("MY", R.drawable.ic_user, "my"),
)

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        items = bottomNavItems,
        currentRoute = "topic",
        onNavigate = {}
    )
}
