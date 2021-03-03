package de.alpharogroup.android.lucky_number_generator

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class EditTextDatePicker @JvmOverloads constructor(
    private val editText: EditText,
    ctx: Context,
    dateFormat: String,
    setToday: Boolean = true
) : View.OnClickListener, OnDateSetListener {
    private val myCalendar: Calendar
    var ctx: Context
    var dateFormat: String
    var simpleDateFormat: SimpleDateFormat
    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        myCalendar[Calendar.YEAR] = year
        myCalendar[Calendar.MONTH] = monthOfYear
        myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        setEditText()
    }

    protected fun setEditText() {
        editText.setText(simpleDateFormat.format(myCalendar.time))
    }

    override fun onClick(v: View) {
        DatePickerDialog(
            ctx, this, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    init {
        editText.setOnClickListener(this)
        myCalendar = Calendar.getInstance()
        this.ctx = ctx
        this.dateFormat = dateFormat
        simpleDateFormat = SimpleDateFormat(this.dateFormat)
        if (setToday) {
            setEditText()
        }
    }
}