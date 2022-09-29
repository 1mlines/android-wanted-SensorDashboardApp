package com.preonboarding.sensordashboard.presentation.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.preonboarding.sensordashboard.common.constant.ViewName
import com.preonboarding.sensordashboard.presentation.view.sensor_history_list.SensorHistoryListFragmentDirections

@BindingAdapter("app:navigateTo")
fun navigateTo(view: View, viewName: ViewName) {
    view.setOnClickListener {
        val action = when (viewName) {
            ViewName.MEASURE -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryMeasureFragment()
            ViewName.PLAY -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryPlayFragment()
            ViewName.SHOW -> SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryShowFragment()
        }
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("app:navigateUp")
fun navigateUp(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().navigateUp()
    }
}