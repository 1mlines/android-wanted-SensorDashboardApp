package com.preonboarding.sensordashboard.presentation.view.sensor_history_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryListBinding
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SensorHistoryListFragment : BaseFragment<FragmentSensorHistoryListBinding>(R.layout.fragment_sensor_history_list) {

    private val sensorHistoryViewModel: SensorHistoryViewModel by activityViewModels()
    private lateinit var adapter: HistoryPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewmodel = sensorHistoryViewModel
            lifecycleOwner = this@SensorHistoryListFragment
            executePendingBindings()
        }
        initAdapter()
    }

    private fun initAdapter() {
        binding.historyListRv.adapter = adapter
        initViewmodel()
    }

    private fun initViewmodel() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.historyListRv.scrollToPosition(0) }
        }
        lifecycleScope.launch {
            sensorHistoryViewModel.sensorHistoryList.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}