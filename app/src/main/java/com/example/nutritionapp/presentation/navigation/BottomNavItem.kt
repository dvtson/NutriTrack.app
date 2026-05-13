package com.example.nutritionapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("dashboard", "Trang chủ", Icons.Rounded.Home)
    object Tracker : BottomNavItem("tracker", "Theo dõi", Icons.Rounded.List)
    object History : BottomNavItem("history", "Lịch sử", Icons.Rounded.DateRange)
    object AddMeal : BottomNavItem("search", "Thêm bữa", Icons.Rounded.AddCircle)
}
