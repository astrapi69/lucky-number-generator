package de.alpharogroup.android.lucky_number_generator.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListLotteryNumberCountConverter {
    @TypeConverter
    fun fromString(value: String?): List<LotteryNumberCount> {
        val type =
            object : TypeToken<List<LotteryNumberCount?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: MutableList<LotteryNumberCount>?): String {
        return Gson().toJson(list)
    }
}