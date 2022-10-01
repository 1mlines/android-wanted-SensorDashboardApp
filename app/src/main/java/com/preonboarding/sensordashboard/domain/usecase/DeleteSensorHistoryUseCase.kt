package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import javax.inject.Inject

class DeleteSensorHistoryUseCase @Inject constructor(
    private val sensorHistoryRepository: SensorHistoryRepository,
) {
    suspend operator fun invoke(sensorHistory: SensorHistory) {
        sensorHistoryRepository.deleteSensorHistory(sensorHistory)
    }
}