package com.keygenqt.mylibrary.ui.local

import android.view.*
import androidx.annotation.*
import com.keygenqt.mylibrary.interfaces.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.data.*
import kotlinx.android.synthetic.main.item_book_list.view.*

class AdapterLocal(@LayoutRes layout: Int, viewModel: ViewModelPage) : BaseAdapter(layout, viewModel) {
    override fun onBindViewHolder(holder: View, model: Any) {
        if (model is ModelBook) {
            holder.apply {
                title.text = model.name
                subtitle.text = model.user.email
            }
        }
    }
}