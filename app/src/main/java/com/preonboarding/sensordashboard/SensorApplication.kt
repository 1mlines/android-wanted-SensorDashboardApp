package com.preonboarding.sensordashboard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/***
 * @Created by 서강휘 2022.09.28
 */

@HiltAndroidApp
class SensorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}