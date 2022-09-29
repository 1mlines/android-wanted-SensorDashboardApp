package com.preonboarding.sensordashboard.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.local.entity.toEntity
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import com.preonboarding.sensordashboard.presentation.view.sensor_history_list.HistoryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SensorHistoryRepositoryImpl @Inject constructor(
    private val sensorHistoryLocalDataSource: SensorHistoryLocalDataSource,
    private val dao: SensorHistoryDao
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

    override fun getSensorDataList(): Flow<PagingData<SensorHistory>> {
        return (Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { HistoryPagingSource(dao) }
        ).flow).map { sensorHistoryEntityList->
            sensorHistoryEntityList.map {
                it.toModel()
            }
        }
    }
}