package com.preonboarding.sensordashboard.domain.model

import kotlinx.serialization.Serializable

/***
 * @Created by 서강휘 2022.09.28
 */

@Serializable
data class SensorAxisData(
    val x: Float,
    val y: Float,
    val z: Float
)
