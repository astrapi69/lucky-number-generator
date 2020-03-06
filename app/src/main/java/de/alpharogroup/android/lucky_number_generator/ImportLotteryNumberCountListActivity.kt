package de.alpharogroup.android.lucky_number_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import de.alpharogroup.android.lucky_number_generator.data.ButtonEvent
import de.alpharogroup.android.lucky_number_generator.data.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ImportLotteryNumberCountListActivity : AppCompatActivity() {

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

    }

    fun onImportJson(view: View){
        txtImportJson
    }
}
