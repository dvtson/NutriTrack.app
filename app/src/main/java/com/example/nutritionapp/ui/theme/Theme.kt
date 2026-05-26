package com.example.nutritionapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    secondary = MacroWater,
    tertiary = MacroProtein,
    background = AppBackground,
    surface = AppSurface,
    onPrimary = TextWhite,
    onBackground = TextPrimary,    // Cấp 1: chữ chính sani
    onSurface = TextPrimary,        // Cấp 1: chữ trên card
    onSurfaceVariant = TextSecondary, // Cấp 2: nhãn, đơn vị
    outline = TextDisabled,         // Cấp 3: placeholder, viền mờ
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = TextWhite,
    surfaceContainer = AppSurface,
    surfaceVariant = AppSurfaceAlt
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = MacroWater,
    tertiary = MacroProtein,
    background = TextWhite,
    surface = TextWhite,
    onPrimary = TextWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextSecondary,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = TextWhite,
    surfaceContainer = TextWhite,
    surfaceVariant = Color(0xFFF1F5F9)
)

@Composable
fun NutritionAppTheme(
    darkTheme: Boolean = true, // Forced Dark Mode
    // We disable dynamic color to force our high-fidelity custom palette 
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}