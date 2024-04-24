package com.example.tripy.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.tripy.R

fun showCurrencyDialog(context: Context,onCurrencySelected: (String) -> Unit) {
    val currencies = context.resources.getStringArray(R.array.currency_array)
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Select Currency")
        .setItems(currencies) { dialog, which ->
            val selectedCurrency = currencies[which]
            onCurrencySelected(selectedCurrency)
        }
        .show()
}

