package de.alpharogroup.android.lucky_number_generator.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.alpharogroup.android.lucky_number_generator.R

import kotlinx.android.synthetic.main.content_lottery_number_count_list.view.*

class LotteryNumberCountAdapter internal  constructor(
    context: Context
) : RecyclerView.Adapter<LotteryNumberCountAdapter.DataViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dataList = listOf<LotteryNumberCount>()

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lotteryNumberCountItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val current = dataList[position]
        val converter = IntegerIntegerMapConverter()
        holder.lotteryNumberCountItemView.text = converter.fromIntegerMap(current.numberCounterMap)
    }

    internal fun setData(data: List<LotteryNumberCount>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}