package com.example.nutritionapp.di

import android.app.Application
import androidx.room.Room
import com.example.nutritionapp.data.local.NutritionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNutritionDatabase(app: Application): NutritionDatabase {
        return Room.databaseBuilder(
            app,
            NutritionDatabase::class.java,
            "nutrition_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: NutritionDatabase) = db.userDao

    @Provides
    @Singleton
    fun provideDailyRecordDao(db: NutritionDatabase) = db.dailyRecordDao

    @Provides
    @Singleton
    fun provideMealDao(db: NutritionDatabase) = db.mealDao

    @Provides
    @Singleton
    fun provideWeightDao(db: NutritionDatabase) = db.weightDao
}
