package com.preonboarding.sensordashboard.data.repositoryimpl

import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SensorHistoryRepositoryImpl @Inject constructor(
    private val sensorHistoryLocalDataSource: SensorHistoryLocalDataSource,
) : SensorHistoryRepository {
    override fun getSensorHistoryList(): Flow<List<SensorHistory>> = sensorHistoryLocalDataSource.getSensorHistoryList()

    override suspend fun saveSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryLocalDataSource.saveSensorHistory(sensorHistory)
    }

    override suspend fun deleteSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryLocalDataSource.deleteSensorHistory(sensorHistory)
    }
}