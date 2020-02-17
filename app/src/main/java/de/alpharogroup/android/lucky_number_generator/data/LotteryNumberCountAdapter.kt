package de.alpharogroup.android.lucky_number_generator.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.alpharogroup.android.lucky_number_generator.R

import kotlinx.android.synthetic.main.content_lottery_number_count_list.view.*

class LotteryNumberCountAdapter internal  constructor(context: Context, private val onDeleteClick: (LotteryNumberCount) -> Unit
) : RecyclerView.Adapter<LotteryNumberCountAdapter.DataViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dataList = mutableListOf<LotteryNumberCount>()

    fun setData(list: List<LotteryNumberCount>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            inflater
                .inflate(R.layout.activity_lottery_number_count_list, parent, false)
        ) {
            onDeleteClick.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.setData(dataList[position])
    }

    inner class DataViewHolder(
        itemView: View, val onDeleteClick: (LotteryNumberCount) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun setData(personData: LotteryNumberCount) {
            itemView.apply {
                saveBtn.setOnClickListener {
                    onDeleteClick(personData)
                }
            }

        }

    }
}