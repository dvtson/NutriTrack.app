package com.example.nutritionapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritionapp.data.local.entity.MealEntity
import com.example.nutritionapp.data.repository.NutritionRepository
import com.example.nutritionapp.domain.model.FoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.example.nutritionapp.data.local.entity.DailyRecordEntity

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        searchFood()
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
        searchFood()
    }

    fun searchFood() {
        val query = _searchQuery.value

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val result = repository.searchFood(query)
            if (result.isSuccess) {
                _searchResults.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = "Error: ${result.exceptionOrNull()?.message ?: "Unknown error"}"
            }
            
            _isLoading.value = false
        }
    }

    fun addFoodToMeal(food: FoodItem, mealType: String, dateString: String, weightGrams: Int) {
        viewModelScope.launch {
            val ratio = weightGrams / 100.0
            val mealEntity = MealEntity(
                dateString = dateString,
                mealType = mealType,
                foodName = "${food.name} (${weightGrams}g)",
                calories = (food.caloriesPer100g * ratio).toInt(),
                protein = (food.proteinPer100g * ratio).toInt(),
                carbs = (food.carbsPer100g * ratio).toInt(),
                fat = (food.fatPer100g * ratio).toInt()
            )
            repository.addMeal(mealEntity)
            
            val currentRecord = repository.getDailyRecord(dateString).firstOrNull() ?: DailyRecordEntity(dateString = dateString)
            repository.saveDailyRecord(
                currentRecord.copy(
                    totalCalories = currentRecord.totalCalories + mealEntity.calories,
                    totalProtein = currentRecord.totalProtein + mealEntity.protein,
                    totalCarbs = currentRecord.totalCarbs + mealEntity.carbs,
                    totalFat = currentRecord.totalFat + mealEntity.fat
                )
            )
        }
    }
}
