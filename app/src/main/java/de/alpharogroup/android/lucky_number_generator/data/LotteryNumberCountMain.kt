package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import androidx.room.*
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

const val DB_VERSION = 1

const val DB_NAME = "LotteryNumberCountDatabase.db"

fun main(args: Array<String>) {
    var numberCounterMap = LotteryNumberCount(id = UUID.randomUUID(), lotteryGameType = "6of49", numberCounterMap  = MapFactory.newCounterMap(
        ListFactory.newRangeList(
            1,
            49
        )
    ))
    println(numberCounterMap)
}