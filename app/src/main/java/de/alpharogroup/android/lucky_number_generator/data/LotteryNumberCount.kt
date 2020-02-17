package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import androidx.room.*
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Entity(tableName = LotteryNumberCount.TABLE_NAME)
data class LotteryNumberCount(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var id: Int,
    @ColumnInfo(name = LOTTERY_GAME_TYPE)
    var lotteryGameType: String = "",
    var numberCounterMap: String = ""
) {
    companion object {
        const val TABLE_NAME = "lottery_numbers_statistics"
        const val ID = "id"
        const val LOTTERY_GAME_TYPE = "lottery_game_type"
    }
}