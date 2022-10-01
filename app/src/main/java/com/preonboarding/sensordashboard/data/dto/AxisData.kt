package com.preonboarding.sensordashboard.data.dto

import com.preonboarding.sensordashboard.domain.model.SensorType

/***
 * @Created by 서강휘 2022.09.28
 */
data class AxisData(
    val x: Float,
    val y: Float,
    val z: Float,
    val type: SensorType
)