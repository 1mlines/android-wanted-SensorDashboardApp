package com.preonboarding.sensordashboard.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.domain.usecase.DeleteSensorHistoryUseCase
import com.preonboarding.sensordashboard.domain.usecase.GetSensorHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorHistoryViewModel @Inject constructor(
    private val getSensorHistoryListUseCase: GetSensorHistoryListUseCase,
    private val deleteSensorHistoryUseCase: DeleteSensorHistoryUseCase,
) : BaseViewModel() {

    private val _historyList: MutableStateFlow<PagingData<SensorHistory>> =
        MutableStateFlow<PagingData<SensorHistory>>(PagingData.empty())
    val historyList: StateFlow<PagingData<SensorHistory>> = _historyList.asStateFlow()

    fun sensorHistoryList() {
        viewModelScope.launch {
            getSensorHistoryListUseCase.invoke().cachedIn(viewModelScope).collectLatest {
                _historyList.emit(it)
            }
        }
    }

    fun deleteSensorHistory(sensorHistory: SensorHistory){
        viewModelScope.launch {
            deleteSensorHistoryUseCase(sensorHistory)
        }
    }
}