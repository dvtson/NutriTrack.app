package com.example.nutritionapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NutritionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("NutritionApp", "Application started gracefully!")
    }
}
