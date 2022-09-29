package com.preonboarding.sensordashboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.entity.SensorHistoryEntity

@Database(
    entities = [SensorHistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(HistoryTypeConverter::class)
abstract class SensorHistoryDatabase : RoomDatabase() {

    abstract fun sensorHistoryDao(): SensorHistoryDao

}