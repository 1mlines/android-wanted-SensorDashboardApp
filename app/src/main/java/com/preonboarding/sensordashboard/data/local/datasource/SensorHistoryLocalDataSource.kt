package com.preonboarding.sensordashboard.data.local.datasource

import com.preonboarding.sensordashboard.data.local.entity.SensorHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SensorHistoryLocalDataSource {
    fun getSensorHistoryList(): Flow<List<SensorHistoryEntity>>
    suspend fun saveSensorHistory(sensorHistory: SensorHistoryEntity)
    suspend fun deleteSensorHistory(sensorHistory: SensorHistoryEntity)
}