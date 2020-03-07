package de.alpharogroup.android.lucky_number_generator.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class LotteryNumberCountViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LotteryNumberCountRepository

    private val compositeDisposable = CompositeDisposable()

    private val dao: LotteryNumberCountDao =
        LotteryNumberCountDatabase.getDatabase(application).lotteryNumberCountDao()
    var lotteryNumberCountList:LiveData<List<LotteryNumberCount>>

    init {
        repository = LotteryNumberCountRepository(dao)
        lotteryNumberCountList = repository.findAll()
    }

    fun insert(data: LotteryNumberCount) = viewModelScope.launch {
        repository.insert(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                //Refresh Page data
                getData()
            },{

            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun getData(){
        lotteryNumberCountList = repository.findAll()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun delete(lotteryNumberCount: LotteryNumberCount) {
        repository.delete(lotteryNumberCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                //Refresh Page data
                getData()
            },{

            }).let {
                compositeDisposable.add(it)
            }
    }

}