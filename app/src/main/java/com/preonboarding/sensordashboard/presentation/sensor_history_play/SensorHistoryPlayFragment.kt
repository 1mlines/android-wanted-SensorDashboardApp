package com.preonboarding.sensordashboard.presentation.sensor_history_play

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryPlayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryPlayFragment : BaseFragment<FragmentSensorHistoryPlayBinding>(R.layout.fragment_sensor_history_play) {

    private val sensorHistoryPlayViewModel: SensorHistoryPlayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryPlayViewModel
    }
}