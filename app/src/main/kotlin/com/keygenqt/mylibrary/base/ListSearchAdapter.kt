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

package com.keygenqt.mylibrary.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView.*
import com.google.android.material.chip.Chip
import com.keygenqt.mylibrary.R.*
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.hal.API_KEY_SEARCH
import com.keygenqt.mylibrary.hal.Link
import kotlinx.android.synthetic.main.item_search.view.chipGroup

class AdapterHolderSearch(
    group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(layout.item_search, group, false)
) : ViewHolder(view)

abstract class ListSearchAdapter<T>(@LayoutRes layout: Int, private val nextPageSearch: ((String, Link) -> Unit)? = null)
    : ListAdapter<T>(layout, { link -> nextPageSearch?.invoke(SEARCH_PAGE, link) }) {

    companion object {
        const val SEARCH_PAGE = "page"
        const val SEARCH_SELF = "self"
    }

    var view: View? = null

    abstract fun getStrings(): LinkedHashMap<String, Int>

    private fun bindView(view: View, model: ModelSearch) {
        view.apply {
            this@ListSearchAdapter.view = this
            chipGroup.setOnCheckedChangeListener(null)
            chipGroup.removeAllViews()
            getStrings().forEach { string ->
                model.links[string.key]?.let {
                    setChip(string.key, view, context.getString(string.value), it)
                }
            }
            chipGroup.setOnCheckedChangeListener { _, viewId ->
                val checkedKey = chipGroup.findViewById<Chip>(viewId).tag.toString()
                nextPageSearch?.invoke(checkedKey, model.links.getValue(checkedKey))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is AdapterHolderSearch -> bindView(holder.view, items[position] as ModelSearch)
            else -> super.onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            -1 -> AdapterHolderSearch(parent)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items.getOrNull(position)) {
            is ModelSearch -> -1
            else -> super.getItemViewType(position)
        }
    }

    override fun updateItems(items: List<Any>, linkSelf: Link?, linkNext: Link?) {
        val search1 = this.items.filterIsInstance<ModelSearch>().firstOrNull()
        super.updateItems(items, linkSelf, linkNext)
        search1?.let {
            this.items.filterIsInstance<ModelSearch>().firstOrNull() ?: run {
                addSearchModel(search1)
            }
        }
    }

    override fun updateList() {
        view?.apply {
            chipGroup.children.forEach { chip ->
                if (chip is Chip && chip.isChecked) {
                    linkSelf?.let { link ->
                        nextPageSearch?.invoke(chip.tag as String, link.linkClearPageable)
                    }
                }
            }
        }
    }

    fun addSearchModel(search: ModelSearch) {
        if (this.items.firstOrNull() !is ModelSearch) {
            this.items.add(0, search)
        } else {
            this.items[0] = search
        }
        notifyDataSetChanged()
    }

    @SuppressLint("InflateParams")
    private fun setChip(key: String, holder: View, title: String, link: Link) {
        holder.apply {
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(layout.item_search_view_chip, null)?.let {
                if (it is Chip) {
                    it.id = View.generateViewId()
                    it.tag = key
                    it.text = title
                    chipGroup.addView(it)
                    linkSelf?.let { linkSelf ->
                        if (linkSelf.value.contains("$API_KEY_SEARCH/")) {
                            it.isChecked = linkSelf.linkClear.value == link.linkClear.value
                        } else {
                            it.isChecked = key == SEARCH_SELF
                        }
                    }
                }
            }
        }
    }
}