package com.example.nutritionapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritionapp.data.local.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight_table ORDER BY dateString DESC")
    fun getAllWeightRecords(): Flow<List<WeightRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecordEntity)

    @Query("DELETE FROM weight_table")
    suspend fun deleteAllWeightRecords()
}
