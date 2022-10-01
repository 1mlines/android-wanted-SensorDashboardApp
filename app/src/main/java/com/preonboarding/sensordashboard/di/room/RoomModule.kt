package com.preonboarding.sensordashboard.di.room

import android.content.Context
import androidx.room.Room
import com.preonboarding.sensordashboard.data.room.SensorDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/***
 * @Created by 서강휘 2022.09.28
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): SensorDataBase {
        return Room.databaseBuilder(
            context,
            SensorDataBase::class.java,
            "SensorDataBase"
        ).build()
    }
}