package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import androidx.room.*
import de.alpharogroup.android.lucky_number_generator.data.converter.IntegerIntegerMapConverter
import de.alpharogroup.android.lucky_number_generator.data.converter.UUIDConverter

@Database(entities = arrayOf(LotteryNumberCount::class), version = 1, exportSchema = false)
@TypeConverters(IntegerIntegerMapConverter::class, UUIDConverter::class)
abstract class LotteryNumberCountDatabase : RoomDatabase() {
    abstract fun lotteryNumberCountDao(): LotteryNumberCountDao

    companion object {
        @Volatile
        private var INSTANCE: LotteryNumberCountDatabase? = null

        fun getDatabase(
            context: Context
        ): LotteryNumberCountDatabase
            {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = buildDatabaseInstance(context)
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }

        private fun buildDatabaseInstance(context: Context) =
            Room.databaseBuilder(context, LotteryNumberCountDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}