package de.alpharogroup.android.lucky_number_generator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawnLotteryNumbersExtensions
import de.alpharogroup.math.MathExtensions
import de.alpharogroup.string.StringExtensions
import java.util.*

class CustomGenerationActivity : AppCompatActivity() {

    companion object {
        const val LUCKY_NUMBERS = "de.alpharogroup.android.lucky_number_generator.LUCKY_NUMBERS"
    }

    private lateinit var txtNumbersToDraw: EditText
    private lateinit var txtMinVolume: EditText
    private lateinit var txtMaxVolume: EditText
    private lateinit var txtIterations: EditText
    private var viewModel: LotteryNumberCountViewModel? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun EditText.validate(message: String, validator: (String) -> Boolean): Boolean {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
        this.error = if (validator(this.text.toString())) null else message
        return this.error != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_generation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        onInitialize()

    }

    private fun onInitialize() {
        txtNumbersToDraw = findViewById(R.id.txt_numbers_to_draw)
        txtMinVolume = findViewById(R.id.txt_min_volume)
        txtMaxVolume = findViewById(R.id.txt_max_volume)
        txtIterations = findViewById(R.id.txt_iterations)
    }

    private fun String.isNumber(): Boolean = StringExtensions.isNumber(this)
    private fun String.isNumberBetween(min: Int, max: Int): Boolean =
        this.isNumber() && MathExtensions.isBetween(min, max, this.toInt(), true, true)

    private fun String.isNumberAndGreaterAs(number: Int): Boolean =
        this.isNumber() && number < this.toInt()

    fun onGenerateCustomNumbers(view: View) {
        val errorMessage = resources.getString(R.string.validation_not_empty)
        val greaterThanMessage = resources.getString(R.string.validation_max_greater_than_min)
        val between1to50Message = resources.getString(R.string.validation_is_between_1_50)
        val between1to100Message = resources.getString(R.string.validation_is_between_1_100)

        val minVolumeString = txtMinVolume.text.toString()
        if (txtNumbersToDraw.validate(errorMessage) { s ->
                s.isNotEmpty()
            }) {
            return
        }
        if (txtNumbersToDraw.validate(between1to50Message) { s ->
                s.isNumberBetween(1, 50)
            }) {
            return
        }
        if (txtMinVolume.validate(errorMessage) { s ->
                s.isNotEmpty()
            }) {
            return
        }
        if (txtMinVolume.validate(between1to50Message) { s ->
                s.isNumberBetween(1, 50)
            }) {
            return
        }
        if (txtMaxVolume.validate(errorMessage) { s ->
                s.isNotEmpty() && minVolumeString.isNumber()
            }) {
            return
        }
        if (txtMaxVolume.validate(between1to100Message) { s ->
                s.isNumberBetween(1, 100)
            }) {
            return
        }
        if (txtMaxVolume.validate(greaterThanMessage) { s ->
                s.isNumberAndGreaterAs(minVolumeString.toInt())
            }) {
            return
        }
        if (txtIterations.validate(errorMessage) { s ->
                s.isNotEmpty() && s.isNumberBetween(1, 500)
            }) {
            return
        }
        onDraw()
    }

    private fun onDraw() {

        val numbersToDraw = txtNumbersToDraw.text.toString()
        val minVolumeString = txtMinVolume.text.toString()
        val maxVolumeString = txtMaxVolume.text.toString()
        val iterations = txtIterations.text.toString()

        val maxNumbers = numbersToDraw.ifEmpty { "5" }.toInt()
        val minVolume = minVolumeString.ifEmpty { "1" }.toInt()
        val maxVolume = maxVolumeString.ifEmpty { "50" }.toInt()
        val drawCount = iterations.ifEmpty { "500" }.toInt()
        val drawFromMultiMap =
            DrawnLotteryNumbersExtensions.drawFromMultiMap(
                maxNumbers,
                minVolume,
                maxVolume,
                drawCount
            )
        val mergeAndSummarize = MapExtensions.mergeAndSummarize(
            MapFactory.newCounterMap(drawFromMultiMap),
            drawFromMultiMap
        )
        val toSortedMap = mergeAndSummarize.toSortedMap()
        val lotteryNumberCount = LotteryNumberCount(
            id = UUID.randomUUID(), lotteryGameType = "custom",
            numberCounterMap = toSortedMap
        )
        viewModel?.insert(lotteryNumberCount)
        val drawFromMultiMapString = drawFromMultiMap.toString()
        val intent = Intent(this, CustomGenerationResultActivity::class.java).apply {
            putExtra(LUCKY_NUMBERS, drawFromMultiMapString.removeFirstAndLastCharacter())
        }
        startActivity(intent)

    }

    private fun String.removeFirstAndLastCharacter(): String {
        return this.substring(1, this.length - 1)
    }

}
