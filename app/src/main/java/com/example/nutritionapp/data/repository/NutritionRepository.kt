package com.example.nutritionapp.data.repository

import com.example.nutritionapp.data.local.dao.DailyRecordDao
import com.example.nutritionapp.data.local.dao.MealDao
import com.example.nutritionapp.data.local.dao.UserDao
import com.example.nutritionapp.data.local.dao.WeightDao
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import com.example.nutritionapp.data.local.entity.MealEntity
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.data.local.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow
import com.example.nutritionapp.data.local.provider.LocalFoodDataProvider
import com.example.nutritionapp.domain.model.FoodItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutritionRepository @Inject constructor(
    private val userDao: UserDao,
    private val dailyRecordDao: DailyRecordDao,
    private val mealDao: MealDao,
    private val weightDao: WeightDao,
    private val localFoodDataProvider: LocalFoodDataProvider
) {
    // User
    fun getUserProfile(): Flow<UserEntity?> = userDao.getUserProfile()
    suspend fun saveUserProfile(user: UserEntity) = userDao.insertUser(user)
    
    // Daily Record
    fun getDailyRecord(date: String): Flow<DailyRecordEntity?> = dailyRecordDao.getDailyRecord(date)
    suspend fun saveDailyRecord(record: DailyRecordEntity) = dailyRecordDao.insertOrUpdateDailyRecord(record)
    suspend fun addWater(date: String, amountMl: Int) = dailyRecordDao.addWater(date, amountMl)
    fun getAllDailyRecords(): Flow<List<DailyRecordEntity>> = dailyRecordDao.getAllDailyRecords()
    suspend fun deleteHistoryForDate(date: String) {
        dailyRecordDao.deleteDailyRecordByDate(date)
        mealDao.deleteMealsForDate(date)
    }

    // Meals
    fun getMealsForDate(date: String): Flow<List<MealEntity>> = mealDao.getMealsForDate(date)
    suspend fun addMeal(meal: MealEntity) = mealDao.insertMeal(meal)
    suspend fun deleteMeal(meal: MealEntity) = mealDao.deleteMeal(meal)

    // Weight
    fun getWeightRecords(): Flow<List<WeightRecordEntity>> = weightDao.getAllWeightRecords()
    suspend fun logWeight(record: WeightRecordEntity) = weightDao.insertWeightRecord(record)

    // Food API
    suspend fun searchFood(query: String): Result<List<FoodItem>> {
        return try {
            val foodItems = localFoodDataProvider.searchFood(query)
            Result.success(foodItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
