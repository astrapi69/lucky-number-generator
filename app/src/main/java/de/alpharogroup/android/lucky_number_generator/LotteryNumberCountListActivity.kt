package de.alpharogroup.android.lucky_number_generator

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountAdapter
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCountViewModel

class LotteryNumberCountListActivity : AppCompatActivity() {

    private var viewModel: LotteryNumberCountViewModel? = null

    lateinit var recyclerView:  RecyclerView

    lateinit var adapter: LotteryNumberCountAdapter

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_lottery_number_count_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = LotteryNumberCountAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)

        observerViewModel()
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
        recyclerView.visibility = View.VISIBLE
        adapter?.setData(data)
    }

    private fun handleZeroCase() {
        recyclerView.visibility = View.GONE
        Toast.makeText(this,"No Records Found",Toast.LENGTH_LONG).show()
    }

}
