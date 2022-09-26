package com.preonboarding.sensordashboard.presentation.viewmodel

import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.domain.usecase.SaveSensorHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SensorHistoryMeasureViewModel @Inject constructor(
    private val saveSensorHistoryUseCase: SaveSensorHistoryUseCase,
) : BaseViewModel() {

    fun saveSensorHistory() {
//        saveSensorHistoryUseCase()
    }
}