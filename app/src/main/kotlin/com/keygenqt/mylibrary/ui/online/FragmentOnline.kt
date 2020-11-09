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

package com.keygenqt.mylibrary.ui.online

import android.view.*
import androidx.core.content.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.keygenqt.mylibrary.*
import com.keygenqt.mylibrary.annotations.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.base.BaseListData.*
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_SET
import com.keygenqt.mylibrary.ui.local.*
import kotlinx.android.synthetic.main.common_fragment_list.*
import kotlinx.android.synthetic.main.common_fragment_list.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
@BottomNavigationEnable
@FragmentTitle("Users Libs")
class FragmentOnline : BaseFragment(R.layout.common_fragment_list) {

    private val viewModels: ViewLocal by inject()

    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterOnline(R.layout.item_book_list, viewModels)
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            val refreshF = {
                refresh.isRefreshing = true
                viewModels.updateList()
            }
            refresh.setOnRefreshListener(refreshF)
            refreshF()
        }
    }

    @ObserveInit private fun statusRefresh() {
        viewModels.loading.observe(viewLifecycleOwner, { status ->
            refresh.isRefreshing = status
        })
    }

    @ObserveInit private fun setItems() {
        viewModels.items.observe(viewLifecycleOwner, { (items, type) ->
            if (type == LIST_DATA_TYPE_SET) {
                notFound.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
                (recyclerView.adapter as BaseAdapter).setItems(items)
                recyclerView.smoothScrollToPosition(0)
            }
        })
    }

    @ObserveInit private fun addAdapterItems() {
        viewModels.items.observe(viewLifecycleOwner, { (items, type) ->
            if (type == Companion.LIST_DATA_TYPE_ADD) {
                (recyclerView.adapter as BaseAdapter).addItems(items)
            }
        })
    }
}