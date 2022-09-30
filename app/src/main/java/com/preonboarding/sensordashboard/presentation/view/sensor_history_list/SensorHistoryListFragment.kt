package com.preonboarding.sensordashboard.presentation.view.sensor_history_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryListBinding
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SensorHistoryListFragment :
    BaseFragment<FragmentSensorHistoryListBinding>(R.layout.fragment_sensor_history_list) {

    private val sensorHistoryViewModel: SensorHistoryViewModel by activityViewModels()
    private val adapter: HistoryPagingAdapter by lazy {
        HistoryPagingAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewmodel = sensorHistoryViewModel
        }
        initAdapter()
        initViewmodel()
        sensorHistoryViewModel.sensorHistoryList()
    }

    private fun initAdapter() {
        binding.historyListRv.adapter = adapter
    }

    private fun initViewmodel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sensorHistoryViewModel.historyList.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}