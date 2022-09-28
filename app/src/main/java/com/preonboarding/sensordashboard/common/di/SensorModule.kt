package com.preonboarding.sensordashboard.common.di

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Singleton
    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(SENSOR_SERVICE) as SensorManager

    @Singleton
    @Provides
    fun provideAccelerometerSensor(sensorManager: SensorManager): Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    @Singleton
    @Provides
    fun provideABC(sensorManager: SensorManager): Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
}