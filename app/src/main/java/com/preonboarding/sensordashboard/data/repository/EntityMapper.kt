package com.preonboarding.sensordashboard.data.repository

import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.util.DateUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/***
 * @Created by 서강휘 2022.09.28
 */

fun SensorData.toEntity(json: Json) = SensorDataEntity.EMPTY.copy(
    dataList = sensorAxisToString(json, this.dataList),
    type = this.type,
    date = this.date,
    dateValue = DateUtil.getTime(),
    time = this.time
)

fun sensorAxisToString(json: Json, list: List<SensorAxisData>): String {
    return json.encodeToString(list)
}