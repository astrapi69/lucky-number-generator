package de.alpharogroup.android.lucky_number_generator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onLottery6Of49(view: View) {
        val intent = Intent(this, Lottery6Of49GenerationActivity::class.java)
        startActivity(intent)
    }

    fun onEurojackpot(view: View) {

    }

    fun onKeno(view: View) {

    }

    fun onCustomGeneration(view: View) {

    }
}
