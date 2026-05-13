package com.example.nutritionapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nutritionapp.data.local.dao.DailyRecordDao
import com.example.nutritionapp.data.local.dao.MealDao
import com.example.nutritionapp.data.local.dao.UserDao
import com.example.nutritionapp.data.local.dao.WeightDao
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import com.example.nutritionapp.data.local.entity.MealEntity
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.data.local.entity.WeightRecordEntity

@Database(
    entities = [UserEntity::class, DailyRecordEntity::class, MealEntity::class, WeightRecordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NutritionDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val dailyRecordDao: DailyRecordDao
    abstract val mealDao: MealDao
    abstract val weightDao: WeightDao
}
