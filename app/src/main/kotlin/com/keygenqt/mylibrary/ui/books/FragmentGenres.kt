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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.ListAdapter
import com.keygenqt.mylibrary.databinding.CommonFragmentListBinding
import com.keygenqt.mylibrary.extensions.showWithPadding
import com.keygenqt.mylibrary.ui.observes.ObserveSelectGenre
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentGenres : BaseFragment<CommonFragmentListBinding>() {

    private val args: FragmentGenresArgs by navArgs()
    private val viewModel: ViewGenres by inject()

    private val observeSelectGenre: ObserveSelectGenre by activityViewModels()

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): CommonFragmentListBinding {
        return CommonFragmentListBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        bind {
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterGenres(R.layout.item_select_list, args.selectGenreId, commonFab, recyclerView) { next ->
                viewModel.updateList(next)
            }
            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                (recyclerView.adapter as? ListAdapter<*>)?.getLinkForUpdate()?.let {
                    viewModel.updateList(it)
                }
            }
            if (args.selectGenreId != 0L) {
                commonFab.showWithPadding(recyclerView)
            }
            commonFab.setOnClickListener {
                observeSelectGenre.select((recyclerView.adapter as AdapterGenres).getSelectItem())
                root.findNavController().navigateUp()
            }
        }
    }

    @OnCreateAfter
    fun updateCache() {
        bind {
            viewModel.changeLink.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { links ->
                    (recyclerView.adapter as? ListAdapter<*>)?.let { adapter ->
                        adapter.updateLinks(links).updateItems(viewModel.findItemsLimit(10))
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun updateResponse() {
        bind {
            viewModel.linkSwitch.observe(viewLifecycleOwner) { links ->
                (recyclerView.adapter as? ListAdapter<*>)?.let { adapter ->
                    adapter.updateLinks(links).updateItems(viewModel.findItems(adapter.getIds()))
                    notFound.visibility = if (adapter.isEmpty()) View.VISIBLE else View.GONE
                    refresh.isRefreshing = false
                    statusProgress(false)
                }
            }
        }
    }
}