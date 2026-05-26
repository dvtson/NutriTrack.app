package com.example.nutritionapp.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Nền & Bề mặt ───────────────────────────────────────────────
val AppBackground  = Color(0xFF101214)  // Đen Than/Charcoal – nền chính, mềm, ấm
val AppSurface     = Color(0xFF1B2127)  // Xám Slate – nền thẻ/card
val AppSurfaceAlt  = Color(0xFF222C34)  // Nền thẻ phụ / elevated

// ─── Văn bản (White + Alpha – chuẩn Dark Mode) ─────────────────
// Cấp 1 – Tiêu đề, Số liệu chính, Tên món ăn      White 100%
val TextPrimary    = Color(0xFFFFFFFF)
// Cấp 2 – Đơn vị (kcal, g, ml), mục tiêu, "Còn lại"  White ~60%
val TextSecondary  = Color(0xFFAAB4BF)
// Cấp 3 – Placeholder / Hint trong khung nhập liệu    White ~38%
val TextDisabled   = Color(0xFF616E7A)
// Tiện ích
val TextWhite      = Color(0xFFFFFFFF)
val TextDark       = Color(0xFF101214)  // Cho Light Mode

// ─── Màu nhấn chính (Primary) ────────────────────────────────────
val PrimaryGreen   = Color(0xFF00C897)  // Xanh Jade/Ngọc bích – sức khỏe tự nhiên

// ─── Macros (Rich / Jewel-toned) ─────────────────────────────────
val MacroProtein   = Color(0xFFEF476F)  // Hồng San Hô – Protein
val MacroCarbs     = Color(0xFF2EC4B6)  // Xanh Ngọc Turquoise – Carbs
val MacroFat       = Color(0xFFF7B538)  // Vàng Hổ Phách – Fat
val MacroWater     = Color(0xFF20A4F3)  // Xanh Bầu Trời – Nước

// ─── Backward Compatibility Aliases (giữ cho các màn hình cũ) ────
val CrispWhite         = TextWhite
val AppleBlack         = AppBackground
val AppleDarkGray      = AppSurface
val AppleLightGray     = AppSurfaceAlt
val AppleGreen         = PrimaryGreen
val AppleBlue          = MacroWater
val AppleRed           = MacroProtein
val AppleCyan          = MacroCarbs
val AppleOrange        = MacroFat
val TextGray           = TextSecondary
val NavyBlue           = PrimaryGreen
val IceBlue            = AppSurfaceAlt
val MaroonRed          = MacroProtein
val GradientNavyGreen  = listOf(PrimaryGreen, MacroCarbs)
val TealAccent         = MacroWater
val VibrantGreen       = PrimaryGreen
val TextLight          = TextSecondary