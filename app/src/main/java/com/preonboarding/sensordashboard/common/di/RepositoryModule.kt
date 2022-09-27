package com.preonboarding.sensordashboard.common.di

import com.preonboarding.sensordashboard.data.repositoryimpl.SensorHistoryRepositoryImpl
import com.preonboarding.sensordashboard.domain.repository.SensorHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSensorHistoryRepository(
        sensorHistoryRepositoryImpl: SensorHistoryRepositoryImpl,
    ): SensorHistoryRepository

}