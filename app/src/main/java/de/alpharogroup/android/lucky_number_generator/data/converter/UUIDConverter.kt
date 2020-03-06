package de.alpharogroup.android.lucky_number_generator.data.converter

import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {
    @TypeConverter
    fun fromString(value: String?): UUID {
        return UUID.fromString(value)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }
}