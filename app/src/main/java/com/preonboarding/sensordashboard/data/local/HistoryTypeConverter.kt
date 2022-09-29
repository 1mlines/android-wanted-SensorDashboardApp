package com.preonboarding.sensordashboard.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class HistoryTypeConverter {

    @TypeConverter
    fun fromFloatList(value: List<Float>?): String = Gson().toJson(value)

    @TypeConverter
    fun toFloatList(value: String) = Gson().fromJson(value, Array<Float>::class.java).toList()

}