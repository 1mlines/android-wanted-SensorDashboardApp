package com.preonboarding.sensordashboard.domain.repository

import androidx.paging.PagingData
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/***
 * @Created by 서강휘 2022.09.27
 */

interface SensorRepository {

    fun getAccFlow(): Flow<SensorAxisData>

    fun getGyroFlow(): Flow<SensorAxisData>

    fun errorFlow(): MutableSharedFlow<Throwable>

    suspend fun insertSensorData(sensorData: SensorData)

    fun getSensorDataPagerFlow(): Flow<PagingData<SensorData>>

    suspend fun deleteSensorData(id: Long)
}