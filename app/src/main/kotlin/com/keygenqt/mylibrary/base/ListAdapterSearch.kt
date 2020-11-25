///*
// * Copyright 2020 Vitaliy Zarubin
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.keygenqt.mylibrary.base
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.LayoutRes
//import androidx.recyclerview.widget.RecyclerView.*
//import com.google.android.material.chip.Chip
//import com.keygenqt.mylibrary.R
//import com.keygenqt.mylibrary.hal.API_KEY_SELF
//import com.keygenqt.mylibrary.hal.Link
//import com.keygenqt.mylibrary.interfaces.ViewModelPage
//import kotlinx.android.synthetic.main.item_search.view.chipGroup
//
//class AdapterHolderSearch(
//    @LayoutRes id: Int, group: ViewGroup,
//    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
//) : ViewHolder(view)
//
//open class ListAdapterSearch<T>(
//    @LayoutRes layout: Int,
//    viewModel: ViewModelPage,
//    private val search: (String, Link) -> Unit
//) : ListAdapter<T>(layout, viewModel) {
//
//    private var checkedKey = API_KEY_SELF
//
//    private val idToKey = hashMapOf<Int, String>()
//
//    open fun getStrings(): LinkedHashMap<String, Int> {
//        return linkedMapOf()
//    }
//
//    override fun onBindViewHolder(holder: View, model: T) {}
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
//        when (holder) {
//            is AdapterHolderSearch -> onBindViewHolderFilter(holder.view, items[position] as BaseSearchModel)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return when (viewType) {
//            2 -> AdapterHolderSearch((items[0] as BaseSearchModel).filter, parent)
//            else -> super.onCreateViewHolder(parent, viewType)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (items.size > position) {
//            when (items[position]) {
//                is BaseSearchModel -> return 2
//            }
//        }
//        return super.getItemViewType(position)
//    }
//
//    @SuppressLint("InflateParams")
//    override fun onBindViewHolderFilter(holder: View, model: BaseSearchModel) {
//        holder.apply {
//            chipGroup.setOnCheckedChangeListener(null)
//            chipGroup.removeAllViews()
//            getStrings().forEach {
//                if (model.links.containsKey(it.key)) {
//                    setChip(it.key, holder)
//                }
//            }
//            chipGroup.setOnCheckedChangeListener { _, checkedId ->
//                idToKey[checkedId]?.let {
//                    checkedKey = it
//                    search.invoke(checkedKey, model.links.getValue(checkedKey))
//                }
//            }
//        }
//    }
//
////    override fun clearItemsBeforeSetList() {
////        items.firstOrNull()?.let {
////            if (it is BaseSearchModel) {
////                items.clear()
////                items.add(it)
////            } else {
////                items.clear()
////            }
////        } ?: run {
////            items.clear()
////        }
////    }
//
//    fun setSearchModel(searchModel: BaseSearchModel) {
//        this.items.add(0, searchModel)
//        notifyDataSetChanged()
//    }
//
//    private fun setChip(key: String, holder: View) {
//        holder.apply {
//            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_search_view_chip, null)?.let {
//                if (it is Chip) {
//                    it.id = View.generateViewId()
//                    idToKey[it.id] = key
//                    it.text = context.getString(getStrings().getValue(key))
//                    chipGroup.addView(it)
//                    if (checkedKey == key) {
//                        chipGroup.check(it.id)
//                    }
//                }
//            }
//        }
//    }
//}