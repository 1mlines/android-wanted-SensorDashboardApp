package com.preonboarding.sensordashboard.domain.usecase

import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSensorHistoryListUseCase @Inject constructor(
    private val sensorHistoryRepository: SensorHistoryRepository,
) {
    suspend operator fun invoke() =
        sensorHistoryRepository.getSensorDataList().flowOn(Dispatchers.Default)
}