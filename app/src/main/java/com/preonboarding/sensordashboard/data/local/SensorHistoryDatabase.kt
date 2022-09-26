package com.preonboarding.sensordashboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.domain.model.SensorHistory

@Database(entities = [SensorHistory::class], version = 1, exportSchema = false)
abstract class SensorHistoryDatabase : RoomDatabase() {

    abstract fun sensorHistoryDao(): SensorHistoryDao

}