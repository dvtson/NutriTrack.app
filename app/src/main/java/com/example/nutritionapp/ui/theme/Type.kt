package com.example.nutritionapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val interFontFamily = FontFamily.SansSerif

// ─── Hằng số "Sàn" kích thước chữ ───────────────────────────────
// TUYỆT ĐỐI KHÔNG dùng fontSize dưới MIN_FONT_SIZE ở bất kỳ màn hình nào
val MIN_FONT_SIZE = 12.sp

// ─── Bảng Typography chuẩn (5 cấp phân cấp thông tin) ──────────
//
//  Cấp     | Size   | Weight    | Color (ánh xạ)        | Ứng dụng
//  ─────────────────────────────────────────────────────────────────
//  Heading  | 28sp   | Bold 700  | White 100% (onBg)    | Chỉ số BMI to, Tổng Calo chính
//  Title    | 17sp   | SemiBold  | White 100% (onBg)    | Tên màn hình, tên món ăn chính
//  Body     | 15sp   | Regular   | White 90% (onSurface)| Chữ input, text hiển thị chung
//  Secondary| 13sp   | Regular   | White 60% (onSVarnt) | Đơn vị kcal, g, số mục tiêu
//  Caption  | 12sp ▲ | Regular   | White 60% (outline)  | Dòng chú thích nhỏ nhất

val Typography = Typography(

    // Cấp 1 – Heading / Big Value (28sp Bold)
    // Dùng cho: Số calo tổng lớn, chỉ số BMI, tiêu đề trang chính
    displayMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.5).sp
    ),

    // Cấp 2 – Title / Label (17sp SemiBold)
    // Dùng cho: Tên màn hình (Dashboard, Tracker...), tên món ăn chính
    titleLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Cấp 2b – Title Medium (15sp SemiBold)
    // Dùng cho: Tên nhóm bữa ăn (Bữa sáng, Bữa trưa...)
    titleMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Cấp 3 – Body (15sp Regular)
    // Dùng cho: Chữ nhập vào input, text hiển thị chung, nội dung card
    bodyLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Cấp 3b – Body Medium (14sp Regular)
    // Dùng cho: Các dòng text thứ cấp trong card, thông tin bổ sung
    bodyMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Cấp 4 – Secondary / Caption (13sp Regular)
    // Dùng cho: Đơn vị kcal, g, ml; số mục tiêu "/ 2310"; ngày tháng
    bodySmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),

    // Cấp 5 – Caption / Hint (12sp Regular) ← SÀN TỐI THIỂU
    // Dùng cho: Dòng chú thích "Đ: 21g - TĐ: 80g", placeholder input
    labelSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    // Label Medium – cho nút bấm, chip
    labelMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)