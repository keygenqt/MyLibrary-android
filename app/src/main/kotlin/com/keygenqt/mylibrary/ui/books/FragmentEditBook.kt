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

import androidx.navigation.fragment.navArgs
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.view.toolbar
import kotlinx.android.synthetic.main.fragment_edit_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentEditBook : BaseFragment(R.layout.fragment_edit_book) {

    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewEditBook by inject()

    override fun onCreateView() {
        initView {
            viewModel.selfLink.postValue(args.selfLink)
            buttonSubmit.setOnClickListener {

            }
        }
    }

    @CallOnCreate fun observeListData() {
        viewModel.loading.observe(viewLifecycleOwner, { status ->
            statusProgress(status)
        })
        viewModel.data.observe(viewLifecycleOwner) { model ->
            initView {
                textInputEditTextTitle.setText(model.title)
                textInputEditTextAuthor.setText(model.author)
                textInputEditTextPublisher.setText(model.publisher)
                textInputEditTextYear.setText(model.year)
                textInputEditTextISBN.setText(model.isbn)
                textInputEditTextNumberOfPages.setText(model.numberOfPages)
                textInputEditTextDescription.setText(model.description)
            }
        }
    }
}