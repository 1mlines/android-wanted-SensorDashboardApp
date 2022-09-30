package com.preonboarding.sensordashboard.presentation.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.preonboarding.sensordashboard.common.constant.ViewName
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.presentation.view.sensor_history_list.SensorHistoryListFragmentDirections

@BindingAdapter("app:navigateToMeasure")
fun navigateToMeasure(view: View, dummy: Any?) {
    view.setOnClickListener {
        val action = SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryMeasureFragment()
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("app:history", "app:viewName")
fun navigateToPlay(view: View, history: SensorHistory, viewName: ViewName) {
    view.setOnClickListener {
        val action = SensorHistoryListFragmentDirections.actionSensorHistoryListFragmentToSensorHistoryPlayFragment(history, viewName)
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("app:navigateUp")
fun navigateUp(view: View, dummy: Any?) {
    view.setOnClickListener {
        view.findNavController().navigateUp()
    }
}