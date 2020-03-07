package de.alpharogroup.android.lucky_number_generator.data

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable

@Dao
interface LotteryNumberCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: LotteryNumberCount): Completable

    @Query("select * from ${LotteryNumberCount.TABLE_NAME}")
    fun findAll(): LiveData<List<LotteryNumberCount>>

    @Delete
    fun delete(data: LotteryNumberCount): Completable

    @Update
    fun update(data: LotteryNumberCount)

    @Query("DELETE FROM ${LotteryNumberCount.TABLE_NAME}")
    suspend fun deleteAll()

}