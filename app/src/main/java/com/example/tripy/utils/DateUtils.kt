package com.example.tripy.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun getDaysBetweenDates(startDateStr: String, endDateStr: String): Long {
        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)
        return ChronoUnit.DAYS.between(startDate, endDate) + 1
    }



    fun getNextDayAndDate(
        currentDate: String,
        daysToAdd: Int
    ): Triple<String, Int,String> {
        val sdfInput = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val sdfDayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val sdfOutput = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        val calendar = Calendar.getInstance()
        calendar.time = sdfInput.parse(currentDate) ?: Date()

        calendar.add(Calendar.DATE, daysToAdd)

        val nextDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val nextDayOfWeek = sdfDayOfWeek.format(calendar.time)
        val nextDate = sdfOutput.format(calendar.time)


        return Triple(nextDayOfWeek, nextDayOfMonth,nextDate)
    }
}
