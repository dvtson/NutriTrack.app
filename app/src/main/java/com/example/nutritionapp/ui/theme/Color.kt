package com.example.nutritionapp.ui.theme

import androidx.compose.ui.graphics.Color

// Backgrounds
val AppBackground = Color(0xFF101214)
val AppSurface    = Color(0xFF1B2127)
val AppSurfaceAlt = Color(0xFF222C34)

// Text hierarchy
val TextPrimary   = Color(0xFFFFFFFF)   // Tiêu đề, số liệu chính
val TextSecondary = Color(0xFFAAB4BF)   // Đơn vị, nhãn phụ
val TextDisabled  = Color(0xFF616E7A)   // Placeholder, hint
val TextWhite     = Color(0xFFFFFFFF)
val TextDark      = Color(0xFF101214)

// Primary
val PrimaryGreen  = Color(0xFF00C897)

// Macros
val MacroProtein  = Color(0xFFEF476F)
val MacroCarbs    = Color(0xFF2EC4B6)
val MacroFat      = Color(0xFFF7B538)
val MacroWater    = Color(0xFF20A4F3)

// Legacy aliases
val CrispWhite        = TextWhite
val AppleBlack        = AppBackground
val AppleDarkGray     = AppSurface
val AppleLightGray    = AppSurfaceAlt
val AppleGreen        = PrimaryGreen
val AppleBlue         = MacroWater
val AppleRed          = MacroProtein
val AppleCyan         = MacroCarbs
val AppleOrange       = MacroFat
val TextGray          = TextSecondary
val NavyBlue          = PrimaryGreen
val IceBlue           = AppSurfaceAlt
val MaroonRed         = MacroProtein
val GradientNavyGreen = listOf(PrimaryGreen, MacroCarbs)
val TealAccent        = MacroWater
val VibrantGreen      = PrimaryGreen
val TextLight         = TextSecondary