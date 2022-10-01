package com.preonboarding.sensordashboard.data.usecase

import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/***
 * @Created by 서강휘 2022.09.27
 */

class GyroSensorUseCaseImpl @Inject constructor(
    private val repository: SensorRepository
) : GyroSensorUseCase {

    override fun getGyroFlow(): Flow<SensorAxisData> {
        return repository.getGyroFlow()
    }
}