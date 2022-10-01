package com.preonboarding.sensordashboard.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase,
    private val errorUseCase: ErrorUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    val accSensorFlow = accSensorUseCase.getAccFlow()
    val gyroSensorFlow = gyroSensorUseCase.getGyroFlow()
    val sensorsDataPagingFlow = roomUseCase.getSensorPagingDataFlow()

    private val errorFlow = errorUseCase.getErrorFlow()

    private val accSensorDataList = mutableListOf<SensorAxisData>()

    init {
        viewModelScope.launch {
            errorFlow.collect {
                Timber.e(it.stackTraceToString())
            }
        }
    }

    fun deleteSensorData(id: Long) {
        viewModelScope.launch {
            roomUseCase.deleteSensorData(id)
        }
    }
}
