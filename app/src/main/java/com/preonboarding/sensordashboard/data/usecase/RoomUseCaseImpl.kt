package com.preonboarding.sensordashboard.data.usecase

import androidx.paging.PagingData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/***
 * @Created by 서강휘 2022.09.28
 */

class RoomUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : RoomUseCase {

    override suspend fun insertSensorData(sensorData: SensorData) {
        sensorRepository.insertSensorData(sensorData)
    }

    override fun getSensorPagingDataFlow(): Flow<PagingData<SensorData>> {
        return sensorRepository.getSensorDataPagerFlow()
    }

    override suspend fun deleteSensorData(id: Long) {
        sensorRepository.deleteSensorData(id)
    }
}