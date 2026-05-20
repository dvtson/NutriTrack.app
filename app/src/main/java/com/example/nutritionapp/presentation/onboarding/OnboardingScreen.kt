package com.example.nutritionapp.presentation.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.ui.theme.*
import com.example.nutritionapp.util.NutritionCalculator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    existingUser: UserEntity? = null,
    onSaveProfile: (UserEntity) -> Unit,
    onCancel: (() -> Unit)? = null,
    onDeleteData: (() -> Unit)? = null
) {
    var name by remember(existingUser) { mutableStateOf(existingUser?.name ?: "") }
    var age by remember(existingUser) { mutableStateOf(existingUser?.age?.toString() ?: "") }
    var weight by remember(existingUser) { mutableStateOf(existingUser?.weightKg?.toString() ?: "") }
    var height by remember(existingUser) { mutableStateOf(existingUser?.heightCm?.toString() ?: "") }
    var isMale by remember(existingUser) { mutableStateOf(existingUser?.isMale ?: true) }

    var activityMultiplier by remember(existingUser) { mutableStateOf(existingUser?.activityMultiplier ?: 1.2) }
    var goal by remember(existingUser) { mutableStateOf(existingUser?.goal ?: "MAINTAIN") }

    val isEditing = existingUser != null

    val saveAction = {
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
    }

    if (isEditing) {
        var showDeleteConfirm by remember { mutableStateOf(false) }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Cảnh báo Xoá Dữ liệu") },
                text = { Text("Bạn có chắc chắn muốn xoá mọi dữ liệu không? Tất cả hồ sơ, nhật ký, và bữa ăn của bạn sẽ bị xoá vĩnh viễn và không thể khôi phục.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirm = false
                            onDeleteData?.invoke()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Xoá tất cả", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("Huỷ")
                    }
                }
            )
        }

        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onCancel != null) {
                        IconButton(onClick = onCancel, modifier = Modifier.padding(end = 8.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                    Text(
                        text = "Sửa Hồ sơ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

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
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Giới tính", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                        Row {
                            FilterChip(
                                selected = isMale,
                                onClick = { isMale = true },
                                label = { Text("Nam", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = if(isMale) FontWeight.Bold else FontWeight.Normal) },
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IceBlue, selectedLabelColor = NavyBlue)
                            )
                            FilterChip(
                                selected = !isMale,
                                onClick = { isMale = false },
                                label = { Text("Nữ", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = if(!isMale) FontWeight.Bold else FontWeight.Normal) },
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

                var activityExpanded by remember { mutableStateOf(false) }
                val activityOptions = mapOf(
                    1.2 to "Ít vận động",
                    1.375 to "Vận động nhẹ",
                    1.55 to "Vận động vừa",
                    1.725 to "Vận động nhiều",
                    1.9 to "Vận động nặng"
                )

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

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Mục tiêu dinh dưỡng", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GoalChip(
                            title = "Giảm cân", 
                            selected = goal == "LOSE_WEIGHT", 
                            onClick = { goal = "LOSE_WEIGHT" },
                            modifier = Modifier.weight(1f),
                            useOldColors = true
                        )
                        GoalChip(
                            title = "Giữ dáng", 
                            selected = goal == "MAINTAIN", 
                            onClick = { goal = "MAINTAIN" },
                            modifier = Modifier.weight(1f),
                            useOldColors = true
                        )
                        GoalChip(
                            title = "Tăng cơ", 
                            selected = goal == "BUILD_MUSCLE", 
                            onClick = { goal = "BUILD_MUSCLE" },
                            modifier = Modifier.weight(1f),
                            useOldColors = true
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = saveAction,
                    modifier = Modifier.fillMaxWidth().height(56.dp).background(
                        if (name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()) Brush.linearGradient(GradientNavyGreen) else androidx.compose.ui.graphics.SolidColor(Color.LightGray), 
                        RoundedCornerShape(24.dp)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    enabled = name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
                ) {
                    Text("Lưu thay đổi", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                if (onDeleteData != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                    ) {
                        Text("Xoá mọi dữ liệu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        // --- NEW ONBOARDING PAGER STYLE ---
        val pagerState = rememberPagerState(pageCount = { 3 })
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            floatingActionButton = {
                val isPage0Valid = name.isNotBlank()
                val isPage1Valid = age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
                val isCurrentPageValid = when (pagerState.currentPage) {
                    0 -> isPage0Valid
                    1 -> isPage1Valid
                    else -> true
                }

                if (isCurrentPageValid) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                brush = Brush.linearGradient(GradientNavyGreen),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (pagerState.currentPage < 2) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                } else {
                                    saveAction()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (pagerState.currentPage < 2) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = Color.White, modifier = Modifier.size(32.dp))
                            } else {
                                Icon(Icons.Default.Check, contentDescription = "Done", tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Header Row with Back Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    } else if (onCancel != null) {
                        IconButton(onClick = onCancel, modifier = Modifier.padding(end = 8.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Simple animated page indicator
                    Text(
                        text = "${pagerState.currentPage + 1}/3",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(brush = Brush.linearGradient(GradientNavyGreen))
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = false // Disable swipe to force using the button for validation
                ) { page ->
                    when (page) {
                        0 -> WelcomePage(
                            name = name,
                            onNameChange = { name = it },
                            isVisible = pagerState.currentPage == 0
                        )
                        1 -> MetricsPage(
                            age = age, onAgeChange = { age = it },
                            height = height, onHeightChange = { height = it },
                            weight = weight, onWeightChange = { weight = it },
                            isMale = isMale, onGenderChange = { isMale = it },
                            isVisible = pagerState.currentPage == 1
                        )
                        2 -> GoalPage(
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            height = height.toDoubleOrNull() ?: 0.0,
                            activityMultiplier = activityMultiplier,
                            onActivityChange = { activityMultiplier = it },
                            goal = goal,
                            onGoalChange = { goal = it },
                            isVisible = pagerState.currentPage == 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomePage(name: String, onNameChange: (String) -> Unit, isVisible: Boolean) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) startAnimation = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        AnimatedVisibility(
            visible = startAnimation,
            enter = slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(600, easing = FastOutSlowInEasing)) + fadeIn(tween(600))
        ) {
            Text(
                text = "NutriTrack",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                style = LocalTextStyle.current.copy(brush = Brush.linearGradient(GradientNavyGreen)),
                modifier = Modifier.padding(bottom = 64.dp)
            )
        }

        AnimatedVisibility(
            visible = startAnimation,
            enter = slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(600, delayMillis = 200, easing = FastOutSlowInEasing)) + fadeIn(tween(600, delayMillis = 200))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Chào mừng đến với NutriTrack!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = LocalTextStyle.current.copy(brush = Brush.linearGradient(GradientNavyGreen))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tôi nên gọi bạn là gì?",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Tên gọi của bạn") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
fun MetricsPage(
    age: String, onAgeChange: (String) -> Unit,
    height: String, onHeightChange: (String) -> Unit,
    weight: String, onWeightChange: (String) -> Unit,
    isMale: Boolean, onGenderChange: (Boolean) -> Unit,
    isVisible: Boolean
) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) startAnimation = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = startAnimation,
            enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(500, delayMillis = 150)) + fadeIn(tween(500, delayMillis = 150))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Chỉ số cơ thể",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = LocalTextStyle.current.copy(brush = Brush.linearGradient(GradientNavyGreen))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hãy nhập các chỉ số của bạn để ứng dụng tính toán lượng Calo phù hợp.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { onAgeChange(it.filter { char -> char.isDigit() }) },
                        label = { Text("Tuổi") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary)
                    )

                    // Gender Select
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Giới tính", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp, start = 8.dp))
                        Row {
                            FilterChip(
                                selected = isMale,
                                onClick = { onGenderChange(true) },
                                label = { Text("Nam", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = if(isMale) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                shape = RoundedCornerShape(24.dp)
                            )
                            FilterChip(
                                selected = !isMale,
                                onClick = { onGenderChange(false) },
                                label = { Text("Nữ", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = if(!isMale) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                modifier = Modifier.weight(1f).padding(start = 4.dp),
                                shape = RoundedCornerShape(24.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = height,
                        onValueChange = { onHeightChange(it.filter { char -> char.isDigit() || char == '.' }) },
                        label = { Text("Chiều cao (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary)
                    )
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { onWeightChange(it.filter { char -> char.isDigit() || char == '.' }) },
                        label = { Text("Cân nặng (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

// Utility to wrap Android's OvershootInterpolator for Compose
class OvershootInterpolator(private val tension: Float = 2.0f) : android.view.animation.Interpolator {
    override fun getInterpolation(t: Float): Float {
        var t1 = t - 1.0f
        return t1 * t1 * ((tension + 1) * t1 + tension) + 1.0f
    }
}
fun android.view.animation.Interpolator.toEasing() = Easing { fraction -> getInterpolation(fraction) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalPage(
    weight: Double,
    height: Double,
    activityMultiplier: Double,
    onActivityChange: (Double) -> Unit,
    goal: String,
    onGoalChange: (String) -> Unit,
    isVisible: Boolean
) {
    val bmi = NutritionCalculator.calculateBMI(weight, height)
    val bmiResult = NutritionCalculator.getBMIClassification(bmi)
    val bmiColor = when (bmiResult.classification) {
        "Thiếu cân" -> Color(0xFF2196F3) // Blue
        "Bình thường" -> Color(0xFF4CAF50) // Green
        "Thừa cân" -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }

    // Animated BMI Number
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) startAnimation = true
    }
    val animatedBmi by animateFloatAsState(
        targetValue = if (startAnimation) bmi.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "bmiAnimation"
    )

    // Auto select recommendation if goal hasn't been modified yet
    LaunchedEffect(bmiResult.recommendedGoal) {
        if (bmiResult.recommendedGoal != "MAINTAIN" && goal == "MAINTAIN") {
            onGoalChange(bmiResult.recommendedGoal)
        }
    }

    var activityExpanded by remember { mutableStateOf(false) }
    val activityOptions = mapOf(
        1.2 to "Ít vận động (Văn phòng)",
        1.375 to "Vận động nhẹ (1-3 ngày/tuần)",
        1.55 to "Vận động vừa (3-5 ngày/tuần)",
        1.725 to "Vận động nhiều (6-7 ngày/tuần)",
        1.9 to "Vận động nặng (VĐV, lao động nặng)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .padding(bottom = 80.dp), // Space for FAB
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // BMI Result Section
        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn(tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800, easing = OvershootInterpolator(1f).toEasing()))
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = bmiColor.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Chỉ số BMI", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = String.format("%.1f", animatedBmi),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = bmiColor
                    )
                    Text(
                        text = bmiResult.classification,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = bmiColor,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bmiResult.recommendation,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Activity Level
        AnimatedVisibility(
            visible = startAnimation,
            enter = slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(500, delayMillis = 400)) + fadeIn(tween(500, delayMillis = 400))
        ) {
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
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary)
                    )
                    ExposedDropdownMenu(
                        expanded = activityExpanded,
                        onDismissRequest = { activityExpanded = false }
                    ) {
                        activityOptions.forEach { (multiplier, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onActivityChange(multiplier)
                                    activityExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Goal Selection
        AnimatedVisibility(
            visible = startAnimation,
            enter = slideInVertically(initialOffsetY = { 40 }, animationSpec = tween(500, delayMillis = 600)) + fadeIn(tween(500, delayMillis = 600))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Mục tiêu dinh dưỡng", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalChip(
                        title = "Giảm cân",
                        selected = goal == "LOSE_WEIGHT",
                        onClick = { onGoalChange("LOSE_WEIGHT") },
                        modifier = Modifier.weight(1f)
                    )
                    GoalChip(
                        title = "Giữ dáng",
                        selected = goal == "MAINTAIN",
                        onClick = { onGoalChange("MAINTAIN") },
                        modifier = Modifier.weight(1f)
                    )
                    GoalChip(
                        title = "Tăng cơ",
                        selected = goal == "BUILD_MUSCLE",
                        onClick = { onGoalChange("BUILD_MUSCLE") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun GoalChip(title: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, useOldColors: Boolean = false) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal) },
        shape = RoundedCornerShape(24.dp),
        colors = if (useOldColors) FilterChipDefaults.filterChipColors(selectedContainerColor = IceBlue, selectedLabelColor = NavyBlue) else FilterChipDefaults.filterChipColors(),
        modifier = modifier
    )
}
