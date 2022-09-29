package com.preonboarding.sensordashboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.sensordashboard.data.local.entity.SensorHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorHistoryDao {

    @Query("SELECT * FROM sensor_history_table ORDER BY publishedAt DESC")
    fun getSensorHistoryList(): Flow<List<SensorHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSensorHistory(sensorHistory: SensorHistoryEntity)

    @Delete
    suspend fun deleteSensorHistory(sensorHistory: SensorHistoryEntity)

    @Query("SELECT * from sensor_history_table ORDER BY publishedAt DESC")
    suspend fun getSensorDataList(): List<SensorHistoryEntity>
}