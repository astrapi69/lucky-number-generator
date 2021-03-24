package de.alpharogroup.android.lucky_number_generator

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*


class EditTextDateTimePicker @JvmOverloads constructor(
    private val editText: EditText,
    ctx: Context,
    dateFormat: String,
    setToday: Boolean = true
) : View.OnClickListener, OnDateSetListener {
    private val myCalendar: Calendar
    private var ctx: Context
    var dateFormat: String
    private var simpleDateFormat: SimpleDateFormat
    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        myCalendar[Calendar.YEAR] = year
        myCalendar[Calendar.MONTH] = monthOfYear
        myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        setEditText()
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

    protected fun setEditText() {
        editText.setText(simpleDateFormat.format(myCalendar.time))
    }

    override fun onClick(v: View) {
        val datePickerDialog = DatePickerDialog(
            ctx,
            { datePicker, year, monthOfYear, dayOfMonth ->
                myCalendar.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    ctx,
                    { view, hourOfDay, minute ->
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        myCalendar.set(Calendar.MINUTE, minute)
                        setEditText()
                    }, myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true
                ).show()
            }, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DATE]
        )
        datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
        datePickerDialog.show()
    }
}