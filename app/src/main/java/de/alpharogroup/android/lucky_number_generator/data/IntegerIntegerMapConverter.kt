package de.alpharogroup.android.lucky_number_generator.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class IntegerIntegerMapConverter {
    @TypeConverter
    fun fromString(value: String?): Map<Int, Int> {
        val mapType =
            object : TypeToken<Map<Int?, Int?>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromIntegerMap(map: MutableMap<Int, Int>?): String {
        val gson = Gson()
        return gson.toJson(map)
    }

}