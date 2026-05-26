package com.example.nutritionapp.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.nutritionapp.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutritionapp.domain.model.FoodItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: FoodSearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val currentDate = remember { LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) }

    // State cho việc Tự tạo món
    var customName by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFat by remember { mutableStateOf("") }
    var expandedMealDropdown by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf("BREAKFAST") }
    
    val mealMap = mapOf(
        "BREAKFAST" to "Bữa sáng",
        "LUNCH" to "Bữa trưa",
        "DINNER" to "Bữa tối",
        "SNACK" to "Ăn nhẹ"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm bữa ăn", fontWeight = FontWeight.Bold, fontSize = 28.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                // Tiêu đề Tự Tạo Món
                Text("Ghi nhận chi tiết", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(12.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = customName, 
                            onValueChange = { customName = it }, 
                            label = { Text("Tên món ăn") }, 
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = customProtein, onValueChange = { customProtein = it.filter { c -> c.isDigit() } },
                                label = { Text("Đạm (g)", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                                singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = customCarbs, onValueChange = { customCarbs = it.filter { c -> c.isDigit() } },
                                label = { Text("Tinh bột", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                                singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = customFat, onValueChange = { customFat = it.filter { c -> c.isDigit() } },
                                label = { Text("Béo (g)", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                                singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                            )
                        }
                        
                        ExposedDropdownMenuBox(
                            expanded = expandedMealDropdown,
                            onExpandedChange = { expandedMealDropdown = !expandedMealDropdown }
                        ) {
                            OutlinedTextField(
                                value = mealMap[selectedMeal] ?: selectedMeal,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lưu vào bữa") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMealDropdown) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMealDropdown,
                                onDismissRequest = { expandedMealDropdown = false }
                            ) {
                                mealMap.forEach { (key, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = { selectedMeal = key; expandedMealDropdown = false }
                                    )
                                }
                            }
                        }

                        val p = customProtein.toIntOrNull() ?: 0
                        val c = customCarbs.toIntOrNull() ?: 0
                        val f = customFat.toIntOrNull() ?: 0
                        val cal = p * 4 + c * 4 + f * 9

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Tổng ước tính: $cal kcal", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                            Button(
                                onClick = {
                                    val finalName = customName.ifBlank { "Món tự chọn" }
                                    val customFood = FoodItem(
                                        id = "custom_" + System.currentTimeMillis(), name = finalName,
                                        caloriesPer100g = cal.toDouble(),
                                        proteinPer100g = p.toDouble(),
                                        carbsPer100g = c.toDouble(),
                                        fatPer100g = f.toDouble(),
                                        imageUrl = null
                                    )
                                    viewModel.addFoodToMeal(customFood, selectedMeal, currentDate, 100)
                                    onNavigateBack() // Go back or reset state. Let's reset state instead since we are in Tab
                                    customName = ""
                                    customProtein = ""
                                    customCarbs = ""
                                    customFat = ""
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.background(Brush.linearGradient(GradientNavyGreen), CircleShape)
                            ) {
                                Text("Lưu món", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
                            }
                        }
                    }
                }
            }

            item {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
                // Bắt đầu phần Search Quick
                Text("Thêm nhanh (Ước lượng)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Vd: Phở, Táo...") },
                    singleLine = true,
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, cursorColor = MaterialTheme.colorScheme.primary),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.searchFood() }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                )
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
                items(searchResults) { food ->
                    FoodItemRow(
                        food = food,
                        onAddClick = { mealType, weight ->
                            viewModel.addFoodToMeal(food, mealType, currentDate, weight)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
            }
        }
    }
}

@Composable
fun FoodItemRow(
    food: FoodItem,
    onAddClick: (String, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${food.caloriesPer100g.toInt()} kcal | Đạm: ${food.proteinPer100g.toInt()}g | Tinh bột: ${food.carbsPer100g.toInt()}g | Béo: ${food.fatPer100g.toInt()}g (100g)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add to Meal", tint = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Bữa sáng") },
                            onClick = { selectedMeal = "BREAKFAST"; expanded = false; showWeightDialog = true }
                        )
                        DropdownMenuItem(
                            text = { Text("Bữa trưa") },
                            onClick = { selectedMeal = "LUNCH"; expanded = false; showWeightDialog = true }
                        )
                        DropdownMenuItem(
                            text = { Text("Bữa tối") },
                            onClick = { selectedMeal = "DINNER"; expanded = false; showWeightDialog = true }
                        )
                        DropdownMenuItem(
                            text = { Text("Ăn nhẹ") },
                            onClick = { selectedMeal = "SNACK"; expanded = false; showWeightDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showWeightDialog) {
        var weightInput by remember { mutableStateOf("100") }
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            title = { Text("Lượng thực phẩm (Gram)", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it.filter { char -> char.isDigit() } },
                    label = { Text("Nhập số Gram") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { 
                        val w = weightInput.toIntOrNull() ?: 100
                        onAddClick(selectedMeal, w)
                        showWeightDialog = false
                    },
                    modifier = Modifier.background(Brush.linearGradient(GradientNavyGreen), CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) { Text("Thêm món", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) { Text("Hủy", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
