package com.preonboarding.sensordashboard.common.di

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object SensorModule {

    @ActivityScoped
    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(SENSOR_SERVICE) as SensorManager

}