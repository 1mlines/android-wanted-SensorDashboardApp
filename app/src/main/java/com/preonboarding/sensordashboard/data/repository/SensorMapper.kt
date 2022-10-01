package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.data.room.entity.SensorAxisDataEntity
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/***
 * @Created by 서강휘 2022.09.28
 */

fun SensorDataEntity.toModel(json: Json) = SensorData(
    id = this.id,
    dataList = stringToSensorList(json, this.dataList).map { it.toModel() },
    type = this.type,
    date = this.date,
    time = this.time
)

fun SensorAxisDataEntity.toModel() = SensorAxisData(
    x = this.x,
    y = this.y,
    z = this.z,
)

fun AxisData.toSensorAxisData() = SensorAxisData(
    x = this.x,
    y = this.y,
    z = this.z
)

fun stringToSensorList(json: Json, string: String): List<SensorAxisDataEntity> {
    return json.decodeFromString(string)
}