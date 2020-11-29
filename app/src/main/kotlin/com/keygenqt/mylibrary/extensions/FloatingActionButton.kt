package com.keygenqt.mylibrary.extensions

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.keygenqt.mylibrary.R

fun FloatingActionButton.showWithPadding(rv: RecyclerView) {
    this.show()
    val paddingTop = context.resources.getDimension(R.dimen.padding_list).toInt()
    val paddingBottom = context.resources.getDimension(R.dimen.padding_list_floating).toInt()
    rv.setPadding(0, paddingTop, 0, paddingBottom)
}

fun FloatingActionButton.hideWithPadding(rv: RecyclerView) {
    this.hide()
    val padding = context.resources.getDimension(R.dimen.padding_list).toInt()
    rv.setPadding(0, padding, 0, padding)
}