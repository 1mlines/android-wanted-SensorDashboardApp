package com.preonboarding.sensordashboard.data.repository

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.preonboarding.sensordashboard.data.dto.AxisData
import com.preonboarding.sensordashboard.data.room.SensorDataBase
import com.preonboarding.sensordashboard.data.room.entity.SensorDataEntity
import com.preonboarding.sensordashboard.di.coroutine.SensorScopeQualifier
import com.preonboarding.sensordashboard.di.sensor.AccSensorQualifier
import com.preonboarding.sensordashboard.di.sensor.GyroSensorQualifier
import com.preonboarding.sensordashboard.domain.model.SensorAxisData
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.domain.model.SensorType
import com.preonboarding.sensordashboard.util.DateUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/***
 * @Created by 서강휘 2022.09.27
 */

class LocalDataSource @Inject constructor(
    private val sensorManager: SensorManager,
    private val sensorDataBase: SensorDataBase,
    private val json: Json,
    @AccSensorQualifier private val accSensor: Sensor,
    @GyroSensorQualifier private val gyroSensor: Sensor,
    @SensorScopeQualifier private val coroutineScope: CoroutineScope
) {
    private val errorFlow = MutableSharedFlow<Throwable>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val ceh = CoroutineExceptionHandler { _, throwable ->
        coroutineScope.launch {
            errorFlow.emit(throwable)
        }
    }

    private val sensorScope = coroutineScope + ceh

    private val sensorDao = sensorDataBase.sensorDao()

    private val SENSOR_DELAY = 100000 //10hz to microsecond

    fun getAccFlow(): Flow<SensorAxisData> {
        return callbackFlow {
            var listener: SensorEventListener? = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { sEvent ->
                        val array = sEvent.values

                        val sensorData = AxisData(
                            x = array[0],
                            y = array[1],
                            z = array[2],
                            type = SensorType.ACC
                        ).toSensorAxisData()

                        trySend(sensorData).onFailure { Error ->
                            Error?.let { throwable ->
                                close() // channel 종료

                                sensorScope.launch {
                                    errorFlow.emit(throwable)
                                }
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                accSensor,
                SENSOR_DELAY
            )

            awaitClose {
                sensorManager.unregisterListener(listener)
                listener = null
            }
        }
    }

    fun getGyroFlow(): Flow<SensorAxisData> {
        var i = 0
        return callbackFlow {
            var listener: SensorEventListener? = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    //todo 드리프트 보상이 없는 무보정 x,y,z 값 리턴
                    //todo 추후에 보정된 값으로 변경 예정

                    event?.let { sEvent ->
                        val array = sEvent.values

                        val sensorData = AxisData(
                            x = array[0],
                            y = array[1],
                            z = array[2],
                            type = SensorType.GYRO
                        ).toSensorAxisData()

                        trySend(sensorData).onFailure { Error ->
                            Error?.let { throwable ->
                                close() // channel 종료

                                sensorScope.launch {
                                    errorFlow.emit(throwable)
                                }
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                gyroSensor,
                SENSOR_DELAY
            )

            awaitClose {
                sensorManager.unregisterListener(listener)
                listener = null
            }
        }
    }

    suspend fun insertSensorData(sensorData: SensorData) {
        sensorDataBase.withTransaction {
            sensorDao.insertSensorData(sensorData.toEntity(json))
        }
    }

    fun getSensorDataPagingSource(): PagingSource<Int, SensorDataEntity> {
        return sensorDao.getSensorDataPagingSource()
    }

    fun getErrorFlow(): MutableSharedFlow<Throwable> {
        return errorFlow
    }

    suspend fun deleteSensorData(id: Long) {
        sensorDataBase.withTransaction {
            sensorDao.deleteSensorData(id)
        }
    }

}