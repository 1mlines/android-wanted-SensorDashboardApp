package com.preonboarding.sensordashboard.util

sealed class CustomTimerState{
    object Start: CustomTimerState()
    object Stop: CustomTimerState()
}
