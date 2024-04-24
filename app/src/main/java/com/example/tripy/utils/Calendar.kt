package com.example.tripy.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


fun showDatePicker(
    activity: Activity,
    fragmentManager: FragmentManager,
    onDateSelected: (startDate: String, endDate: String) -> Unit
) {

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(activity).apply {
        setTitle("Select Start Date")
        setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val startDate = formatDate(year, monthOfYear, dayOfMonth)
            calendar.set(year, monthOfYear, dayOfMonth)
            showEndDatePicker(activity, fragmentManager, calendar, onDateSelected, startDate)
        }
    }

    startDatePickerDialog.datePicker.minDate =
        System.currentTimeMillis() - 1000
    startDatePickerDialog.show()
}

private fun showEndDatePicker(
    activity: Activity,
    fragmentManager: FragmentManager,
    startDateCalendar: Calendar,
    onDateSelected: (startDate: String, endDate: String) -> Unit,
    startDate: String
) {

    val endDatePickerDialog = DatePickerDialog(activity).apply {
        setTitle("Select End Date")
        setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val endDate = formatDate(year, monthOfYear, dayOfMonth)
            onDateSelected(startDate, endDate)
        }
    }

    endDatePickerDialog.datePicker.minDate =
        startDateCalendar.timeInMillis
    endDatePickerDialog.show()
}


private fun formatDate(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}


fun showDateDialog(
    activity: Activity,
    fragmentManager: FragmentManager,
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (startDate: String) -> Unit
) {

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(activity).apply {
        setTitle("Select Start Date")
        setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val startDate = formatDate(year, monthOfYear, dayOfMonth)
            calendar.set(year, monthOfYear, dayOfMonth)
            onDateSelected(startDate)
        }
    }

    minDate?.let { startDatePickerDialog.datePicker.minDate = it }
    maxDate?.let { startDatePickerDialog.datePicker.maxDate = it }

    startDatePickerDialog.show()
}

fun parseDate(dateString: String): Long {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val date = format.parse(dateString)
    return date?.time ?: 0L
}



