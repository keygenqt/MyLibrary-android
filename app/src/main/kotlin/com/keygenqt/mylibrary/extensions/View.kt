package com.keygenqt.mylibrary.extensions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.toggleKeyboard() {
    Handler(Looper.getMainLooper()).post {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (imm is InputMethodManager) {
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}