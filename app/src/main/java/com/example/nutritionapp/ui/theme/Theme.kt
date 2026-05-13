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
    primary = NavyBlue,
    secondary = VibrantGreen,
    tertiary = RosePink,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = CrispWhite,
    onBackground = CrispWhite,
    onSurface = CrispWhite,
    onSurfaceVariant = Color(0xFFB0BEC5), // Light gray for subtle text in dark mode
    primaryContainer = NavyBlue,
    onPrimaryContainer = CrispWhite,
    surfaceContainer = DarkSurface,
    surfaceVariant = DarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    secondary = VibrantGreen,
    tertiary = RosePink,
    background = IceBlueLight,
    surface = CrispWhite,
    onPrimary = CrispWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextLight,
    primaryContainer = NavyBlue,
    onPrimaryContainer = CrispWhite,
    surfaceContainer = CrispWhite,
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