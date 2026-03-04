package com.example.fe.feature.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.common.AppConstants

@Composable
fun SignUpLanguageDropdown(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    // label 출력
    Text(text = "사용 언어 (선택)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .run { 
                    shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp)) 
                        .background(Color.White, RoundedCornerShape(8.dp)) // 그림자 위 배경
                }
                .background(Color.White) // 배경 확실히
                .clickable { expanded = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Text(
                     text = if (selectedLanguage.isEmpty()) "언어를 선택해주세요" else selectedLanguage, 
                     fontSize = 16.sp, 
                     color = if(selectedLanguage.isEmpty()) Color.Gray else Color.Black
                 )
                 Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
             }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Color.White)
        ) {
            AppConstants.SUPPORTED_LANGUAGES.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang) },
                    onClick = { 
                        onLanguageSelected(lang)
                        expanded = false 
                    }
                )
            }
        }
    }
}
