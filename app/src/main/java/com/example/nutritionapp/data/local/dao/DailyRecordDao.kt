package com.example.nutritionapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyRecordDao {
    @Query("SELECT * FROM daily_record_table WHERE dateString = :date LIMIT 1")
    fun getDailyRecord(date: String): Flow<DailyRecordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyRecord(record: DailyRecordEntity)

    @Query("UPDATE daily_record_table SET waterIntakeMl = waterIntakeMl + :amountMl WHERE dateString = :date")
    suspend fun addWater(date: String, amountMl: Int)

    @Query("SELECT * FROM daily_record_table ORDER BY dateString DESC")
    fun getAllDailyRecords(): Flow<List<DailyRecordEntity>>

    @Query("DELETE FROM daily_record_table WHERE dateString = :date")
    suspend fun deleteDailyRecordByDate(date: String)

    @Query("DELETE FROM daily_record_table")
    suspend fun deleteAllDailyRecords()
}
