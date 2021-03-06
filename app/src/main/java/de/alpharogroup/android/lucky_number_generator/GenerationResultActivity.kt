package de.alpharogroup.android.lucky_number_generator

import android.app.ActionBar.LayoutParams
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class GenerationResultActivity : AppCompatActivity() {

    private lateinit var luckyNumbers: String

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onInitialize()
    }

    private fun onInitialize() {
        luckyNumbers = intent.getStringExtra(CustomGenerationActivity.LUCKY_NUMBERS)
        val replaceWithoutSpaces = luckyNumbers.replace(" ", "")
            .split(",")

        val lm = findViewById<View>(R.id.linearLayoutGenerationResult) as LinearLayout

        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val density = resources.displayMetrics.density
        val dpWidth: Float = displayMetrics.widthPixels / density
        val horizontalBallsFit = (dpWidth / 60).toInt()
        val chunks = replaceWithoutSpaces.chunked(horizontalBallsFit)

        for (chunk in chunks) {
            val ll = LinearLayout(this)
            ll.orientation = LinearLayout.HORIZONTAL
            for (j in chunk) {
                // Create TextView
                val luckyNumber = TextView(this)
                luckyNumber.text = j
                luckyNumber.setTextColor(Color.parseColor("#ffffffff"))
                luckyNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
                luckyNumber.background = ContextCompat.getDrawable(this, R.drawable.circle)
                luckyNumber.gravity = Gravity.CENTER
                ll.addView(luckyNumber)
            }
            //Add TextView to LinearLayout defined in XML
            lm.addView(ll)
        }

    }

}