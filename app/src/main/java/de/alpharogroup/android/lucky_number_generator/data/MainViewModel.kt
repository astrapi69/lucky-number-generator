package de.alpharogroup.android.lucky_number_generator.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: LotteryNumberCountRepository

    protected val compositeDisposable = CompositeDisposable()

    private val dataBaseInstance: LotteryNumberCountDatabase

    var lotteryNumberCountList = MutableLiveData<List<LotteryNumberCount>>()

    init {
        dataBaseInstance = LotteryNumberCountDatabase.getDatabase(application)
        repository = LotteryNumberCountDatabase.getDatabase(application).lotteryNumberCountRepository()
    }

    fun saveDataIntoDb(lotteryNumberCount: LotteryNumberCount){

        dataBaseInstance?.lotteryNumberCountRepository()?.insert(lotteryNumberCount)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }

    fun getData(){

        dataBaseInstance?.lotteryNumberCountRepository()?.findAll()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(!it.isNullOrEmpty()){
                    lotteryNumberCountList.postValue(it)
                }else{
                    lotteryNumberCountList.postValue(listOf())
                }
                it?.forEach {
                    Log.v("uuid value", it.id.toString())
                }
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun delete(lotteryNumberCount: LotteryNumberCount) {
        dataBaseInstance?.lotteryNumberCountRepository()?.delete(lotteryNumberCount)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                //Refresh Page data
                getData()
            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }

}