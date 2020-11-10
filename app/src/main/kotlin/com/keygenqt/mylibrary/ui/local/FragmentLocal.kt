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
import androidx.core.content.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.hal.*
import kotlinx.android.synthetic.main.common_fragment_list.view.*
import org.koin.android.ext.android.*

@ActionBarEnable
@BottomNavigationEnable
@FragmentTitle("My Library")
class FragmentLocal : BaseFragment(R.layout.common_fragment_list) {

    private val viewModel: ViewLocal by inject()

    override fun onCreateView() {
        initView {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterLocal(R.layout.item_book_list, viewModel)
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))

            refresh.setOnRefreshListener {
                viewModel.link.postValue(null)
            }

            viewModel.loading.observe(viewLifecycleOwner, { status ->
                refresh.isRefreshing = status
            })

            viewModel.listData.observe(viewLifecycleOwner, { listData ->
                (recyclerView.adapter as Adapter<*>).setListData(listData) { type ->
                    if (type == Adapter.LIST_DATA_TYPE_SET) {
                        notFound.visibility = if (listData.items.isEmpty()) View.VISIBLE else View.GONE
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(FragmentLocalDirections.actionFragmentLocalToFragmentSettings())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}