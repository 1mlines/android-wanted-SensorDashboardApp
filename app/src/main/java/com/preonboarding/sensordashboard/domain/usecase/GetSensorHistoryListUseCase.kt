package com.preonboarding.sensordashboard.domain.usecase

import androidx.paging.PagingData
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSensorHistoryListUseCase @Inject constructor(
    private val sensorHistoryRepository: SensorHistoryRepository,
) {
    operator fun invoke(): Flow<PagingData<SensorHistory>> = sensorHistoryRepository.getSensorDataList()
}