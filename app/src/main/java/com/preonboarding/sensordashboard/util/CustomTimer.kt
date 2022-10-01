package com.preonboarding.sensordashboard.util

import android.annotation.SuppressLint
import com.preonboarding.sensordashboard.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CustomTimer @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private var timerJob : Job = Job()

    private val coroutineScope = CoroutineScope(ioDispatcher)
    private var isActive = false
    private var MAX_TIME = 60000L
    private var timerCount = 0L

    var formatTime = MutableSharedFlow<String>(0,1,BufferOverflow.DROP_OLDEST)

    @SuppressLint("SimpleDateFormat")
    fun setTimerState(state: CustomTimerState, time : Long? ) {
        if ( time != null ) {
            MAX_TIME = time
        }

        when (state) {
            is CustomTimerState.Start ->  startTimerJob()
            is CustomTimerState.Stop -> stopTimerJob()
        }
    }

    private fun startTimerJob(){
        if (timerJob.isActive) timerJob.cancel()
        val form = SimpleDateFormat("ss.SSS")

        timerJob = coroutineScope.launch {
            withContext(ioDispatcher) {
                this@CustomTimer.isActive = true
                while ( timerCount <= MAX_TIME && timerJob.isActive ) {
                    delay(100L)
                    timerCount += 100L
                    if ( timerCount == MAX_TIME + 100L ) {
                        formatTime.tryEmit("NULL")
                    } else {
                        formatTime.tryEmit(form.format(Date(timerCount)))
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun stopTimerJob():String{
        isActive = false
        timerCount = 0L
        if (timerJob.isActive) timerJob.cancel()
        return SimpleDateFormat("ss.SSS").format(Date(timerCount))
    }

}
