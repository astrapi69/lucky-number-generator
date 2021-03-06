package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.android.lucky_number_generator.extensions.Constants
import de.alpharogroup.android.lucky_number_generator.extensions.Extensions
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawLotteryNumbersFactory
import de.alpharogroup.lottery.drawing.DrawMultiMapLotteryNumbersFactory
import de.alpharogroup.random.number.RandomIntFactory
import io.github.astrapi69.android.datetime.EditTextDateTimePicker
import java.security.SecureRandom
import java.util.*

class EurojackpotGenerationActivity : AppCompatActivity() {

    lateinit var txtFirstNumber1Of50: EditText
    lateinit var txtSecondNumber1Of50: EditText
    lateinit var txtThirdNumber1Of50: EditText
    lateinit var txtFourthNumber1Of50: EditText
    lateinit var txtFifthNumber1Of50: EditText
    lateinit var txtFirstEuroNumber1Of10: EditText
    lateinit var txtSecondEuroNumber1Of10: EditText
    private lateinit var txtDrawDate: EditText
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
        setContentView(R.layout.activity_eurojackpot_generation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        onInitialize()
        disableEditTexts()
    }

    private fun onInitialize() {
        txtFirstNumber1Of50 = findViewById<EditText>(R.id.firstNumber1Of50)
        txtSecondNumber1Of50 = findViewById<EditText>(R.id.secondNumber1Of50)
        txtThirdNumber1Of50 = findViewById<EditText>(R.id.thirdNumber1Of50)
        txtFourthNumber1Of50 = findViewById<EditText>(R.id.fourthNumber1Of50)
        txtFifthNumber1Of50 = findViewById<EditText>(R.id.fifthNumber1Of50)
        txtFirstEuroNumber1Of10 = findViewById<EditText>(R.id.firstEuroNumber1Of10)
        txtSecondEuroNumber1Of10 = findViewById<EditText>(R.id.secondEuroNumber1Of10)
        txtDrawDate = findViewById(R.id.drawDateEuroJackpot)
        txtDrawDate.inputType = InputType.TYPE_NULL
        EditTextDateTimePicker(
            txtDrawDate,
            this,
            Constants.DEFAULT_DATE_TIME_FORMAT
        )
    }

    private fun disableEditTexts() {
        txtFirstNumber1Of50.setEnabled(false)
        txtSecondNumber1Of50.setEnabled(false)
        txtThirdNumber1Of50.setEnabled(false)
        txtFourthNumber1Of50.setEnabled(false)
        txtFifthNumber1Of50.setEnabled(false)
        txtFirstEuroNumber1Of10.setEnabled(false)
        txtSecondEuroNumber1Of10.setEnabled(false)
    }

    fun onGenerate1of50Numbers(view: View) {
        onDraw()
    }

    private fun onDraw() {
        onSetEurojackpotNumbers()
        onSetEuroNumbers()
    }

    private fun onSetEurojackpotNumbers() {
        val maxNumbers = 5
        val minVolume = 1
        val maxVolume = 50
        val drawCount = RandomIntFactory.randomIntBetween(200, 1000)
        val currentDrawDateValue: String = txtDrawDate.text.toString()
        val currentDrawDate: Date = Extensions.parseToDate(currentDrawDateValue, Constants.DEFAULT_DATE_TIME_FORMAT)
        val secureRandom: SecureRandom = Extensions.newSecureRandom(currentDrawDate)
        val eurojackpotNumbers =
            DrawMultiMapLotteryNumbersFactory.drawFromMultiMap(
                maxNumbers,
                minVolume,
                maxVolume,
                drawCount,
                secureRandom
            )
        val toIntArray = eurojackpotNumbers.toIntArray()

        txtFirstNumber1Of50.setText(toIntArray[0].toString())
        txtSecondNumber1Of50.setText(toIntArray[1].toString())
        txtThirdNumber1Of50.setText(toIntArray[2].toString())
        txtFourthNumber1Of50.setText(toIntArray[3].toString())
        txtFifthNumber1Of50.setText(toIntArray[4].toString())
        val numberCounterMap = MapFactory.newCounterMap(
            ListFactory.newRangeList(
                1,
                49
            )
        )
        val mergeAndSummarize =
            MapExtensions.mergeAndSummarize(numberCounterMap, eurojackpotNumbers)
        val filteredMap = mergeAndSummarize.filterValues { it > 0 }
        val lotteryNumberCount = LotteryNumberCount(
            id = UUID.randomUUID(), lotteryGameType = "eurojackpot",
            numberCounterMap = filteredMap.toMutableMap()
        )
        viewModel?.insert(lotteryNumberCount)
    }

    private fun onSetEuroNumbers() {
        val maxNumbers = 2
        val minVolume = 1
        val maxVolume = 10
        val eurojackpotNumbers = DrawLotteryNumbersFactory
            .draw(maxNumbers, minVolume, maxVolume)
        val toIntArray = eurojackpotNumbers.toIntArray()

        txtFirstEuroNumber1Of10.setText(toIntArray[0].toString())
        txtSecondEuroNumber1Of10.setText(toIntArray[1].toString())
    }

}
