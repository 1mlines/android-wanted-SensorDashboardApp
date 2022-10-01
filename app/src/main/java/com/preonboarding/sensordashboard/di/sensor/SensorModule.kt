package com.preonboarding.sensordashboard.di.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.preonboarding.sensordashboard.di.sensor.AccSensorQualifier
import com.preonboarding.sensordashboard.di.sensor.GyroSensorQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/***
 * @Created by 서강휘 2022.09.27
 */

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @AccSensorQualifier
    @Provides
    @Singleton
    fun providesAccSensor(sensorManager: SensorManager): Sensor {
        return sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

    @GyroSensorQualifier
    @Provides
    @Singleton
    fun providesGyroSensor(sensorManager: SensorManager): Sensor {
        return sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)
    }

}