package com.preonboarding.sensordashboard.presentation.sensor_history_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryListFragment : BaseFragment<FragmentSensorHistoryListBinding>(R.layout.fragment_sensor_history_list) {

    private val sensorHistoryListViewModel: SensorHistoryListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryListViewModel
    }
}