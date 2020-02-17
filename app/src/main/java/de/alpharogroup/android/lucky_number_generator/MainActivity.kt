package de.alpharogroup.android.lucky_number_generator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountDatabase
import de.alpharogroup.android.lucky_number_generator.data.MainViewModel

class MainActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onLottery6Of49(view: View) {
        val intent = Intent(this, Lottery6Of49GenerationActivity::class.java)
        startActivity(intent)
    }

    fun onEurojackpot(view: View) {
        val intent = Intent(this, EurojackpotGenerationActivity::class.java)
        startActivity(intent)
    }

    fun onCustomGeneration(view: View) {
        val intent = Intent(this, CustomGenerationActivity::class.java)
        startActivity(intent)
    }

    fun onLotteryNumberCountList(view: View) {
        val intent = Intent(this, LotteryNumberCountListActivity::class.java)
        startActivity(intent)
    }
}
