package com.example.fe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary          = Primary,
    onPrimary        = BgPrimary,
    primaryContainer = PrimaryDim15,
    background       = BgPrimary,
    onBackground     = TextPrimary,
    surface          = BgSurface,
    onSurface        = TextPrimary,
    surfaceVariant   = BgElevated,
    onSurfaceVariant = TextSecondary,
    outline          = BgDivider,
    error            = Error,
    onError          = TextPrimary,
)

@Composable
fun FETheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = Typography,
        content     = content
    )
}
