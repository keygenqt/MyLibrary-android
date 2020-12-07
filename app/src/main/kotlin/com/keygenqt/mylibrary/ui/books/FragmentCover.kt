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
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.ListAdapter
import com.keygenqt.mylibrary.data.models.ModelBook.Companion.COVER_TYPE_OTHER
import com.keygenqt.mylibrary.data.models.ModelBook.Companion.COVER_TYPE_SOFT
import com.keygenqt.mylibrary.data.models.ModelBook.Companion.COVER_TYPE_SOLID
import com.keygenqt.mylibrary.databinding.CommonFragmentListBinding
import com.keygenqt.mylibrary.extensions.showWithPadding
import com.keygenqt.mylibrary.ui.observes.ObserveSelectCover

@ActionBarEnable
class FragmentCover : BaseFragment<CommonFragmentListBinding>() {

    private val args: FragmentCoverArgs by navArgs()
    private val observeSelectCover: ObserveSelectCover by activityViewModels()

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): CommonFragmentListBinding {
        return CommonFragmentListBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        bind {

            refresh.isEnabled = false

            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = AdapterCover(R.layout.item_select_list, args.selectCover, commonFab, recyclerView)

            args.selectCover?.let {
                commonFab.showWithPadding(recyclerView)
            }
            commonFab.setOnClickListener {
                observeSelectCover.select((recyclerView.adapter as AdapterCover).getSelectItem())
                root.findNavController().navigateUp()
            }

            (recyclerView.adapter as ListAdapter<*>).updateItems(listOf(
                COVER_TYPE_SOFT,
                COVER_TYPE_SOLID,
                COVER_TYPE_OTHER,
            ))
        }
    }
}