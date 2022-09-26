package com.preonboarding.sensordashboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorHistoryDao {

    @Query("SELECT * FROM sensor_history_table ORDER BY publishedAt DESC")
    fun getSensorHistoryList(): Flow<List<SensorHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSensorHistory(sensorHistory: SensorHistory)

    @Delete
    suspend fun deleteSensorHistory(sensorHistory: SensorHistory)

}