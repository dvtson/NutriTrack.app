package com.example.nutritionapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_table")
data class WeightRecordEntity(
    @PrimaryKey val dateString: String,
    val weightKg: Double
)
