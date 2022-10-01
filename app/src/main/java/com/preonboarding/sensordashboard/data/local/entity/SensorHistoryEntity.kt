package com.preonboarding.sensordashboard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.preonboarding.sensordashboard.domain.model.SensorHistory

@Entity(tableName = "sensor_history_table")
data class SensorHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val type: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val publishedAt: String,
    val period: String, //double
    val xList: List<Float>,
    val yList: List<Float>,
    val zList: List<Float>
) {
    fun toModel(): SensorHistory {
        return SensorHistory(
            id = id,
            type = type,
            publishedAt = publishedAt,
            period = period,
            xList =xList,
            yList =yList,
            zList =zList
        )
    }
}

fun SensorHistory.toEntity(): SensorHistoryEntity{
    return SensorHistoryEntity(
        id = id,
        type = type,
        publishedAt = publishedAt,
        period = period,
        xList=xList,
        yList=yList,
        zList=zList
    )
}