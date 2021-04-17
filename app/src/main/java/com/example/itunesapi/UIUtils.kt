package com.example.itunesapi

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager

fun dp(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

fun closeKeyboard(context: Context, view: View?) {
    if (view != null) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}