package com.preonboarding.sensordashboard.di.repository

import com.preonboarding.sensordashboard.data.repository.SensorRepositoryImpl
import com.preonboarding.sensordashboard.domain.repository.SensorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/***
 * @Created by 서강휘 2022.09.27
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsSensorRepository(sensorRepository: SensorRepositoryImpl): SensorRepository

}