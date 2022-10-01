package com.preonboarding.sensordashboard.di.usecase

import com.preonboarding.sensordashboard.data.usecase.AccSensorUseCaseImpl
import com.preonboarding.sensordashboard.data.usecase.ErrorUseCaseImpl
import com.preonboarding.sensordashboard.data.usecase.GyroSensorUseCaseImpl
import com.preonboarding.sensordashboard.data.usecase.RoomUseCaseImpl
import com.preonboarding.sensordashboard.domain.usecase.AccSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.ErrorUseCase
import com.preonboarding.sensordashboard.domain.usecase.GyroSensorUseCase
import com.preonboarding.sensordashboard.domain.usecase.RoomUseCase
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
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsAccUseCase(accSensorUseCase: AccSensorUseCaseImpl): AccSensorUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindsGyroUseCase(gyroUseCaseImpl: GyroSensorUseCaseImpl): GyroSensorUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindsErrorUseCase(errorUseCaseImpl: ErrorUseCaseImpl): ErrorUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindsRoomUseCase(roomUseCaseImpl: RoomUseCaseImpl): RoomUseCase
}