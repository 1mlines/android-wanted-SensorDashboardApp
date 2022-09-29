package com.preonboarding.sensordashboard.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SensorHistory(
    val id: Long,
    val type: String,
    val publishedAt: String,
    val period: String,
    val xList: List<Float>,
    val yList: List<Float>,
    val zList: List<Float>
):Serializable