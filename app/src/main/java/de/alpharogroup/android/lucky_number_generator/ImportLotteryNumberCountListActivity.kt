package de.alpharogroup.android.lucky_number_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel
import de.alpharogroup.android.lucky_number_generator.data.converter.ListLotteryNumberCountConverter
import de.alpharogroup.android.lucky_number_generator.event.ButtonEvent
import de.alpharogroup.android.lucky_number_generator.event.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ImportLotteryNumberCountListActivity : AppCompatActivity() {

    private var viewModel: LotteryNumberCountViewModel? = null
    lateinit var txtImportJson: EditText
    lateinit var btnImportJson: Button
    private var disposable: Disposable? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_lottery_number_count_list)
        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)
        txtImportJson = findViewById(R.id.txtImportJson)
        btnImportJson = findViewById(R.id.btnImportJson)

    }


    private fun observeButtonStates() {
        disposable =
            EventBus.subscribe<ButtonEvent>()
                // receive the event on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toggleButtons()
                }
    }

    private fun toggleButtons() {
        txtImportJson.addTextChangedListener ( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.length?.let {
                    txtImportJson.isEnabled = 0<it
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun onImportJson(view: View){
        val jsonString = txtImportJson.text.toString()
        val fromString: List<LotteryNumberCount>
        if (validateJsonString(jsonString)){
            val listLotteryNumberCountConverter =
                ListLotteryNumberCountConverter()
            fromString = listLotteryNumberCountConverter.fromString(jsonString)
            fromString.forEach {
                viewModel?.insert(it)
            }
        } else {
            Toast.makeText(this,"Input is not a valid json string", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateJsonString(jsonString: String): Boolean{
        return try {
            JsonParser.parseString(jsonString)
            true
        } catch (jse: JsonSyntaxException){
            false
        }
    }
}
