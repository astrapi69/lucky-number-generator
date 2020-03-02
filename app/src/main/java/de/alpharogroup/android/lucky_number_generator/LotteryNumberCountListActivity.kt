package de.alpharogroup.android.lucky_number_generator

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
        adapter = LotteryNumberCountAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(LotteryNumberCountViewModel::class.java)

        disposable =
            EventBus.subscribe<ButtonEvent>()
                // receive the event on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    toggleMergeThemButton()
                })

        observerViewModel()
    }

    private fun toggleMergeThemButton() {
        if (adapter.selected.isEmpty()){
            btnMergeThem.setEnabled(false)
            btnDeselectAll.setEnabled(false)
            btnDeleteSelected.setEnabled(false)
        }
        else{
            if(2 <= adapter.selected.size ){
                btnMergeThem.setEnabled(true)
            }else{
                btnMergeThem.setEnabled(false)
            }
            btnDeselectAll.setEnabled(true)
            btnDeleteSelected.setEnabled(true)
        }
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

    fun onSelectAll(view: View) {
        adapter?.selectAll()
    }

    fun onDeselectAll(view: View){
        adapter?.clearSelected()
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

}

