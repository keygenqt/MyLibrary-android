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

import android.widget.ScrollView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_edit_book.view.*
import kotlinx.android.synthetic.main.fragment_edit_book.view.body
import kotlinx.android.synthetic.main.fragment_edit_book.view.buttonSubmit
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentEditBook : BaseFragment(R.layout.fragment_edit_book) {

    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewEditBook by inject()

    override fun onCreateView() {
        initView {
            viewModel.selfLink.postValue(args.selfLink)
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(viewModel.book?.apply {
                    title = textInputEditTextTitle.text.toString()
                    author = textInputEditTextAuthor.text.toString()
                    publisher = textInputEditTextPublisher.text.toString()
                    year = textInputEditTextYear.text.toString()
                    isbn = textInputEditTextISBN.text.toString()
                    numberOfPages = textInputEditTextNumberOfPages.text.toString()
                    description = textInputEditTextDescription.text.toString()
                })
            }
        }
    }

    @CallOnCreate fun observeUpdate() {
        initView {
            viewModel.updateBook.observe(viewLifecycleOwner) {
                statusProgress(false)
                viewModel.error.postValue(null)
                this.hideKeyboard()
                body.requestFocus()
                Toast.makeText(activity, getString(R.string.edit_book_updated_successfully), Toast.LENGTH_SHORT).show()
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

    @CallOnCreate fun observeError() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { throwable ->

                statusProgress(false)

                textInputLayoutTitle.isErrorEnabled = false
                textInputLayoutAuthor.isErrorEnabled = false
                textInputLayoutPublisher.isErrorEnabled = false
                textInputLayoutYear.isErrorEnabled = false
                textInputLayoutISBN.isErrorEnabled = false
                textInputLayoutNumberOfPages.isErrorEnabled = false
                textInputLayoutDescription.isErrorEnabled = false

                if (throwable is ValidateException) {

                    scrollView.fullScroll(ScrollView.FOCUS_UP)

                    throwable.errors.forEach {
                        when (it.field) {
                            "title" ->
                                if (textInputLayoutTitle.error.isNullOrEmpty()) {
                                    textInputLayoutTitle.error = it.defaultMessage
                                }
                            "author" ->
                                if (textInputLayoutAuthor.error.isNullOrEmpty()) {
                                    textInputLayoutAuthor.error = it.defaultMessage
                                }
                            "publisher" ->
                                if (textInputLayoutPublisher.error.isNullOrEmpty()) {
                                    textInputLayoutPublisher.error = it.defaultMessage
                                }
                            "year" ->
                                if (textInputLayoutYear.error.isNullOrEmpty()) {
                                    textInputLayoutYear.error = it.defaultMessage
                                }
                            "ISBN" ->
                                if (textInputLayoutISBN.error.isNullOrEmpty()) {
                                    textInputLayoutISBN.error = it.defaultMessage
                                }
                            "numberOfPages" ->
                                if (textInputLayoutNumberOfPages.error.isNullOrEmpty()) {
                                    textInputLayoutNumberOfPages.error = it.defaultMessage
                                }
                            "description" ->
                                if (textInputLayoutDescription.error.isNullOrEmpty()) {
                                    textInputLayoutDescription.error = it.defaultMessage
                                }
                        }
                    }
                }
            })
        }
    }
}