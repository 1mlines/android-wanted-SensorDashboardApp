package com.preonboarding.sensordashboard.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.preonboarding.sensordashboard.domain.model.SensorType
import com.preonboarding.sensordashboard.util.DateUtil

/***
 * @Created by 서강휘 2022.09.28
 */

@Entity(tableName = "SensorDataEntity")
data class SensorDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "dataList") val dataList: String,
    @ColumnInfo(name = "type") val type: SensorType,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "dateValue") val dateValue: Long,
    @ColumnInfo(name = "time") val time: Float
) {

    companion object {
        val EMPTY = SensorDataEntity(
            id = 0,
            dataList = "",
            type = SensorType.EMPTY,
            date = "",
            dateValue = 0,
            time = 60f
        )
    }
}
