package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawnLotteryNumbersExtensions
import de.alpharogroup.random.number.RandomPrimitivesExtensions
import java.util.*

class EurojackpotGenerationActivity : AppCompatActivity() {

    lateinit var txtFirstNumber1Of50: EditText
    lateinit var txtSecondNumber1Of50: EditText
    lateinit var txtThirdNumber1Of50: EditText
    lateinit var txtFourthNumber1Of50: EditText
    lateinit var txtFifthNumber1Of50: EditText
    lateinit var txtFirstEuroNumber1Of10: EditText
    lateinit var txtSecondEuroNumber1Of10: EditText
    private var viewModel: LotteryNumberCountViewModel? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
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

    fun onInitialize() {
        txtFirstNumber1Of50 = findViewById<EditText>(R.id.firstNumber1Of50)
        txtSecondNumber1Of50 = findViewById<EditText>(R.id.secondNumber1Of50)
        txtThirdNumber1Of50 = findViewById<EditText>(R.id.thirdNumber1Of50)
        txtFourthNumber1Of50 = findViewById<EditText>(R.id.fourthNumber1Of50)
        txtFifthNumber1Of50 = findViewById<EditText>(R.id.fifthNumber1Of50)
        txtFirstEuroNumber1Of10 = findViewById<EditText>(R.id.firstEuroNumber1Of10)
        txtSecondEuroNumber1Of10 = findViewById<EditText>(R.id.secondEuroNumber1Of10)
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

    fun onDraw() {
        onSetEurojackpotNumbers()
        onSetEuroNumbers()
    }

    private fun onSetEurojackpotNumbers() {
        val maxNumbers = 5
        val minVolume = 1
        val maxVolume = 50
        val drawCount = RandomPrimitivesExtensions.getRandomIntBetween(200, 1000)
        val eurojackpotNumbers =
            DrawnLotteryNumbersExtensions.drawFromMultiMap(
                maxNumbers,
                minVolume,
                maxVolume,
                drawCount
            )
        val toIntArray = eurojackpotNumbers.toIntArray()

        txtFirstNumber1Of50.setText(toIntArray[0].toString())
        txtSecondNumber1Of50.setText(toIntArray[1].toString())
        txtThirdNumber1Of50.setText(toIntArray[2].toString())
        txtFourthNumber1Of50.setText(toIntArray[3].toString())
        txtFifthNumber1Of50.setText(toIntArray[4].toString())
        var numberCounterMap = MapFactory.newCounterMap(
            ListFactory.newRangeList(
                1,
                49
            )
        )
        val mergeAndSummarize = MapExtensions.mergeAndSummarize(numberCounterMap, eurojackpotNumbers)
        val filteredMap = mergeAndSummarize.filterValues{it > 0}
        var lotteryNumberCount = LotteryNumberCount(
            id = UUID.randomUUID(), lotteryGameType = "eurojackpot",
            numberCounterMap = filteredMap.toMutableMap())
        viewModel?.insert(lotteryNumberCount)
    }

    private fun onSetEuroNumbers() {
        val maxNumbers = 2
        val minVolume = 1
        val maxVolume = 10
        val eurojackpotNumbers = DrawnLotteryNumbersExtensions
            .draw(maxNumbers, minVolume, maxVolume)
        val toIntArray = eurojackpotNumbers.toIntArray()

        txtFirstEuroNumber1Of10.setText(toIntArray[0].toString())
        txtSecondEuroNumber1Of10.setText(toIntArray[1].toString())
    }

}
