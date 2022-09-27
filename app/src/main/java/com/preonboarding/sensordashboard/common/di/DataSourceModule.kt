package com.preonboarding.sensordashboard.common.di

import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.local.datasourceimpl.SensorHistoryLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSensorHistoryLocalDataSource(
        sensorHistoryLocalDataSourceImpl: SensorHistoryLocalDataSourceImpl,
    ): SensorHistoryLocalDataSource
}