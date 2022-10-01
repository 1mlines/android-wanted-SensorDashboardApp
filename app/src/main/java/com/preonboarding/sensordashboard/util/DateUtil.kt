package com.preonboarding.sensordashboard.util

import java.sql.Timestamp
import java.text.SimpleDateFormat

/***
 * @Created by 서강휘 2022.09.28
 */

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