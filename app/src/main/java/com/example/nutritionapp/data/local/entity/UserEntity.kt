package com.example.nutritionapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: Int = 1, // Defaulting to 1 since there's typically one profile on personal device
    val name: String,
    val age: Int,
    val isMale: Boolean,
    val heightCm: Double,
    val weightKg: Double,
    val activityMultiplier: Double,
    val goal: String = "MAINTAIN",
    val targetCalories: Int = 0,
    val targetProtein: Int = 0,
    val targetCarbs: Int = 0,
    val targetFat: Int = 0
)
