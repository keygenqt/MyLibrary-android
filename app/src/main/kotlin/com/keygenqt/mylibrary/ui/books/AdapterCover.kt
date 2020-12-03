/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.ui.books

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.keygenqt.mylibrary.base.ListAdapter
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.extensions.showWithPadding
import kotlinx.android.synthetic.main.item_select_list.view.description
import kotlinx.android.synthetic.main.item_select_list.view.itemBlock
import kotlinx.android.synthetic.main.item_select_list.view.radioButton
import kotlinx.android.synthetic.main.item_select_list.view.title

class AdapterCover(
    @LayoutRes layout: Int,
    var selectCover: String?,
    private val fb: FloatingActionButton,
    private val recyclerView: RecyclerView
) : ListAdapter<String>(layout) {

    override fun onBindViewHolder(holder: View, model: Any) {
        if (model is String) {
            holder.apply {
                title.text = model
                description.visibility = View.GONE
                itemBlock.setOnClickListener {
                    selectCover?.let {
                        items.forEachIndexed { index, any ->
                            if (any is String && any == selectCover) {
                                notifyItemChanged(index)
                                return@forEachIndexed
                            }
                        }
                    }
                    selectCover = model
                    radioButton.isChecked = true
                    fb.showWithPadding(recyclerView)
                }
                radioButton.isChecked = model == selectCover
            }
        }
    }

    fun getSelectItem(): String? {
        items.forEach {
            if (it is String && it == selectCover) {
                return it
            }
        }
        return null
    }
}