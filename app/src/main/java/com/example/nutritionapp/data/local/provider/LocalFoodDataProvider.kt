package com.example.nutritionapp.data.local.provider

import android.content.Context
import com.example.nutritionapp.domain.model.FoodItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.Normalizer
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class LocalFoodDataProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var cachedFoods: List<FoodItem>? = null

    private suspend fun getFoods(): List<FoodItem> = withContext(Dispatchers.IO) {
        cachedFoods?.let { return@withContext it }

        val foods = mutableListOf<FoodItem>()
        try {
            val inputStream = context.assets.open("food_data.csv")
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            
            // Skip header line
            reader.readLine()
            
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val currentLine = line ?: break
                val tokens = currentLine.split(";")
                if (tokens.size >= 5) {
                    val name = tokens[0].trim()
                    val calories = tokens[1].trim().toDoubleOrNull() ?: 0.0
                    val protein = tokens[2].trim().toDoubleOrNull() ?: 0.0
                    val carbs = tokens[3].trim().toDoubleOrNull() ?: 0.0
                    val fat = tokens[4].trim().toDoubleOrNull() ?: 0.0
                    
                    foods.add(
                        FoodItem(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            imageUrl = null,
                            caloriesPer100g = calories,
                            proteinPer100g = protein,
                            carbsPer100g = carbs,
                            fatPer100g = fat
                        )
                    )
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        cachedFoods = foods
        return@withContext foods
    }

    private fun removeAccents(str: String): String {
        val normalized = Normalizer.normalize(str, Normalizer.Form.NFD)
        return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(normalized, "")
            .replace("đ", "d").replace("Đ", "D")
    }

    suspend fun searchFood(query: String): List<FoodItem> {
        val allFoods = getFoods()
        if (query.isBlank()) return allFoods.take(50) // Return first 50 items if query is empty
        
        val normalizedQuery = removeAccents(query.lowercase())
        return allFoods.filter { removeAccents(it.name.lowercase()).contains(normalizedQuery) }
            .take(50)
    }
}
