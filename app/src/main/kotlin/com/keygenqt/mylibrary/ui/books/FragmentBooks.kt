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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.ListSearchAdapter
import com.keygenqt.mylibrary.ui.utils.observes.ObserveUpdateBook
import kotlinx.android.synthetic.main.common_fragment_list.view.notFound
import kotlinx.android.synthetic.main.common_fragment_list.view.recyclerView
import kotlinx.android.synthetic.main.common_fragment_list.view.refresh
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentBooks : BaseFragment(R.layout.common_fragment_list) {

    private val viewModel: ViewBooks by inject()

    private val observeUpdateBook: ObserveUpdateBook by activityViewModels()

    // menu
    override fun onCreateOptionsMenu(): Int {
        return R.menu.menu_main_page
    }

    override fun onOptionsItemSelected(id: Int) {
        when (id) {
            R.id.action_settings -> {
                findNavController().navigate(FragmentBooksDirections.actionFragmentBooksToFragmentSettings())
            }
        }
    }

    // bind view
    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterBooks(R.layout.item_book_list) { key, linkNext ->
                refresh.isRefreshing = ListSearchAdapter.SEARCH_PAGE != key
                viewModel.updateList(key, linkNext)
            }
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                (recyclerView.adapter as ListSearchAdapter<*>).updateList()
            }
        }
    }

    @InitObserve fun observeUpdateBook() {
        initView {
            observeUpdateBook.change.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { model ->
                    (recyclerView.adapter as AdapterBooks).updateItem(model)
                }
            }
        }
    }

    @InitObserve fun linkSearchSwitch() {
        initView {
            viewModel.linkSearchSwitch.observe(viewLifecycleOwner) { event ->
                event?.peekContent()?.let { listData ->
                    refresh.isRefreshing = false
                    (recyclerView.adapter as ListSearchAdapter<*>).updateItems(listData.items, listData.linkSelf, listData.linkNext)
                    notFound.visibility = if (listData.items.isEmpty()) View.VISIBLE else View.GONE
                }

            }
        }
    }

    @InitObserve fun search() {
        initView {
            viewModel.search.observe(viewLifecycleOwner) { event ->
                event?.peekContent()?.let { search ->
                    (recyclerView.adapter as ListSearchAdapter<*>).addSearchModel(search)
                }
            }
        }
    }
}