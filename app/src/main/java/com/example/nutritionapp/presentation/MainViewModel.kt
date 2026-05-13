package com.example.nutritionapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import com.example.nutritionapp.data.local.entity.MealEntity
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.data.repository.NutritionRepository
import com.example.nutritionapp.util.NutritionCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    private val todayString: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val userProfile: StateFlow<UserEntity?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val todayRecord: StateFlow<DailyRecordEntity?> = repository.getDailyRecord(todayString)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val todayMeals: StateFlow<List<MealEntity>> = repository.getMealsForDate(todayString)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     * Lưu thông tin người dùng và tự động tính lại TDEE/Macro
     */
    fun saveProfile(user: UserEntity) {
        viewModelScope.launch {
            val bmi = NutritionCalculator.calculateBMI(user.weightKg, user.heightCm)
            val bmr = NutritionCalculator.calculateBMR(user.weightKg, user.heightCm, user.age, user.isMale)
            val tdee = NutritionCalculator.calculateTDEE(bmr, user.activityMultiplier)
            
            // Adjust target calories based on goal
            val targetCals = when (user.goal) {
                "LOSE_WEIGHT", "LOSE_FAT" -> tdee - 500.0
                "BUILD_MUSCLE", "GAIN_WEIGHT" -> tdee + 300.0
                else -> tdee
            }
            
            val macros = NutritionCalculator.calculateMacros(targetCals)
            
            val updatedUser = user.copy(
                targetCalories = targetCals.toInt(),
                targetProtein = macros["Protein"] ?: 0,
                targetFat = macros["Fat"] ?: 0,
                targetCarbs = macros["Carbs"] ?: 0
            )
            repository.saveUserProfile(updatedUser)
            
            // Tự động ghi lại lịch sử Tracker mỗi khi cập nhật cân nặng trong Hồ sơ
            repository.logWeight(com.example.nutritionapp.data.local.entity.WeightRecordEntity(
                dateString = todayString,
                weightKg = user.weightKg
            ))
        }
    }

    /**
     * Thêm nước uống (đơn vị: ml)
     */
    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            val currentRecord = todayRecord.value
            if (currentRecord == null) {
                if (amountMl > 0) {
                    repository.saveDailyRecord(DailyRecordEntity(dateString = todayString, waterIntakeMl = amountMl))
                }
            } else {
                val newAmount = Math.max(0, currentRecord.waterIntakeMl + amountMl)
                repository.saveDailyRecord(currentRecord.copy(waterIntakeMl = newAmount))
            }
        }
    }

    /**
     * Thêm món ăn và tự động cộng dồn vào Calo/Macro trong ngày
     */
    fun addMeal(mealName: String, cals: Int, protein: Int, carbs: Int, fat: Int, type: String = "LUNCH") {
        viewModelScope.launch {
            val meal = MealEntity(
                dateString = todayString,
                mealType = type,
                foodName = mealName,
                calories = cals,
                protein = protein,
                carbs = carbs,
                fat = fat
            )
            repository.addMeal(meal)
            
            // Update daily record totals
            val currentRecord = todayRecord.value ?: DailyRecordEntity(dateString = todayString)
            repository.saveDailyRecord(
                currentRecord.copy(
                    totalCalories = currentRecord.totalCalories + cals,
                    totalProtein = currentRecord.totalProtein + protein,
                    totalCarbs = currentRecord.totalCarbs + carbs,
                    totalFat = currentRecord.totalFat + fat
                )
            )
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            val currentRecord = todayRecord.value ?: return@launch
            repository.saveDailyRecord(
                currentRecord.copy(
                    totalCalories = Math.max(0, currentRecord.totalCalories - meal.calories),
                    totalProtein = Math.max(0, currentRecord.totalProtein - meal.protein),
                    totalCarbs = Math.max(0, currentRecord.totalCarbs - meal.carbs),
                    totalFat = Math.max(0, currentRecord.totalFat - meal.fat)
                )
            )
        }
    }
}
