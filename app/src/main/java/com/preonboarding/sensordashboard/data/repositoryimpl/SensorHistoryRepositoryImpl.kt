package com.preonboarding.sensordashboard.data.repositoryimpl

import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.local.entity.toEntity
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SensorHistoryRepositoryImpl @Inject constructor(
    private val sensorHistoryLocalDataSource: SensorHistoryLocalDataSource,
) : SensorHistoryRepository {
    override fun getSensorHistoryList(): Flow<List<SensorHistory>> {
        return sensorHistoryLocalDataSource.getSensorHistoryList().map { sensorHistoryEntityList ->
            sensorHistoryEntityList.map {
                it.toModel()
            }
        }
    }

    override suspend fun saveSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryLocalDataSource.saveSensorHistory(sensorHistory.toEntity())
    }

    override suspend fun deleteSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryLocalDataSource.deleteSensorHistory(sensorHistory.toEntity())
    }
}