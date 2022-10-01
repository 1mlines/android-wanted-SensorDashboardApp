package com.preonboarding.sensordashboard.di.json

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

/***
 * @Created by 서강휘 2022.09.28
 */

@Module
@InstallIn(SingletonComponent::class)
object JsonModule {

    @Provides
    fun provideJson() :Json = Json
}