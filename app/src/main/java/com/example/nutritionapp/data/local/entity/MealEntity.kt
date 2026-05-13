package com.example.nutritionapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_table")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String, // Format "YYYY-MM-DD"
    val mealType: String, // "BREAKFAST", "LUNCH", "DINNER", "SNACK"
    val foodName: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)
