package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountDao
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountDatabase
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawnLotteryNumbersExtensions
import de.alpharogroup.random.number.RandomPrimitivesExtensions
import java.util.*

class Lottery6Of49GenerationActivity : AppCompatActivity() {

    lateinit var txtFirstNumber1Of49: EditText
    lateinit var txtSecondNumber1Of49: EditText
    lateinit var txtThirdNumber1Of49: EditText
    lateinit var txtFourthNumber1Of49: EditText
    lateinit var txtFifthNumber1Of49: EditText
    lateinit var txtSixthNumber1Of49: EditText
    lateinit var txtSuperNumber1Of49: EditText
    lateinit var txtSuperSixNumber1Of49: EditText
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
        setContentView(R.layout.activity_lottery6_of49_generation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        onInitialize()
        disableEditTexts()
    }

    fun onInitialize() {
        txtFirstNumber1Of49 = findViewById<EditText>(R.id.firstNumber1Of49)
        txtSecondNumber1Of49 = findViewById<EditText>(R.id.secondNumber1Of49)
        txtThirdNumber1Of49 = findViewById<EditText>(R.id.thirdNumber1Of49)
        txtFourthNumber1Of49 = findViewById<EditText>(R.id.fourthNumber1Of49)
        txtFifthNumber1Of49 = findViewById<EditText>(R.id.fifthNumber1Of49)
        txtSixthNumber1Of49 = findViewById<EditText>(R.id.sixthNumber1Of49)
        txtSuperNumber1Of49 = findViewById<EditText>(R.id.superNumber1Of49)
        txtSuperSixNumber1Of49 = findViewById<EditText>(R.id.superSixNumber1Of49)
    }


    private fun disableEditTexts() {
        txtFirstNumber1Of49.setEnabled(false)
        txtSecondNumber1Of49.setEnabled(false)
        txtThirdNumber1Of49.setEnabled(false)
        txtFourthNumber1Of49.setEnabled(false)
        txtFifthNumber1Of49.setEnabled(false)
        txtSixthNumber1Of49.setEnabled(false)
        txtSuperNumber1Of49.setEnabled(false)
        txtSuperSixNumber1Of49.setEnabled(false)
    }

    fun onGenerate1Of49(view: View) {
        onDraw()
    }

    fun onDraw() {
        val max = 6
        val volume = 49
        val lotteryNumbers = DrawnLotteryNumbersExtensions.draw(max, volume)
        val superNumber = DrawnLotteryNumbersExtensions.drawSuperNumber(lotteryNumbers, volume)
        val superSixNumber = RandomPrimitivesExtensions.randomIntBetween(1, 10)

        val toIntArray = lotteryNumbers.toIntArray()
        txtFirstNumber1Of49.setText(toIntArray[0].toString())
        txtSecondNumber1Of49.setText(toIntArray[1].toString())
        txtThirdNumber1Of49.setText(toIntArray[2].toString())
        txtFourthNumber1Of49.setText(toIntArray[3].toString())
        txtFifthNumber1Of49.setText(toIntArray[4].toString())
        txtSixthNumber1Of49.setText(toIntArray[5].toString())
        txtSuperNumber1Of49.setText(superNumber.toString())
        txtSuperSixNumber1Of49.setText(superSixNumber.toString())
        var numberCounterMap = MapFactory.newCounterMap(
            ListFactory.newRangeList(
                1,
                49
            )
        )
        val mergeAndSummarize = MapExtensions.mergeAndSummarize(numberCounterMap, lotteryNumbers)
        var lotteryNumberCount = LotteryNumberCount(
            id = UUID.randomUUID(), lotteryGameType = "6of49",
            numberCounterMap = mergeAndSummarize)
        viewModel?.insert(lotteryNumberCount)

    }

}
