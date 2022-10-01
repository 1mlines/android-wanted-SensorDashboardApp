package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import kotlinx.coroutines.flow.Flow

/***
 * @Created by 서강휘 2022.09.27
 */

interface GyroSensorUseCase {

    fun getGyroFlow(): Flow<SensorAxisData>
}