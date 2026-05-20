package com.example.nutritionapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritionapp.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_table WHERE dateString = :date ORDER BY mealType ASC")
    fun getMealsForDate(date: String): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meal_table WHERE dateString = :date")
    suspend fun deleteMealsForDate(date: String)

    @Query("DELETE FROM meal_table")
    suspend fun deleteAllMeals()
}
