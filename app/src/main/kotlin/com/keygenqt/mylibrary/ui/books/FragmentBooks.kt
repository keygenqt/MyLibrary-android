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

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.ListSearchAdapter
import com.keygenqt.mylibrary.extensions.showWithPadding
import com.keygenqt.mylibrary.ui.other.FragmentSplashDirections
import kotlinx.android.synthetic.main.common_fragment_list.view.commonFab
import kotlinx.android.synthetic.main.common_fragment_list.view.notFound
import kotlinx.android.synthetic.main.common_fragment_list.view.recyclerView
import kotlinx.android.synthetic.main.common_fragment_list.view.refresh
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentBooks : BaseFragment(R.layout.common_fragment_list) {

    private val viewModel: ViewBooks by inject()

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
            recyclerView.adapter = AdapterBooks(R.layout.item_book_list) { next ->
                statusProgress(next.isFirstPage())
                viewModel.updateList(next)
            }
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                (recyclerView.adapter as? ListSearchAdapter<*>)?.getLinkForUpdate()?.let {
                    statusProgress(true)
                    viewModel.updateList(it)
                }
            }
            commonFab.setImageResource(R.drawable.ic_baseline_add)
            commonFab.showWithPadding(recyclerView)
            commonFab.setOnClickListener {
                findNavController().navigate(FragmentBooksDirections.actionFragmentBooksToFragmentBookAdd())
            }
        }
    }

    @OnCreateAfter
    fun updateCache() {
        initView {
            viewModel.changeLink.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { links ->
                    (recyclerView.adapter as? ListSearchAdapter<*>)?.let { adapter ->
                        adapter.updateLinks(links).updateItems(viewModel.findItems(links.self))
                        viewModel.findSearch()?.let { search ->
                            adapter.setSearchModel(search)
                        }
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun updateResponse() {
        initView {
            viewModel.linkSwitch.observe(viewLifecycleOwner) { links ->
                (recyclerView.adapter as? ListSearchAdapter<*>)?.let { adapter ->
                    adapter.updateLinks(links).updateItems(viewModel.findItems(links.self, adapter.getIds()))
                    notFound.visibility = if (adapter.isEmpty()) View.VISIBLE else View.GONE
                    refresh.isRefreshing = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        statusProgress(false)
                    }, 3000)
                }
            }
        }
    }

    @OnCreateAfter
    fun search() {
        initView {
            viewModel.search.observe(viewLifecycleOwner) { search ->
                (recyclerView.adapter as? ListSearchAdapter<*>)?.setSearchModel(search)
            }
        }
    }
}