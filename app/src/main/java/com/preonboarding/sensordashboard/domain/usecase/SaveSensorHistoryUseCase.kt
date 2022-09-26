package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import javax.inject.Inject

class SaveSensorHistoryUseCase @Inject constructor(
    private val sensorHistoryRepository: SensorHistoryRepository
){
    suspend operator fun invoke(sensorHistory: SensorHistory) {
        sensorHistoryRepository.saveSensorHistory(sensorHistory)
    }
}