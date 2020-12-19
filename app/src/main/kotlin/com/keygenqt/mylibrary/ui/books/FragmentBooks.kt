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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.ActionBarSearchEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.ListSearchAdapter
import com.keygenqt.mylibrary.databinding.CommonFragmentListBinding
import com.keygenqt.mylibrary.extensions.showWithPadding
import com.keygenqt.mylibrary.ui.observes.ObserveUpdateBooks
import org.koin.android.ext.android.inject

@ActionBarEnable
@ActionBarSearchEnable
class FragmentBooks : BaseFragment<CommonFragmentListBinding>() {

    private val viewModel: ViewBooks by inject()
    private val preferences: BaseSharedPreferences by inject()
    private val observeUpdateBooks: ObserveUpdateBooks by activityViewModels()

    private var updateBooks = false

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

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): CommonFragmentListBinding {
        return CommonFragmentListBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        if (preferences.firstOpen) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.dialog_warning_title))
                .setMessage(resources.getString(R.string.dialog_warning_text))
                .setNeutralButton(resources.getString(R.string.dialog_ok)) { dialog, _ ->
                    preferences.firstOpen = false
                    dialog.dismiss()
                }
                .show()
        }

        bind {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterBooks(R.layout.item_book_list) { next ->
                statusProgress(next.isFirstPage())
                viewModel.updateList(next)
            }
            refresh.setColorSchemeColors(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            refresh.setOnRefreshListener {
                (recyclerView.adapter as? ListSearchAdapter<*>)?.getLinkForUpdate()?.let {
                    viewModel.updateList(it)
                }
            }
            commonFab.setImageResource(R.drawable.ic_baseline_add)
            commonFab.showWithPadding(recyclerView)
            commonFab.setOnClickListener {
                findNavController().navigate(FragmentBooksDirections.actionFragmentBooksToFragmentBookAdd())
            }

            activity?.findViewById<FloatingSearchView>(R.id.floatingSearchView)?.apply {
                setSearchText(viewModel.searchValue)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_menu -> {
                            activity?.openOptionsMenu()
                        }
                    }
                }
                setOnQueryChangeListener { _, newQuery ->
                    searchDelay {
                        bind {
                            (recyclerView.adapter as? ListSearchAdapter<*>)?.getLinkForUpdate()?.let {
                                viewModel.updateSearch(it, if (newQuery.isNullOrEmpty()) null else newQuery)
                            }
                        }
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun observeUpdateBooks() {
        bind {
            observeUpdateBooks.update.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    updateBooks = it
                }
            }
        }
    }

    @OnCreateAfter
    fun updateCache() {
        bind {
            viewModel.changeLink.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { links ->
                    (recyclerView.adapter as? ListSearchAdapter<*>)?.let { adapter ->
                        adapter.updateLinks(links).updateItems(viewModel.findItemsLimit(links.self, 10))
                        viewModel.findSearch()?.let { adapter.setSearchModel(it) }
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun updateResponse() {
        bind {
            viewModel.linkSwitch.observe(viewLifecycleOwner) { links ->
                if (updateBooks) {
                    updateBooks = false
                    viewModel.updateList(links.self.linkClearPageable)
                } else {
                    (recyclerView.adapter as? ListSearchAdapter<*>)?.let { adapter ->
                        adapter.updateLinks(links).updateItems(viewModel.findItems(links.self, adapter.getIds()))
                        notFound.visibility = if (adapter.isEmpty()) View.VISIBLE else View.GONE
                        refresh.isRefreshing = false
                        statusProgress(false)
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun search() {
        bind {
            viewModel.search.observe(viewLifecycleOwner) { search ->
                (recyclerView.adapter as? ListSearchAdapter<*>)?.setSearchModel(search)
            }
        }
    }
}