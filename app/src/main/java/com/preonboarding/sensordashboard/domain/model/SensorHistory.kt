package com.preonboarding.sensordashboard.domain.model

data class SensorHistory(
    val id: Long,
    val type: String,
    val publishedAt: String,
    val period: Double,
//    val xList: List<Double>,
//    val yList: List<Double>,
//    val zList: List<Double>,
)