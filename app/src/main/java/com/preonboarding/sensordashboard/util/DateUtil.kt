package com.preonboarding.sensordashboard.util

import java.sql.Timestamp
import java.text.SimpleDateFormat

object DateUtil {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    fun getCurrentTime(): String {
        val timeStamp = Timestamp(System.currentTimeMillis())

        return dateFormat.format(timeStamp)
    }

    fun getTime(): Long {
        val timeStamp = Timestamp(System.currentTimeMillis())

        return timeStamp.time
    }
}