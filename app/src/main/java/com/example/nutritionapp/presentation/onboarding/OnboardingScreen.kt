package com.example.nutritionapp.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    existingUser: UserEntity? = null,
    onSaveProfile: (UserEntity) -> Unit,
    onCancel: (() -> Unit)? = null
) {
    var name by remember(existingUser) { mutableStateOf(existingUser?.name ?: "") }
    var age by remember(existingUser) { mutableStateOf(existingUser?.age?.toString() ?: "") }
    var weight by remember(existingUser) { mutableStateOf(existingUser?.weightKg?.toString() ?: "") }
    var height by remember(existingUser) { mutableStateOf(existingUser?.heightCm?.toString() ?: "") }
    var isMale by remember(existingUser) { mutableStateOf(existingUser?.isMale ?: true) }
    
    // Default Activity: Sedentary
    var activityMultiplier by remember(existingUser) { mutableStateOf(existingUser?.activityMultiplier ?: 1.2) }
    var activityExpanded by remember { mutableStateOf(false) }
    val activityOptions = mapOf(
        1.2 to "Ít vận động (Văn phòng)",
        1.375 to "Vận động nhẹ (1-3 ngày/tuần)",
        1.55 to "Vận động vừa (3-5 ngày/tuần)",
        1.725 to "Vận động nhiều (6-7 ngày/tuần)",
        1.9 to "Vận động nặng (VĐV, lao động nặng)"
    )

    // Goal
    var goal by remember(existingUser) { mutableStateOf(existingUser?.goal ?: "MAINTAIN") }

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row (Cuộn đồng bộ)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onCancel != null) {
                    IconButton(onClick = onCancel, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = if (existingUser == null) "Hồ sơ Dinh dưỡng" else "Sửa Hồ sơ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Hãy nhập các chỉ số cơ thể của bạn để ứng dụng tính toán chuẩn xác lượng Calo & Macro cần thiết nhé!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên gọi của bạn") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, cursorColor = NavyBlue)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it.filter { char -> char.isDigit() } },
                    label = { Text("Tuổi") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, cursorColor = NavyBlue)
                )
                
                // Gender Select
                Column(modifier = Modifier.weight(1f)) {
                    Text("Giới tính", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                    Row {
                        FilterChip(
                            selected = isMale,
                            onClick = { isMale = true },
                            label = { Text("Nam", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = if(isMale) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                            modifier = Modifier.weight(1f).padding(end = 4.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IceBlue, selectedLabelColor = NavyBlue)
                        )
                        FilterChip(
                            selected = !isMale,
                            onClick = { isMale = false },
                            label = { Text("Nữ", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = if(!isMale) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                            modifier = Modifier.weight(1f).padding(start = 4.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IceBlue, selectedLabelColor = NavyBlue)
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Chiều cao (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, cursorColor = NavyBlue)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Cân nặng (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, cursorColor = NavyBlue)
                )
            }

            // Activity Level
            Column(modifier = Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = activityExpanded,
                    onExpandedChange = { activityExpanded = !activityExpanded }
                ) {
                    OutlinedTextField(
                        value = activityOptions[activityMultiplier] ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Mức độ vận động") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, cursorColor = NavyBlue)
                    )
                    ExposedDropdownMenu(
                        expanded = activityExpanded,
                        onDismissRequest = { activityExpanded = false }
                    ) {
                        activityOptions.forEach { (multiplier, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    activityMultiplier = multiplier
                                    activityExpanded = false
                                }
                            )
                        }
                    }
                }
                
                val activityNote = when(activityMultiplier) {
                    1.2 -> "Phù hợp: Làm việc văn phòng, ít đi lại, không tập luyện thể dục."
                    1.375 -> "Phù hợp: Vận động nhẹ, tập đi bộ hoặc yoga 1-3 ngày/tuần."
                    1.55 -> "Phù hợp: Tập luyện cường độ vừa (chạy bộ, đạp xe) 3-5 ngày/tuần."
                    1.725 -> "Phù hợp: Tập Gym, chơi thể thao cường độ cao 6-7 ngày/tuần."
                    else -> "Phù hợp: Vận động viên chuyên nghiệp, lao động tay chân nặng."
                }
                Text(
                    text = activityNote,
                    fontSize = 12.sp,
                    color = NavyBlue,
                    modifier = Modifier.padding(start = 16.dp, top = 6.dp)
                )
            }

            // Goal
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Mục tiêu dinh dưỡng", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalChip(
                        title = "Giảm cân", 
                        selected = goal == "LOSE_WEIGHT", 
                        onClick = { goal = "LOSE_WEIGHT" },
                        modifier = Modifier.weight(1f)
                    )
                    GoalChip(
                        title = "Giữ dáng", 
                        selected = goal == "MAINTAIN", 
                        onClick = { goal = "MAINTAIN" },
                        modifier = Modifier.weight(1f)
                    )
                    GoalChip(
                        title = "Tăng cơ", 
                        selected = goal == "BUILD_MUSCLE", 
                        onClick = { goal = "BUILD_MUSCLE" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val user = UserEntity(
                        name = name.ifBlank { "Người dùng" },
                        age = age.toIntOrNull() ?: 25,
                        isMale = isMale,
                        heightCm = height.toDoubleOrNull() ?: 170.0,
                        weightKg = weight.toDoubleOrNull() ?: 65.0,
                        activityMultiplier = activityMultiplier,
                        goal = goal
                    )
                    onSaveProfile(user)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).background(
                    if (name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()) androidx.compose.ui.graphics.Brush.linearGradient(GradientNavyGreen) else androidx.compose.ui.graphics.SolidColor(Color.LightGray), 
                    RoundedCornerShape(24.dp)
                ),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                enabled = name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
            ) {
                Text(if (existingUser == null) "Bắt đầu tính toán" else "Lưu thay đổi", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GoalChip(title: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal) },
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IceBlue, selectedLabelColor = NavyBlue),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    )
}
