package com.preonboarding.sensordashboard.di

import com.preonboarding.sensordashboard.data.local.datasource.SensorHistoryLocalDataSource
import com.preonboarding.sensordashboard.data.repositoryimpl.SensorHistoryRepositoryImpl
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSensorHistoryRepository(
        sensorHistoryLocalDataSource: SensorHistoryLocalDataSource,
    ): SensorHistoryRepository {
        return SensorHistoryRepositoryImpl(sensorHistoryLocalDataSource)
    }

}