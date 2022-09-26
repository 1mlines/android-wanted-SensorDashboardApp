package com.preonboarding.sensordashboard.presentation.sensor_history_measure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryMeasureBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryMeasureFragment : BaseFragment<FragmentSensorHistoryMeasureBinding>(R.layout.fragment_sensor_history_measure) {

    private val sensorHistoryMeasureViewModel: SensorHistoryMeasureViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryMeasureViewModel
    }
}