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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.hal.Link
import java.util.Timer
import kotlin.concurrent.schedule

class AdapterHolder(
    @LayoutRes id: Int, group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
) : ViewHolder(view)

class AdapterHolderLoading(
    group: ViewGroup,
    view: View = LayoutInflater.from(group.context)
        .inflate(R.layout.common_item_loading, group, false)
) : ViewHolder(view)

@Suppress("UNCHECKED_CAST")
abstract class ListAdapter<T>(@LayoutRes val id: Int, var nextPage: ((Link) -> Unit)? = null)
    : RecyclerView.Adapter<ViewHolder>() {

    var linkSelf: Link? = null
    private var linkNext: Link? = null
    private var timer = Timer()
    internal var items = mutableListOf<Any>()

    abstract fun onBindViewHolder(holder: View, model: Any)

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
                linkNext?.let { link ->
                    nextPage?.invoke(link)
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
        return items.size + (if (nextPage != null && linkNext != null) 1 else 0)
    }

    open fun updateItems(items: List<Any>, linkSelf: Link?, linkNext: Link?) {
        this.items.clear()
        this.items.addAll(items)
        this.linkNext = linkNext
        this.linkSelf = linkSelf
        notifyDataSetChanged()
    }

    open fun updateList() {
        linkSelf?.let { link ->
            nextPage?.invoke(link.linkClearPageable)
        }
    }
}