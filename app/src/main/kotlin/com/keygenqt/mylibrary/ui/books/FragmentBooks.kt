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

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.ListAdapter
import com.keygenqt.mylibrary.base.ListAdapterSearch
import kotlinx.android.synthetic.main.common_fragment_list.view.notFound
import kotlinx.android.synthetic.main.common_fragment_list.view.recyclerView
import kotlinx.android.synthetic.main.common_fragment_list.view.refresh
import org.koin.android.ext.android.inject

@ActionBarEnable
@BottomNavigationEnable
@FragmentTitle("My Library")
class FragmentBooks : BaseFragment(R.layout.common_fragment_list) {

    private val viewModel: ViewBooks by inject()
    private val sharedPreferences: BaseSharedPreferences by inject()

    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterBooks(R.layout.item_book_list, viewModel) { key, link ->
                when (key) {
                    AdapterBooks.SEARCH_SELF -> {
                        viewModel.link.postValue(null)
                    }
                    AdapterBooks.SEARCH_FIND_ALL_BY_USER_ID -> {
                        viewModel.link.postValue(link.linkWithParams(hashMapOf("userId" to sharedPreferences.userId!!)))
                    }
                    AdapterBooks.SEARCH_FIND_ALL_BY_SALE -> {
                        viewModel.link.postValue(link.linkWithParams(hashMapOf("sale" to "true")))
                    }
                }
            }
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                if (viewModel.link.value?.isSearch() == true) {
                    viewModel.link.postValue(viewModel.link.value!!.linkClearPageable)
                } else {
                    viewModel.link.postValue(null)
                }
            }
        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.loading.observe(viewLifecycleOwner, { status ->
                refresh.isRefreshing = status
            })
        }
    }

    @CallOnCreate fun observeSearchModel() {
        initView {
            viewModel.search.observe(viewLifecycleOwner, { searchModel ->
                (recyclerView.adapter as ListAdapterSearch<*>).setSearchModel(searchModel)
            })
        }
    }

    @CallOnCreate fun observeListData() {
        initView {
            viewModel.listData.observe(viewLifecycleOwner) { listData ->
                (recyclerView.adapter as ListAdapter<*>).setListData(listData) { type ->
                    if (type == ListAdapter.LIST_DATA_TYPE_SET) {
                        notFound.visibility = if (listData.items.isEmpty()) View.VISIBLE else View.GONE
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(FragmentBooksDirections.actionFragmentBooksToFragmentSettings())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}