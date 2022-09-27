package com.preonboarding.sensordashboard.common.di

import android.content.Context
import androidx.room.Room
import com.preonboarding.sensordashboard.common.constant.Constants.DB_NAME
import com.preonboarding.sensordashboard.data.local.SensorHistoryDatabase
import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): SensorHistoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SensorHistoryDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(sensorHistoryDatabase: SensorHistoryDatabase): SensorHistoryDao {
        return sensorHistoryDatabase.sensorHistoryDao()
    }
}