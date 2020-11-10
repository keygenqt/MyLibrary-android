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

package com.keygenqt.mylibrary.hal

import android.net.*
import android.view.*
import androidx.annotation.*
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.*
import com.keygenqt.mylibrary.*
import com.keygenqt.mylibrary.interfaces.*
import java.util.*
import kotlin.concurrent.*

class AdapterHolder(
    @LayoutRes id: Int, group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
) : ViewHolder(view)

class AdapterHolderLoading(
    group: ViewGroup,
    view: View = LayoutInflater.from(group.context)
        .inflate(R.layout.common_item_loading, group, false)
) : ViewHolder(view)

abstract class Adapter<T>(
    @LayoutRes val id: Int,
    private val viewModel: ViewModelPage? = null
) :
    RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val LIST_DATA_TYPE_ADD = 1
        const val LIST_DATA_TYPE_SET = 2
    }

    private var linkNext: String? = null

    private var timer = Timer()

    private var items = mutableListOf<T>()

    abstract fun onBindViewHolder(holder: View, model: T)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is AdapterHolder -> onBindViewHolder(holder.view, items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> AdapterHolderLoading(parent)
            1 -> AdapterHolder(id, parent)
            else -> throw RuntimeException("Only AdapterHolderHAL")
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == 0) {
            timer = Timer()
            timer.schedule(800) {
                linkNext?.let {
                    viewModel?.link?.postValue(it)
                }
                timer.cancel()
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == 0) {
            timer.cancel()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items.size == position) {
            true -> 0
            false -> 1
        }
    }

    override fun getItemCount(): Int {
        return items.size + (if (viewModel != null && linkNext != null) 1 else 0)
    }

    @Suppress("UNCHECKED_CAST") fun setListData(listData: ListData<*>, delegate: (type: Int) -> Unit) {
        listData.linkSelf?.let {
            Uri.parse(it).getQueryParameter("page")?.let { page ->
                if (page.toInt() == 0) {
                    this.items = listData.items as ArrayList<T>
                    delegate.invoke(LIST_DATA_TYPE_SET)
                } else {
                    this.items.addAll(listData.items as ArrayList<T>)
                    delegate.invoke(LIST_DATA_TYPE_ADD)
                }
            } ?: run {
                this.items = listData.items as ArrayList<T>
                delegate.invoke(LIST_DATA_TYPE_SET)
            }
        }
        this.linkNext = listData.linkNext
        notifyDataSetChanged()
    }
}