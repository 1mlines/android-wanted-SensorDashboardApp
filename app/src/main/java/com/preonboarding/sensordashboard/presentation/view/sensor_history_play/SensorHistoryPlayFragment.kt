package com.preonboarding.sensordashboard.presentation.view.sensor_history_play

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryPlayBinding
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryPlayFragment : BaseFragment<FragmentSensorHistoryPlayBinding>(R.layout.fragment_sensor_history_play) {

    private val sensorHistoryViewModel: SensorHistoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryViewModel
    }
}