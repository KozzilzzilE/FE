package com.example.fe.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.ui.theme.*

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
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
            .padding(horizontal = 21.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(36.dp))
                .background(BgSurface)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (selected) Primary else androidx.compose.ui.graphics.Color.Transparent)
                        .clickable { onNavigate(item.route) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selected) BgPrimary else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selected) BgPrimary else TextMuted
                        )
                    }
                }
            }
        }
    }
}

val bottomNavItems = listOf(
    BottomNavItem("홈", Icons.Default.Home, "home"),
    BottomNavItem("학습", Icons.Default.Menu, "topic"),
    BottomNavItem("문제", Icons.Default.Star, "problem"),
    BottomNavItem("MY", Icons.Default.Person, "my"),
)

@Preview(showBackground = true, backgroundColor = 0xFF1C1917)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        items = bottomNavItems,
        currentRoute = "home",
        onNavigate = {}
    )
}
