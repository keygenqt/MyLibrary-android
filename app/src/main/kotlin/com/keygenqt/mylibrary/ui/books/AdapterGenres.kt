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
import com.keygenqt.mylibrary.data.models.ModelListGenre
import com.keygenqt.mylibrary.extensions.showWithPadding
import com.keygenqt.mylibrary.hal.Link
import kotlinx.android.synthetic.main.item_select_list.view.description
import kotlinx.android.synthetic.main.item_select_list.view.itemBlock
import kotlinx.android.synthetic.main.item_select_list.view.radioButton
import kotlinx.android.synthetic.main.item_select_list.view.title

class AdapterGenres(
    @LayoutRes layout: Int,
    var selectGenreId: Long?,
    private val fb: FloatingActionButton,
    private val recyclerView: RecyclerView,
    nextPage: (Link) -> Unit
) : ListAdapter<ModelListGenre>(layout, nextPage) {

    override fun onBindViewHolder(holder: View, model: Any) {
        if (model is ModelListGenre) {
            holder.apply {
                title.text = model.title
                description.text = model.description
                itemBlock.setOnClickListener {
                    selectGenreId?.let {
                        items.forEachIndexed { index, any ->
                            if (any is ModelListGenre && any.id == selectGenreId) {
                                notifyItemChanged(index)
                                return@forEachIndexed
                            }
                        }
                    }
                    selectGenreId = model.id
                    radioButton.isChecked = true
                    fb.showWithPadding(recyclerView)
                }
                radioButton.isChecked = model.id == selectGenreId
            }
        }
    }

    fun getSelectItem(): ModelListGenre? {
        items.forEach {
            if (it is ModelListGenre && it.id == selectGenreId) {
                return it
            }
        }
        return null
    }
}