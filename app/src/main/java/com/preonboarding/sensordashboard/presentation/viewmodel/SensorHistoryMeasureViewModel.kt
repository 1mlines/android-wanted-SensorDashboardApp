package com.preonboarding.sensordashboard.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.domain.usecase.SaveSensorHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorHistoryMeasureViewModel @Inject constructor(
    private val saveSensorHistoryUseCase: SaveSensorHistoryUseCase,
) : BaseViewModel() {
    var isRun: Boolean = false
    private lateinit var job: Job
    private var interval: Long = 100


    fun timerStart(fnCallback: () -> Unit) {
        isRun = true
        if (::job.isInitialized) job.cancel()

        job = viewModelScope.launch {
            while (true) {
                delay(interval)
                fnCallback()
            }
        }
    }

    fun timerStop() {
        isRun = false
        if (::job.isInitialized) job.cancel()
    }

    fun saveSensorHistory() {
//        saveSensorHistoryUseCase()
    }
}