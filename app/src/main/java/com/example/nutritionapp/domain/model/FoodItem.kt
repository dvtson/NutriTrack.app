package com.example.nutritionapp.domain.model

data class FoodItem(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double
)
