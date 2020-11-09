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

package com.keygenqt.mylibrary.ui.local

import android.view.*
import androidx.annotation.*
import com.keygenqt.mylibrary.interfaces.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.data.models.ModelBook
import kotlinx.android.synthetic.main.item_book_list.view.*

class AdapterLocal(@LayoutRes layout: Int, viewModel: ViewModelPage) : BaseAdapter(layout, viewModel) {
    override fun onBindViewHolder(holder: View, model: Any) {
        if (model is ModelBook) {
            holder.apply {
                title.text = model.title
                subtitle.text = model.description
            }
        }
    }
}