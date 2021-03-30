package de.alpharogroup.android.lucky_number_generator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.android.lucky_number_generator.extensions.Constants
import de.alpharogroup.android.lucky_number_generator.extensions.Extensions
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import de.alpharogroup.lottery.drawing.DrawMerger
import de.alpharogroup.lottery.drawing.DrawMultiMapLotteryNumbersFactory
import de.alpharogroup.lottery.drawings.DrawModelBean
import de.alpharogroup.math.MathExtensions
import de.alpharogroup.string.StringExtensions
import io.github.astrapi69.android.datetime.EditTextDateTimePicker
import java.security.SecureRandom
import java.util.*

class CustomGenerationActivity : AppCompatActivity() {

    companion object {
        const val LUCKY_NUMBERS = "de.alpharogroup.android.lucky_number_generator.LUCKY_NUMBERS"
    }

    private lateinit var txtNumbersToDraw: EditText
    private lateinit var txtMinVolume: EditText
    private lateinit var txtMaxVolume: EditText
    private lateinit var txtIterations: EditText
    private lateinit var txtDrawDate: EditText
    private lateinit var btn_generate_my_lucky_cg_numbers: Button
    private lateinit var handler: Handler
    private var viewModel: LotteryNumberCountViewModel? = null
    private lateinit var progressBar: ProgressBar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        btn_generate_my_lucky_cg_numbers.isEnabled = true
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
        txtDrawDate = findViewById(R.id.drawDateCustom)
        btn_generate_my_lucky_cg_numbers = findViewById(R.id.btn_generate_my_lucky_cg_numbers)
        txtDrawDate.inputType = InputType.TYPE_NULL
        EditTextDateTimePicker(
            txtDrawDate,
            this,
            Constants.DEFAULT_DATE_TIME_FORMAT
        )
        progressBar = findViewById(R.id.progressBar)
        handler = Handler()

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
        val between1to5000Message = resources.getString(R.string.validation_is_between_1_5000)

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
                s.isNotEmpty()
            }) {
            return
        }
        if (txtIterations.validate(between1to5000Message) { s ->
                s.isNumberBetween(1, 5000)
            }) {
            return
        }
        onDraw()
    }

    override fun onStop() {
        super.onStop()
        btn_generate_my_lucky_cg_numbers.isEnabled = true
        progressBar.visibility = View.GONE
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
        val currentDrawDateValue: String = txtDrawDate.text.toString()
        val currentDrawDate: Date =
            Extensions.parseToDate(currentDrawDateValue, Constants.DEFAULT_DATE_TIME_FORMAT)
        val secureRandom: SecureRandom = Extensions.newSecureRandom(currentDrawDate)
        var drawModel: DrawModelBean
        var mergeAndSummarize: MutableMap<Int, Int> = MapFactory.newNumberCounterMap(minVolume, maxVolume)
        var drawFromMultiMap: MutableSet<Int>

        if(500 < drawCount) {
            btn_generate_my_lucky_cg_numbers.isEnabled = false
            progressBar.visibility = View.VISIBLE
            drawModel = DrawModelBean.builder()
                .maxNumbers(maxNumbers)
                .minVolume(minVolume)
                .maxVolume(maxVolume)
                .drawCount(500)
                .secureRandom(secureRandom)
                .build()
            val one = 1.0
            val onePercent: Double = (drawCount / 500).toDouble()
            var counter: Double = one.div(onePercent)
            val contextThis: Context = this

            Thread(object : Runnable {
                var i = 0
                var progressStatus = 0.0
                override fun run() {
                    while (progressStatus < 100) {
                        progressStatus += doWork()
                        // Update the progress bar
                        handler.post {
                            progressBar.setProgress(progressStatus.toInt())
                            i++
                        }
                    }
                    var sortByValue = mergeAndSummarize.toList().sortedBy { (_, value) -> -value }.toMap()
                    drawFromMultiMap = sortByValue.keys.take(maxNumbers).toSortedSet()

                    val toSortedMap = mergeAndSummarize.toSortedMap()
                    val lotteryNumberCount = LotteryNumberCount(
                        id = UUID.randomUUID(), lotteryGameType = "custom",
                        numberCounterMap = toSortedMap
                    )

                    viewModel?.insert(lotteryNumberCount)
                    val drawFromMultiMapString = drawFromMultiMap.toString()
                    val intent = Intent(contextThis, GenerationResultActivity::class.java).apply {
                        putExtra(LUCKY_NUMBERS, drawFromMultiMapString.removeFirstAndLastCharacter())
                    }
                    progressStatus = 0.0
                    progressBar.setProgress(progressStatus.toInt())
                    startActivity(intent)
                }

                private fun doWork(): Double {
                    mergeAndSummarize = DrawMerger.mergeDrawings(drawModel, mergeAndSummarize)
                    return counter
                }
            }).start()

        } else {
            drawModel = DrawModelBean.builder()
                .maxNumbers(maxNumbers)
                .minVolume(minVolume)
                .maxVolume(maxVolume)
                .drawCount(drawCount)
                .secureRandom(secureRandom)
                .build()
            drawFromMultiMap =
                DrawMultiMapLotteryNumbersFactory.drawFromMultiMap(drawModel)

            mergeAndSummarize = MapExtensions.mergeAndSummarize(
                MapFactory.newCounterMap(drawFromMultiMap),
                drawFromMultiMap
            )
            var sortByValue = mergeAndSummarize.toList().sortedBy { (_, value) -> -value }.toMap()
            drawFromMultiMap = sortByValue.keys.take(maxNumbers).toSortedSet()

            val toSortedMap = mergeAndSummarize.toSortedMap()
            val lotteryNumberCount = LotteryNumberCount(
                id = UUID.randomUUID(), lotteryGameType = "custom",
                numberCounterMap = toSortedMap
            )

            viewModel?.insert(lotteryNumberCount)
            val drawFromMultiMapString = drawFromMultiMap.toString()
            val intent = Intent(this, GenerationResultActivity::class.java).apply {
                putExtra(LUCKY_NUMBERS, drawFromMultiMapString.removeFirstAndLastCharacter())
            }
            startActivity(intent)
        }

    }

    private fun String.removeFirstAndLastCharacter(): String {
        return this.substring(1, this.length - 1)
    }

}
