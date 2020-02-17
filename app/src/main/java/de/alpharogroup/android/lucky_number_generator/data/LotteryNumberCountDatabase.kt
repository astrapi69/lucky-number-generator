package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import androidx.room.*
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Database(entities = arrayOf(LotteryNumberCount::class), version = 1, exportSchema = false)
abstract class LotteryNumberCountDatabase : RoomDatabase() {
    abstract fun lotteryNumberCountRepository(): LotteryNumberCountRepository
    companion object {
        @Volatile
        private var databseInstance: LotteryNumberCountDatabase? = null

        fun getDatabase(mContext: Context): LotteryNumberCountDatabase =
            databseInstance ?: synchronized(this) {
                databseInstance ?: buildDatabaseInstance(mContext).also {
                    databseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, LotteryNumberCountDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}