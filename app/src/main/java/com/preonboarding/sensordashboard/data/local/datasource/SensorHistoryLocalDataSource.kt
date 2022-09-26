package com.preonboarding.sensordashboard.data.local.datasource

import com.preonboarding.sensordashboard.domain.model.SensorHistory
import kotlinx.coroutines.flow.Flow

interface SensorHistoryLocalDataSource {
    fun getSensorHistoryList(): Flow<List<SensorHistory>>
    suspend fun saveSensorHistory(sensorHistory: SensorHistory)
    suspend fun deleteSensorHistory(sensorHistory: SensorHistory)
}