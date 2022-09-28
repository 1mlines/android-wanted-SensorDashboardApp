package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SensorData.toEntity(json: Json) = SensorDataEntity.EMPTY.copy(
    dataList = sensorAxisToString(json, this.dataList),
    type = this.type,
    date = this.date,
    time = this.time
)

fun sensorAxisToString(json: Json, list: List<SensorAxisData>): String {
    return json.encodeToString(list)
}