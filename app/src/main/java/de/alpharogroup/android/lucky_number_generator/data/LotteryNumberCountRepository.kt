package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import androidx.room.*
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Dao
interface LotteryNumberCountRepository {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: LotteryNumberCount) : Completable

    @Query("select * from ${LotteryNumberCount.TABLE_NAME}")
    fun findAll():Single<List<LotteryNumberCount>>

    @Delete
    fun delete(data: LotteryNumberCount):Completable

    @Update
    fun update(data: LotteryNumberCount)

}