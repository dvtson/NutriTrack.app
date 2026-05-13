package com.example.nutritionapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_record_table")
data class DailyRecordEntity(
    @PrimaryKey val dateString: String, // Format usually ISO 'YYYY-MM-DD'
    val totalCalories: Int = 0,
    val totalProtein: Int = 0,
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val waterIntakeMl: Int = 0
)
