package com.unltm.distance.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StringListConverter {
    @TypeConverter
    fun revert(jsonString: String): List<String> {
        return Gson().fromJson(jsonString, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun converter(list: List<String>): String {
        return Gson().toJson(list)
    }
}