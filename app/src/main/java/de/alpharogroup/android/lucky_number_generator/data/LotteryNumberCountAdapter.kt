package de.alpharogroup.android.lucky_number_generator.data

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import de.alpharogroup.android.lucky_number_generator.R
import de.alpharogroup.android.lucky_number_generator.data.converter.IntegerIntegerMapConverter
import de.alpharogroup.android.lucky_number_generator.event.ButtonEvent
import de.alpharogroup.android.lucky_number_generator.event.EventBus
import de.alpharogroup.collections.map.MapExtensions
import de.alpharogroup.collections.map.MapFactory
import java.util.*

class LotteryNumberCountAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<LotteryNumberCountAdapter.DataViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dataList = listOf<LotteryNumberCount>()
    var selected: MutableList<LotteryNumberCount> = mutableListOf<LotteryNumberCount>()
    var itemStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var lotteryNumberCountItemView: CheckedTextView =
            itemView.findViewById<View>(R.id.checkedTextView) as CheckedTextView

        fun bind(position: Int) { // use the sparse boolean array to check
            val current = dataList[position]
            val converter =
                IntegerIntegerMapConverter()
            val labelview =
                "Drawn numbers:\n" + converter.fromIntegerMap(current.numberCounterMap) + "\nType:\n" + current.lotteryGameType
            lotteryNumberCountItemView.isChecked = itemStateArray[position, false]
            lotteryNumberCountItemView.text = labelview
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            EventBus.post(
                ButtonEvent(
                    "item clicked"
                )
            )
            if (!itemStateArray[adapterPosition, false]) {
                lotteryNumberCountItemView.isChecked = true
                itemStateArray.put(adapterPosition, true)
                selected.add(dataList[adapterPosition])
            } else {
                lotteryNumberCountItemView.isChecked = false
                itemStateArray.put(adapterPosition, false)
                selected.remove(dataList[adapterPosition])
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    internal fun setData(data: List<LotteryNumberCount>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun clearSelected() {
        selected.clear()
        for (i in 0 until itemStateArray.size()) {
            val key = itemStateArray.keyAt(i)
            itemStateArray.put(key, false)
        }
        EventBus.post(
            ButtonEvent(
                "clearSelected"
            )
        )
        notifyDataSetChanged()
    }

    fun selectAll() {
        selected.clear()
        selected.addAll(dataList)
        for (i in 0 until dataList.size) {
            itemStateArray.put(i, true)
        }
        EventBus.post(
            ButtonEvent(
                "selectAll"
            )
        )
        notifyDataSetChanged()
    }

    fun mergeSelected(): LotteryNumberCount? {
        var numberCounterMap: LotteryNumberCount? = null
        if (!selected.isEmpty()) {
            var map: MutableMap<Int, Int> = MapFactory.newLinkedHashMap()
            selected.forEach {
                map = MapExtensions.mergeAndSummarize(map, it.numberCounterMap, true)
            }
            map = MapExtensions.sortByValue(map, true);
            numberCounterMap = LotteryNumberCount(
                id = UUID.randomUUID(), lotteryGameType = "merged", numberCounterMap = map
            )
        }
        return numberCounterMap
    }

    fun deleteSelected(): List<LotteryNumberCount>? {
        if (!selected.isEmpty()) {
            val toMutableList = dataList.toMutableList()
            toMutableList.removeAll(selected)
            setData(toMutableList)
            notifyDataSetChanged()
        }
        return selected
    }
}
