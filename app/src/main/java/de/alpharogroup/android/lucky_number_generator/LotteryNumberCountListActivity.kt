package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountAdapter
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountDatabase
import de.alpharogroup.android.lucky_number_generator.data.MainViewModel
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory

import kotlinx.android.synthetic.main.content_lottery_number_count_list.*
import java.util.*

class LotteryNumberCountListActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null

    private var personAdapter: LotteryNumberCountAdapter?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        var dataBaseInstance =
            LotteryNumberCountDatabase.getDatabase(
                this
            )
        initViews()
        setListeners()
        observerViewModel()
    }

    private fun initViews() {
        rvSavedRecords.layoutManager= LinearLayoutManager(this)
        personAdapter =
            LotteryNumberCountAdapter(this) {
                it.let {
                    viewModel?.delete(it)
                }
            }
        rvSavedRecords.adapter = personAdapter
    }

    private fun observerViewModel() {
        viewModel?.lotteryNumberCountList?.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                handleData(it)
            } else {
                handleZeroCase()
            }
        })
    }

    private fun handleData(data: List<LotteryNumberCount>) {
        rvSavedRecords.visibility = View.VISIBLE
        personAdapter?.setData(data)
    }

    private fun handleZeroCase() {
        rvSavedRecords.visibility = View.GONE
        Toast.makeText(this,"No Records Found",Toast.LENGTH_LONG).show()
    }

    private fun setListeners() {
        saveBtn.setOnClickListener {
            saveData()
        }

        retrieveBtn.setOnClickListener {
            viewModel?.getData()
        }
    }

    private fun saveData() {
        var name = edtName.text.trim().toString()
        var age = edtAge.text.trim().toString()
        edtName.setText("")
        edtAge.setText("")
        if (name.isNullOrBlank() || age.isNullOrBlank()) {
            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_LONG).show()
        } else {

            var lotteryNumberCount =
                LotteryNumberCount(
                    id = UUID.randomUUID(),
                    lotteryGameType = "6of49",
                    numberCounterMap = MapFactory.newCounterMap(
                        ListFactory.newRangeList(
                            1,
                            49
                        )
                    )
                )
            viewModel?.saveDataIntoDb(lotteryNumberCount)

        }
    }

}
