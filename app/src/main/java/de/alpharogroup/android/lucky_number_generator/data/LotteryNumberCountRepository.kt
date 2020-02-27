package de.alpharogroup.android.lucky_number_generator.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Single

class LotteryNumberCountRepository(private val dao: LotteryNumberCountDao) {

    fun insert(data: LotteryNumberCount): Completable {
        return dao.insert(data)
    }

    fun findAll(): LiveData<List<LotteryNumberCount>> {
        return dao.findAll()
    }

    fun delete(data: LotteryNumberCount):Completable {
        return dao.delete(data)
    }

    fun update(data: LotteryNumberCount) {
        dao.update(data)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }
}