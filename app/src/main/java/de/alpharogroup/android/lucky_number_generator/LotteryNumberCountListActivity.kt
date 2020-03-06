package de.alpharogroup.android.lucky_number_generator

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.alpharogroup.android.lucky_number_generator.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class LotteryNumberCountListActivity : AppCompatActivity() {

    private var viewModel: LotteryNumberCountViewModel? = null

    lateinit var recyclerView:  RecyclerView

    lateinit var btnMergeThem: Button
    lateinit var btnDeselectAll: Button
    lateinit var btnSelectAll: Button
    lateinit var btnDeleteSelected: Button
    lateinit var btnShareSelected: Button

    lateinit var adapter: LotteryNumberCountAdapter

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
        setContentView(R.layout.content_lottery_number_count_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        btnMergeThem = findViewById<Button>(R.id.btnMergeThem)
        btnDeselectAll = findViewById<Button>(R.id.btnDeselectAll)
        btnSelectAll = findViewById(R.id.btnSelectAll)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)
        btnShareSelected = findViewById(R.id.btnShareSelected)
        adapter = LotteryNumberCountAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)

        observeButtonStates()

        observeViewModel()
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
        if (adapter.selected.isEmpty()){
            btnMergeThem.setEnabled(false)
            btnDeselectAll.setEnabled(false)
            btnDeleteSelected.setEnabled(false)
            btnShareSelected.setEnabled(false)
        }
        else{
            if(2 <= adapter.selected.size ){
                btnMergeThem.setEnabled(true)
            }else{
                btnMergeThem.setEnabled(false)
            }
            btnDeselectAll.setEnabled(true)
            btnDeleteSelected.setEnabled(true)
            btnShareSelected.setEnabled(true)
        }
    }

    private fun observeViewModel() {
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

    fun onSelectAll(view: View) {
        adapter?.selectAll()
    }

    fun onDeselectAll(view: View){
        adapter?.clearSelected()
    }

    fun onShareSelected(view: View){
        adapter?.selected
        val listLotteryNumberCountConverter = ListLotteryNumberCountConverter()
        val json = listLotteryNumberCountConverter.fromList(adapter?.selected)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lucky lottery numbers collection")
        shareIntent.putExtra(Intent.EXTRA_TEXT, json)
        shareIntent.type = "application/json"
        startActivity(shareIntent)
    }

    fun onDeleteSelected(view: View){
        val deleteSelected = adapter?.deleteSelected()
        deleteSelected?.forEach {
            viewModel?.delete(it)
        }
        adapter?.selected.clear()
        adapter?.clearSelected()
    }

    fun onMergeSelected(view: View){
        val mergeSelected = adapter?.mergeSelected()
        mergeSelected?.let {
            viewModel?.insert(it)
        }
    }

    fun onImport(view: View){
        val intent = Intent(this, ImportLotteryNumberCountListActivity::class.java)
        startActivity(intent)
    }

}

