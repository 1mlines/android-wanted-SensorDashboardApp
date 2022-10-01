package com.preonboarding.sensordashboard.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.preonboarding.sensordashboard.domain.model.SensorType

object BindingAdapterUtil {
    @JvmStatic
    @BindingAdapter("setType")
    fun setType(view: TextView, type: SensorType) {
        view.text = when (type) {
            SensorType.ACC -> "Accelerometer"
            SensorType.GYRO -> "Gyro"
            else -> {
                ""
            }
        }
    }
}