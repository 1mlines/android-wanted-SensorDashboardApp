package com.preonboarding.sensordashboard.presentation.viewmodel

import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.usecase.DeleteSensorHistoryUseCase
import com.preonboarding.sensordashboard.domain.usecase.GetSensorHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SensorHistoryViewModel @Inject constructor(
    getSensorHistoryListUseCase: GetSensorHistoryListUseCase,
    private val deleteSensorHistoryUseCase: DeleteSensorHistoryUseCase,
) : BaseViewModel() {
    val sensorHistoryList: Flow<List<SensorHistory>> = getSensorHistoryListUseCase()

    fun deleteSensorHistory(){
//        deleteSensorHistoryUseCase()
    }
}