package de.alpharogroup.android.lucky_number_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import de.alpharogroup.lottery.drawing.DrawnLotteryNumbersExtensions
import de.alpharogroup.random.number.RandomPrimitivesExtensions

class Lottery6Of49GenerationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottery6_of49_generation)

        val first = findViewById<EditText>(R.id.firstNumber1Of49)
        val second = findViewById<EditText>(R.id.secondNumber1Of49)
        val third = findViewById<EditText>(R.id.thirdNumber1Of49)
        val fourth = findViewById<EditText>(R.id.fourthNumber1Of49)
        val fifth = findViewById<EditText>(R.id.fifthNumber1Of49)
        val sixth = findViewById<EditText>(R.id.sixthNumber1Of49)
        val superNumber1Of49 = findViewById<EditText>(R.id.superNumber1Of49)
        val superSixNumber1Of49 = findViewById<EditText>(R.id.superSixNumber1Of49)

        first.setEnabled(false)
        second.setEnabled(false)
        third.setEnabled(false)
        fourth.setEnabled(false)
        fifth.setEnabled(false)
        sixth.setEnabled(false)
        superNumber1Of49.setEnabled(false)
        superSixNumber1Of49.setEnabled(false)
    }

    fun onGenerate1Of49(view: View) {
        val button = findViewById<Button>(R.id.btn_generate_my_lucky_numbers)
        button.setOnClickListener{
            onDraw()
        }
    }

    fun onDraw() {
        val max = 6
        val volume = 49
        val lotteryNumbers = DrawnLotteryNumbersExtensions.draw(max, volume)
        val superNumber = DrawnLotteryNumbersExtensions.drawSuperNumber(lotteryNumbers, volume)
        val superSixNumber = RandomPrimitivesExtensions.randomIntBetween(1, 10)

        val first = findViewById<EditText>(R.id.firstNumber1Of49)
        val second = findViewById<EditText>(R.id.secondNumber1Of49)
        val third = findViewById<EditText>(R.id.thirdNumber1Of49)
        val fourth = findViewById<EditText>(R.id.fourthNumber1Of49)
        val fifth = findViewById<EditText>(R.id.fifthNumber1Of49)
        val sixth = findViewById<EditText>(R.id.sixthNumber1Of49)
        val superNumber1Of49 = findViewById<EditText>(R.id.superNumber1Of49)
        val superSixNumber1Of49 = findViewById<EditText>(R.id.superSixNumber1Of49)

        val toIntArray = lotteryNumbers.toIntArray()
        first.setText(toIntArray[0].toString())
        second.setText(toIntArray[1].toString())
        third.setText(toIntArray[2].toString())
        fourth.setText(toIntArray[3].toString())
        fifth.setText(toIntArray[4].toString())
        sixth.setText(toIntArray[5].toString())
        superNumber1Of49.setText(superNumber.toString())
        superSixNumber1Of49.setText(superSixNumber.toString())

    }
}
