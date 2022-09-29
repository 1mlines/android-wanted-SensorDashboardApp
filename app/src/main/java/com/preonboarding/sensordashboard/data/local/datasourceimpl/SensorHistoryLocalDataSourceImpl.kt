package com.preonboarding.sensordashboard.data.local.datasourceimpl

import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.local.entity.SensorHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SensorHistoryLocalDataSourceImpl @Inject constructor(
    private val sensorHistoryDao: SensorHistoryDao,
) : SensorHistoryLocalDataSource {

    override fun getSensorHistoryList(): Flow<List<SensorHistoryEntity>> = sensorHistoryDao.getSensorHistoryList()

    override suspend fun saveSensorHistory(sensorHistory: SensorHistoryEntity) {
        sensorHistoryDao.saveSensorHistory(sensorHistory)
    }

    override suspend fun deleteSensorHistory(sensorHistory: SensorHistoryEntity) {
        sensorHistoryDao.deleteSensorHistory(sensorHistory)
    }

    override fun getSensorDataList(): List<SensorHistoryEntity> =
        sensorHistoryDao.getSensorDataList(page = 1, loadSize = 5)
}