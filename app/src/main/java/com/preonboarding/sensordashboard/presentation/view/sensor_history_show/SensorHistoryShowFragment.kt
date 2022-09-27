package com.preonboarding.sensordashboard.presentation.view.sensor_history_show

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryShowBinding
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryShowFragment : BaseFragment<FragmentSensorHistoryShowBinding>(R.layout.fragment_sensor_history_show) {

    private val sensorHistoryViewModel: SensorHistoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}