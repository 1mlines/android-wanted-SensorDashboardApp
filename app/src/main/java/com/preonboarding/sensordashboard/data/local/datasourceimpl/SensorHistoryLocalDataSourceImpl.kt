package com.preonboarding.sensordashboard.data.local.datasourceimpl

import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SensorHistoryLocalDataSourceImpl @Inject constructor(
    private val sensorHistoryDao: SensorHistoryDao,
) : SensorHistoryLocalDataSource {
    override fun getSensorHistoryList(): Flow<List<SensorHistory>> = sensorHistoryDao.getSensorHistoryList()

    override suspend fun saveSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryDao.saveSensorHistory(sensorHistory)
    }

    override suspend fun deleteSensorHistory(sensorHistory: SensorHistory) {
        sensorHistoryDao.deleteSensorHistory(sensorHistory)
    }
}