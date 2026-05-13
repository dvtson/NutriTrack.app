package com.example.nutritionapp.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.nutritionapp.ui.theme.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val records by viewModel.dailyRecords.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val targetCals = userProfile?.targetCalories?.takeIf { it > 0 } ?: 2000

    val todayStr = remember { java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE) }
    var selectedDate by remember { mutableStateOf(todayStr) }
    
    val dateList = remember(records) {
        val uniqueDates = records.map { it.dateString }.toMutableSet()
        uniqueDates.add(todayStr) // Luôn hiển thị ngày hôm nay dù chưa có log
        uniqueDates.toList().sortedDescending()
    }

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Lịch sử ăn uống",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
            )
            // Horizontal Date Pager
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dateList) { dateStr ->
                    val isSelected = dateStr == selectedDate
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedDate = dateStr },
                        label = { Text(if (dateStr == todayStr) "Hôm nay" else dateStr, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IceBlue,
                            selectedLabelColor = NavyBlue,
                            labelColor = TextLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            }

            // Selected Record
            val selectedRecord = records.find { it.dateString == selectedDate }
            if (selectedRecord == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có dữ liệu trong ngày này.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                HistoryItemCard(
                    record = selectedRecord, 
                    targetCals = targetCals,
                    onDelete = { viewModel.deleteRecord(selectedRecord.dateString) }
                )
            }
        }
    }
}

@Composable
fun HistoryItemCard(record: DailyRecordEntity, targetCals: Int, onDelete: () -> Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có muốn xóa toàn bộ lịch sử ăn uống của ngày ${record.dateString} không?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete()
                }) {
                    Text("Xóa", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    val progress = if (targetCals > 0) (record.totalCalories.toFloat() / targetCals.toFloat()).coerceIn(0f, 1f) else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ngày: ${record.dateString}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Calo: ${record.totalCalories} / $targetCals", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Đạm: ${record.totalProtein}g | Tinh bột: ${record.totalCarbs}g | Béo: ${record.totalFat}g", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Nước: ${record.waterIntakeMl} ml", fontSize = 12.sp, color = TealAccent)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(40.dp)) {
                         drawArc(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                         )
                         drawArc(
                            brush = if (record.totalCalories > targetCals) androidx.compose.ui.graphics.SolidColor(Color.Red) else androidx.compose.ui.graphics.Brush.linearGradient(GradientNavyGreen),
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                         )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa ngày này", tint = MaroonRed)
                    }
                }
            }
        }
    }
}
