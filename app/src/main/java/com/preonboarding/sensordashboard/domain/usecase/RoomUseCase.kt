package com.preonboarding.sensordashboard.domain.usecase

import androidx.paging.PagingData
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow

/***
 * @Created by 서강휘 2022.09.28
 */

interface RoomUseCase {

    suspend fun insertSensorData(sensorData: SensorData)

    fun getSensorPagingDataFlow() : Flow<PagingData<SensorData>>

    suspend fun deleteSensorData(id: Long)
}