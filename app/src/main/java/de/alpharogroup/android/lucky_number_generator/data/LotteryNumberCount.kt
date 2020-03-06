package de.alpharogroup.android.lucky_number_generator.data

import androidx.room.*
import de.alpharogroup.android.lucky_number_generator.data.converter.IntegerIntegerMapConverter
import de.alpharogroup.collections.map.MapFactory
import java.util.*

@Entity(tableName = LotteryNumberCount.TABLE_NAME)
data class LotteryNumberCount(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: UUID,
    @ColumnInfo(name = LOTTERY_GAME_TYPE)
    var lotteryGameType: String = "",
    @TypeConverters(IntegerIntegerMapConverter::class)
    var numberCounterMap: MutableMap<Int, Int> = MapFactory.newLinkedHashMap()
) {
    companion object {
        const val TABLE_NAME = "lottery_numbers_statistics"
        const val ID = "id"
        const val LOTTERY_GAME_TYPE = "lottery_game_type"
    }
}