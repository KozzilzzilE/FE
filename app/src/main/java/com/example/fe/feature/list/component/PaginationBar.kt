package com.example.fe.feature.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    if (totalPages <= 0) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ChevronLeft,
            contentDescription = "이전 페이지",
            tint = if (currentPage > 1) Color(0xFF64748B) else Color(0xFFCBD5E1),
            modifier = Modifier
                .clickable(enabled = currentPage > 1) {
                    onPageChange(currentPage - 1)
                }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        for (page in 1..totalPages) {
            val isSelected = page == currentPage

            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) Color(0xFF7EA3F7) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onPageChange(page) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    color = if (isSelected) Color.White else Color(0xFF64748B),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "다음 페이지",
            tint = if (currentPage < totalPages) Color(0xFF64748B) else Color(0xFFCBD5E1),
            modifier = Modifier
                .clickable(enabled = currentPage < totalPages) {
                    onPageChange(currentPage + 1)
                }
                .padding(8.dp)
        )
    }
}