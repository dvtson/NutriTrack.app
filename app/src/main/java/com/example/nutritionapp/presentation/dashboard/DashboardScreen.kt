package com.example.nutritionapp.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.Add
import com.example.nutritionapp.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutritionapp.data.local.entity.MealEntity
import com.example.nutritionapp.presentation.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProfile: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val todayRecord by viewModel.todayRecord.collectAsState()
    val meals by viewModel.todayMeals.collectAsState()

    if (userProfile == null) {
        com.example.nutritionapp.presentation.onboarding.OnboardingScreen(
            existingUser = null,
            onSaveProfile = { user -> 
                viewModel.saveProfile(user)
            },
            onCancel = null
        )
        return
    }

    // Temporary default values if profile is not set (for preview feel)
    val targetCals = userProfile?.targetCalories?.takeIf { it > 0 } ?: 2000
    val currentCals = todayRecord?.totalCalories ?: 0
    val targetProtein = userProfile?.targetProtein?.takeIf { it > 0 } ?: 150
    val currentProtein = todayRecord?.totalProtein ?: 0
    val targetCarbs = userProfile?.targetCarbs?.takeIf { it > 0 } ?: 200
    val currentCarbs = todayRecord?.totalCarbs ?: 0
    val targetFat = userProfile?.targetFat?.takeIf { it > 0 } ?: 65
    val currentFat = todayRecord?.totalFat ?: 0
    val waterIntake = todayRecord?.waterIntakeMl ?: 0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bảng điều khiển\nDinh dưỡng", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Hồ sơ", modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            item {
                CalorieRingCard(currentCals, targetCals)
            }
            item {
                MacroStatsCard(
                    currentProtein, targetProtein,
                    currentCarbs, targetCarbs,
                    currentFat, targetFat
                )
            }
            item {
                WaterTrackerCard(
                    waterMl = waterIntake,
                    onAddWater = { viewModel.addWater(250) },
                    onRemoveWater = { viewModel.addWater(-250) }
                )
            }
            item {
                Text(
                    text = "Bữa ăn hôm nay",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            if (meals.isEmpty()) {
                item {
                    Text("Chưa có bữa ăn nào. Hãy thao tác với tab 'Thêm bữa' ở thanh công cụ dưới cùng để bắt đầu!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(meals) { meal ->
                    MealItemCard(meal) {
                        viewModel.deleteMeal(meal)
                    }
                }
            }
        }
    }
}

@Composable
fun CalorieRingCard(current: Int, target: Int) {
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
    val remaining = (target - current).coerceAtLeast(0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(28.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = Brush.linearGradient(GradientNavyGreen),
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$current", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "/ $target", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "kcal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Còn lại", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                Text("$remaining", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("kcal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MacroStatsCard(
    cProt: Int, tProt: Int,
    cCarb: Int, tCarb: Int,
    cFat: Int, tFat: Int
) {
    val proteinDiff = tProt - cProt
    val carbDiff = tCarb - cCarb
    val fatDiff = tFat - cFat
    
    val insightMessage = buildString {
        if (proteinDiff > tProt * 0.3) append("💡 Đang thiếu lượng lớn Đạm. ")
        if (carbDiff > tCarb * 0.3) append("💡 Bạn cần thêm năng lượng từ Tinh bột. ")
        if (cFat > tFat) append("⚠️ Bạn đã nạp hố mức Chất béo! ")
    }.trim()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroItem("Đạm", cProt, tProt, Brush.linearGradient(listOf(Color(0xFFE91E63), Color(0xFFFFCCBC))))
                MacroItem("Tinh bột", cCarb, tCarb, Brush.linearGradient(listOf(Color(0xFF03A9F4), Color(0xFFB2EBF2))))
                MacroItem("Béo", cFat, tFat, Brush.linearGradient(listOf(Color(0xFFFFC107), Color(0xFFFFF9C4))))
            }
            if (insightMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = insightMessage, 
                        fontSize = 13.sp, 
                        color = MaterialTheme.colorScheme.onPrimaryContainer, 
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MacroItem(label: String, current: Int, target: Int, brush: Brush) {
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(CircleShape)
                    .background(brush)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("$current / $target g", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun WaterTrackerCard(waterMl: Int, onAddWater: () -> Unit, onRemoveWater: () -> Unit) {
    val glasses = waterMl / 250
    val targetGlasses = 8
    val progress = (glasses.toFloat() / targetGlasses).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Theo dõi Nước", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$waterMl ml ", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
                    Text("($glasses / $targetGlasses cốc)", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onRemoveWater,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("-", fontWeight = FontWeight.Medium, color = TealAccent, fontSize = 20.sp)
                }
                Button(
                    onClick = onAddWater,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Text("+", fontWeight = FontWeight.Medium, color = VibrantGreen, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun MealItemCard(meal: MealEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val typeLabel = when(meal.mealType) {
                    "BREAKFAST" -> "BREAKFAST"
                    "LUNCH" -> "LUNCH"
                    "DINNER" -> "DINNER"
                    "SNACK" -> "SNACK"
                    else -> meal.mealType
                }
                Text(meal.foodName, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text(typeLabel, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 8.dp)) {
                Text("${meal.calories} kcal", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Text("Đ: ${meal.protein}g • TB: ${meal.carbs}g • B: ${meal.fat}g", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Meal", tint = MaroonRed)
            }
        }
    }
}
