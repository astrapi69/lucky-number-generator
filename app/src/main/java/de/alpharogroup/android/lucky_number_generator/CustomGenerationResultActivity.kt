package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CustomGenerationResultActivity : AppCompatActivity() {

    private lateinit var luckyNumbers: String
    private lateinit var txtCustomGenerated: EditText

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_generation_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onInitialize()
    }

    private fun onInitialize() {
        luckyNumbers = intent.getStringExtra(CustomGenerationActivity.LUCKY_NUMBERS)
        txtCustomGenerated = findViewById(R.id.txt_custom_generated)
        txtCustomGenerated.setText(luckyNumbers)
    }

}
