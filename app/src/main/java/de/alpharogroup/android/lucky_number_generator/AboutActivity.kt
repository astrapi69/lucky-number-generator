package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.android.lucky_number_generator.extensions.Constants

class AboutActivity : AppCompatActivity() {

    private var viewModel: LotteryNumberCountViewModel? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        onInitialize()
    }

    private fun onInitialize() {

    }
}