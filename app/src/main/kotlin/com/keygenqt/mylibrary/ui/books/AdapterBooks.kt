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
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.ListAdapterSearch
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.interfaces.ViewModelPage
import kotlinx.android.synthetic.main.item_book_list.view.subtitle
import kotlinx.android.synthetic.main.item_book_list.view.title

class AdapterBooks(@LayoutRes layout: Int, viewModel: ViewModelPage, search: (String, Link) -> Unit)
    : ListAdapterSearch<ModelBook>(layout, viewModel, search) {

    companion object {
        const val SEARCH_SELF = "self"
        const val SEARCH_FIND_ALL_BY_USER_ID = "findAllByUserId"
        const val SEARCH_FIND_ALL_BY_SALE = "findAllBySale"
    }

    override fun getStrings(): LinkedHashMap<String, Int> {
        return linkedMapOf(
            SEARCH_SELF to R.string.search_books_self,
            SEARCH_FIND_ALL_BY_USER_ID to R.string.search_books_findAllByUserId,
            SEARCH_FIND_ALL_BY_SALE to R.string.search_books_findAllBySale
        )
    }

    override fun onBindViewHolder(holder: View, model: ModelBook) {
        holder.apply {
            title.text = model.title
            subtitle.text = model.description
        }
    }
}