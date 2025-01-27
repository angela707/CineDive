package com.adimovska.cinedive.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFF0D47A1),
    tertiary = Color(0xFF80D8FF),
    background = Color(0xFF2A2826),
    surface = Color(0xFF3B3937),
    onPrimary = Color.White,
    onSecondary = Color(0xFFB3E5FC),
    onTertiary = Color(0xFFEFEDEC),
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF64B5F6),
    secondary = Color(0xFF1565C0),
    tertiary = Color(0xFFB3E5FC),
    background = Color(0xFFEFEDEC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF706E6B),
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun CineDiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}