package com.example.nutritionapp.presentation.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.core.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutritionapp.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutritionapp.data.local.entity.WeightRecordEntity
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(viewModel: TrackerViewModel = hiltViewModel()) {
    val dailyRecords by viewModel.dailyRecords.collectAsState()
    val weightRecords by viewModel.weightRecords.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val heightCm = userProfile?.heightCm ?: 170.0
    val targetCals = userProfile?.targetCalories?.takeIf { it > 0 } ?: 2000

    Scaffold() { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Theo dõi hoạt động",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                WeeklyAnalysisCard(records = dailyRecords, targetCals = targetCals)
            }
            item {
                BMITrackerCard(
                    weightRecords = weightRecords,
                    userProfile = userProfile,
                    onLogWeight = { weight -> viewModel.logWeight(weight) }
                )
            }
        }
    }
}

@Composable
fun WeeklyAnalysisCard(records: List<com.example.nutritionapp.data.local.entity.DailyRecordEntity>, targetCals: Int) {
    val chartData = if (records.isEmpty()) {
        val today = java.time.LocalDate.now()
        listOf(Pair(String.format("%02d/%02d", today.dayOfMonth, today.monthValue), 0))
    } else {
        records.sortedBy { it.dateString }.takeLast(7).map { record ->
            val dateParts = record.dateString.split("-")
            val label = if(dateParts.size >= 3) "${dateParts[2]}/${dateParts[1]}" else record.dateString
            Pair(label, record.totalCalories)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        var startAnimation by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { startAnimation = true }

        val animatedProgress by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            label = "weeklyAnimation"
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Báo cáo tuần (7 ngày gần nhất)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxCal = (chartData.maxOfOrNull { it.second } ?: targetCals).coerceAtLeast(targetCals)
                val safeMaxCal = if (maxCal == 0) 1 else maxCal
                
                chartData.forEach { (dateLabel, cals) ->
                    val ratio = (cals.toFloat() / safeMaxCal).coerceIn(0f, 1f)
                    val animatedRatio = ratio * animatedProgress
                    val barColor = if (cals > targetCals) Color.Red else MaterialTheme.colorScheme.primary
                    
                    val animatedCals = (cals * animatedProgress).roundToInt()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, 
                        verticalArrangement = Arrangement.Bottom, 
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(if (animatedCals > 0) "$animatedCals" else "", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(animatedRatio.coerceAtLeast(0.02f))
                                    .background(if (cals > targetCals) Brush.linearGradient(listOf(Color.White, MaroonRed)) else Brush.linearGradient(GradientNavyGreen), RoundedCornerShape(4.dp))
                            )
                        }
                        Text(dateLabel, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun BMITrackerCard(
    weightRecords: List<WeightRecordEntity>,
    userProfile: com.example.nutritionapp.data.local.entity.UserEntity?,
    onLogWeight: (Double) -> Unit
) {
    var weightInput by remember { mutableStateOf("") }
    val currentWeight = weightRecords.firstOrNull()?.weightKg ?: userProfile?.weightKg ?: 0.0
    val heightCm = userProfile?.heightCm ?: 170.0
    val goal = userProfile?.goal ?: "MAINTAIN"
    
    val heightM = heightCm / 100.0
    val bmi = if (heightM > 0 && currentWeight > 0) currentWeight / heightM.pow(2) else 0.0
    val formattedBmi = (bmi * 10.0).roundToInt() / 10.0
    
    val bmiStatus = when {
        bmi == 0.0 -> "Chưa xác định"
        bmi < 18.5 -> "Thiếu cân"
        bmi < 24.9 -> "Bình thường"
        bmi < 29.9 -> "Thừa cân"
        else -> "Béo phì"
    }

    val targetWeight = when (goal) {
       "LOSE_WEIGHT", "LOSE_FAT" -> 21.0 * heightM.pow(2)
       "BUILD_MUSCLE", "GAIN_WEIGHT" -> 23.0 * heightM.pow(2)
       else -> currentWeight
    }
    val diff = currentWeight - targetWeight
    
    val targetMessage = when {
       goal == "MAINTAIN" -> "Tuyệt vời! Bạn đang ở mức cân nặng mục tiêu."
       diff > 0 && (goal == "LOSE_WEIGHT" || goal == "LOSE_FAT") -> "💡 Cần giảm ${String.format("%.1f", diff)} kg nữa để chạm mốc BMI lý tưởng Châu Á (21.0)"
       diff < 0 && (goal == "BUILD_MUSCLE" || goal == "GAIN_WEIGHT") -> "💡 Cần tăng ${String.format("%.1f", -diff)} kg nữa để chạm mốc BMI cơ bắp chuẩn (23.0)"
       else -> "Cân nặng hiện tại: ${String.format("%.1f", currentWeight)} kg. Mục tiêu: ${String.format("%.1f", targetWeight)} kg."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Theo dõi cân nặng & BMI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Cân nặng HT: ${if (currentWeight>0) "$currentWeight kg" else "--"}", fontWeight = FontWeight.SemiBold)
            Text("BMI: $formattedBmi ($bmiStatus)", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            if (weightRecords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                WeightTrendChart(records = weightRecords.take(7).reversed())
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Chưa có dữ liệu ghi nhận cân nặng.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Nhập cân nặng (kg)", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).height(60.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        val w = weightInput.toDoubleOrNull()
                        if (w != null && w > 0) {
                            onLogWeight(w)
                            weightInput = ""
                        }
                    },
                    modifier = Modifier.height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cập nhật")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
            Text(
                 text = targetMessage, 
                 fontSize = 13.sp, 
                 style = LocalTextStyle.current.copy(brush = Brush.linearGradient(GradientNavyGreen)), 
                 fontWeight = FontWeight.SemiBold,
                 modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun WeightTrendChart(records: List<WeightRecordEntity>) {
    val maxWeight = (records.maxOfOrNull { it.weightKg } ?: 100.0) + 2.0
    val minWeight = (records.minOfOrNull { it.weightKg } ?: 0.0) - 2.0
    val weightRange = (maxWeight - minWeight).coerceAtLeast(2.0)
    val weightData = records.map { it.weightKg }

    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { startAnimation = true }

    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "weightTrendAnimation"
    )
    
    val pathMeasure = remember { androidx.compose.ui.graphics.PathMeasure() }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
           modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
           horizontalArrangement = Arrangement.Center
        ) {
           Row(verticalAlignment = Alignment.CenterVertically) {
               Box(Modifier.size(10.dp).background(VibrantGreen, CircleShape))
               Spacer(Modifier.width(6.dp))
               Text("Cân nặng (kg)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
           }
        }
        
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(140.dp).padding(horizontal = 12.dp, vertical = 20.dp)) {
            val width = size.width
            val height = size.height
            val stepX = if (records.size > 1) width / (records.size - 1) else width
            
            val weightPath = androidx.compose.ui.graphics.Path()
            weightData.forEachIndexed { index, w ->
                val x = if (records.size > 1) index * stepX else width / 2
                val y = height - ((w - minWeight) / weightRange).toFloat() * height
                if (index == 0) weightPath.moveTo(x, y) else weightPath.lineTo(x, y)
                
                // Only draw circle if it falls within the animated portion (approximately based on X)
                if (x <= width * animatedProgress || records.size == 1) {
                    drawCircle(color = VibrantGreen, radius = 5.dp.toPx(), center = androidx.compose.ui.geometry.Offset(x, y))
                }
            }
            
            pathMeasure.setPath(weightPath, false)
            val animatedPath = androidx.compose.ui.graphics.Path()
            pathMeasure.getSegment(0f, pathMeasure.length * animatedProgress, animatedPath, true)
            
            drawPath(
                path = animatedPath, 
                color = VibrantGreen.copy(alpha = 0.6f), 
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
             records.forEach { 
                 val dateParts = it.dateString.split("-")
                 val label = if(dateParts.size >= 3) "${dateParts[2]}/${dateParts[1]}" else it.dateString
                 Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
             }
        }
    }
}
