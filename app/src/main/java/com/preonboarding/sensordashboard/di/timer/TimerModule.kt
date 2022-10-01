package com.preonboarding.sensordashboard.di.timer

import com.preonboarding.sensordashboard.di.IoDispatcher
import com.preonboarding.sensordashboard.util.CustomTimer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TimerModule {

    /***
     * @Created by 박인아 2022.09.28
     */
    @Provides
    @Singleton
    fun provideTimer(@IoDispatcher ioDispatcher : CoroutineDispatcher ) : CustomTimer {
        return CustomTimer(ioDispatcher)
    }

}