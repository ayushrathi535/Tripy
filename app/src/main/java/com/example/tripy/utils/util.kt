package com.example.tripy.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hidekeyboard(context: Context, view: View?) {
    try {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}