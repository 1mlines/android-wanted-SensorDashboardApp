package com.preonboarding.sensordashboard.common.di

import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.local.datasourceimpl.SensorHistoryLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideSensorHistoryLocalDataSource(
        sensorHistoryDao: SensorHistoryDao,
    ): SensorHistoryLocalDataSource {
        return SensorHistoryLocalDataSourceImpl(sensorHistoryDao)
    }
}