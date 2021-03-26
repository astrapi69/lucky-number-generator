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
import de.alpharogroup.android.lucky_number_generator.extensions.Extensions.newSecureRandom
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawLotteryNumbersFactory
import de.alpharogroup.lottery.drawing.DrawSuperNumbersFactory
import de.alpharogroup.random.number.RandomIntFactory
import java.security.SecureRandom
import java.util.*

class Lottery6Of49GenerationActivity : AppCompatActivity() {

    private lateinit var txtFirstNumber1Of49: EditText
    private lateinit var txtSecondNumber1Of49: EditText
    private lateinit var txtThirdNumber1Of49: EditText
    private lateinit var txtFourthNumber1Of49: EditText
    private lateinit var txtFifthNumber1Of49: EditText
    private lateinit var txtSixthNumber1Of49: EditText
    private lateinit var txtSuperNumber1Of49: EditText
    private lateinit var txtSuperSixNumber1Of49: EditText
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
        setContentView(R.layout.activity_lottery6_of49_generation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        onInitialize()
        disableEditTexts()
    }

    private fun onInitialize() {
        txtFirstNumber1Of49 = findViewById(R.id.firstNumber1Of49)
        txtSecondNumber1Of49 = findViewById(R.id.secondNumber1Of49)
        txtThirdNumber1Of49 = findViewById(R.id.thirdNumber1Of49)
        txtFourthNumber1Of49 = findViewById(R.id.fourthNumber1Of49)
        txtFifthNumber1Of49 = findViewById(R.id.fifthNumber1Of49)
        txtSixthNumber1Of49 = findViewById(R.id.sixthNumber1Of49)
        txtSuperNumber1Of49 = findViewById(R.id.superNumber1Of49)
        txtSuperSixNumber1Of49 = findViewById(R.id.superSixNumber1Of49)
        txtDrawDate = findViewById(R.id.drawDate)
        txtDrawDate.inputType = InputType.TYPE_NULL
        EditTextDateTimePicker(
            txtDrawDate,
            this,
            Constants.DEFAULT_DATE_TIME_FORMAT
        )

    }

    private fun disableEditTexts() {
        txtFirstNumber1Of49.isEnabled = false
        txtSecondNumber1Of49.isEnabled = false
        txtThirdNumber1Of49.isEnabled = false
        txtFourthNumber1Of49.isEnabled = false
        txtFifthNumber1Of49.isEnabled = false
        txtSixthNumber1Of49.isEnabled = false
        txtSuperNumber1Of49.isEnabled = false
        txtSuperSixNumber1Of49.isEnabled = false
    }

    fun onGenerate1Of49(view: View) {
        onDraw()
    }

    private fun onDraw() {
        val max = 6
        val volume = 49
        val currentDrawDateValue: String = txtDrawDate.text.toString()
        val currentDrawDate: Date = Extensions.parseToDate(currentDrawDateValue, Constants.DEFAULT_DATE_TIME_FORMAT)
        val secureRandom: SecureRandom = newSecureRandom(currentDrawDate)
        val lotteryNumbers = DrawLotteryNumbersFactory.draw(max, volume, secureRandom)
        val superNumber =
            DrawSuperNumbersFactory.drawSuperNumber(lotteryNumbers, volume, secureRandom)
        val superSixNumber = RandomIntFactory.randomIntBetween(1, 10, secureRandom)

        val toIntArray = lotteryNumbers.toIntArray()
        txtFirstNumber1Of49.setText(toIntArray[0].toString())
        txtSecondNumber1Of49.setText(toIntArray[1].toString())
        txtThirdNumber1Of49.setText(toIntArray[2].toString())
        txtFourthNumber1Of49.setText(toIntArray[3].toString())
        txtFifthNumber1Of49.setText(toIntArray[4].toString())
        txtSixthNumber1Of49.setText(toIntArray[5].toString())
        txtSuperNumber1Of49.setText(superNumber.toString())
        txtSuperSixNumber1Of49.setText(superSixNumber.toString())
        val numberCounterMap = MapFactory.newCounterMap(
            ListFactory.newRangeList(
                1,
                49
            )
        )
        val mergeAndSummarize = MapExtensions.mergeAndSummarize(numberCounterMap, lotteryNumbers)
        val filteredMap = mergeAndSummarize.filterValues { it > 0 }
        val lotteryNumberCount = LotteryNumberCount(
            id = UUID.randomUUID(), lotteryGameType = "6of49",
            numberCounterMap = filteredMap.toMutableMap()
        )
        viewModel?.insert(lotteryNumberCount)

    }

}
