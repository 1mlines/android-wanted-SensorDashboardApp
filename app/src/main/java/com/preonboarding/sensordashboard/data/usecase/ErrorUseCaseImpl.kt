package com.preonboarding.sensordashboard.data.usecase

import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ErrorUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : ErrorUseCase {

    override fun getErrorFlow(): MutableSharedFlow<Throwable> {
        return sensorRepository.errorFlow()
    }
}