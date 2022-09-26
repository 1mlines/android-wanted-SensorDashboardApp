package com.preonboarding.sensordashboard.presentation.sensor_history_show

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryShowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryShowFragment : BaseFragment<FragmentSensorHistoryShowBinding>(R.layout.fragment_sensor_history_show) {

    private val sensorHistoryShowViewModel: SensorHistoryShowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryShowViewModel
    }
}