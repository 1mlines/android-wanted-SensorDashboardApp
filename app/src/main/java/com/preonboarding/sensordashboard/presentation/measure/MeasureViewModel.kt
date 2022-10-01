package com.preonboarding.sensordashboard.presentation.measure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
import com.preonboarding.sensordashboard.util.DateUtil.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by 김현국 2022/09/29
 */

@HiltViewModel
class MeasureViewModel @Inject constructor(
    private val accSensorUseCase: AccSensorUseCase,
    private val gyroSensorUseCase: GyroSensorUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    private val _measuredSensorData = MutableStateFlow(
        SensorAxisData(
            0f,
            0f,
            0f
        )
    )

    private val _pressStop = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var currentSensorType: SensorType = SensorType.ACC

    val measuredSensorData = _measuredSensorData.asStateFlow()

    val sensorDataList = mutableListOf<SensorAxisData>()

    fun measureGyroSensor() {
        val job = viewModelScope.launch {
            gyroSensorUseCase.getGyroFlow().collect { sensorAxisData ->
                _measuredSensorData.value = sensorAxisData
            }
        }
        viewModelScope.launch {
            _pressStop.collect {
                job.cancelAndJoin()
            }
        }
    }

    fun measureAccSensor() {
        val job = viewModelScope.launch {
            accSensorUseCase.getAccFlow().collect { sensorAxisData ->
                _measuredSensorData.value = sensorAxisData
            }
        }
        viewModelScope.launch {
            _pressStop.collect {
                job.cancelAndJoin()
            }
        }
    }

    fun addSensorAxisData(sensorAxisData: SensorAxisData) {
        sensorDataList.add(sensorAxisData)
    }

    fun clearSensorDataList() {
        sensorDataList.removeAll { true }
    }

    fun pressStop() {
        viewModelScope.launch {
            _pressStop.tryEmit(Unit)
        }
    }

    fun updateCurrentSensorType(type: SensorType) {
        currentSensorType = type
    }

    fun saveSensorData() {
        viewModelScope.launch {
            val time = sensorDataList.size / 10f
            roomUseCase.insertSensorData(
                SensorData.EMPTY.copy(
                    dataList = sensorDataList,
                    type = when (currentSensorType) {
                        SensorType.ACC -> {
                            SensorType.ACC
                        }
                        else -> {
                            SensorType.GYRO
                        }
                    },
                    time = time,
                    date = getCurrentTime()
                )
            )
        }
    }
}
