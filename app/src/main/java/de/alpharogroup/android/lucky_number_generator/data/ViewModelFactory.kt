package de.alpharogroup.android.lucky_number_generator.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LotteryNumberCountDao::class.java)) {
            return LotteryNumberCountViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}