package com.preonboarding.sensordashboard.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity

/***
 * @Created by 서강휘 2022.09.28
 */

@Dao
interface SensorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSensorData(sensorDataEntity: SensorDataEntity)

    @Query("SELECT * FROM SensorDataEntity ORDER BY dateValue DESC")
    fun getSensorDataPagingSource(): PagingSource<Int, SensorDataEntity>

    @Query("DELETE FROM SensorDataEntity WHERE id = :id")
    fun deleteSensorData(id: Long)
}