package com.preonboarding.sensordashboard.domain.model

import com.preonboarding.sensordashboard.util.DateUtil
import kotlinx.serialization.Serializable

/***
 * @Created by 서강휘 2022.09.27
 */

@Serializable
data class SensorData(
    val id: Long,
    val dataList: List<SensorAxisData>,
    val type: SensorType,
    val date: String,
    val time: Float
) {

    companion object {
        val EMPTY = SensorData(
            id = 0,
            dataList = mutableListOf(),
            type = SensorType.EMPTY,
            date = DateUtil.getCurrentTime(),
            time = 60f
        )
    }
}
