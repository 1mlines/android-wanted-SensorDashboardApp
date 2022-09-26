package com.preonboarding.sensordashboard.presentation.sensor_history_measure

import androidx.lifecycle.ViewModel
import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.usecase.SaveSensorHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SensorHistoryMeasureViewModel @Inject constructor(
    private val saveSensorHistoryUseCase: SaveSensorHistoryUseCase
) : BaseViewModel() {

    fun saveSensorHistory(){
//        saveSensorHistoryUseCase()
    }
}