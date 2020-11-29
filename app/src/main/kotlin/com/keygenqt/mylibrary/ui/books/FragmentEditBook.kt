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
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_edit_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentEditBook : BaseFragment(R.layout.fragment_edit_book) {

    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewEditBook by inject()
    private val model: ViewEditBookGenres by activityViewModels()

    private var selectGenreId: String? = null

    override fun onCreateView() {
        initView {
            viewModel.selfLink.postValue(args.selfLink)
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(viewModel.book?.apply {
                    genreId = selectGenreId
                    title = textInputEditTextTitle.text.toString()
                    author = textInputEditTextAuthor.text.toString()
                    publisher = textInputEditTextPublisher.text.toString()
                    year = textInputEditTextYear.text.toString()
                    isbn = textInputEditTextISBN.text.toString()
                    numberOfPages = textInputEditTextNumberOfPages.text.toString()
                    description = textInputEditTextDescription.text.toString()
                })
            }
            selectGenre.setOnClickListener {
                findNavController().navigate(FragmentEditBookDirections.actionFragmentEditBookToFragmentGenres(selectGenreId))
            }
        }
    }

    @CallOnCreate fun selectGenre() {
        initView {
            model.selected.observe(viewLifecycleOwner) { model ->
                model?.let {
                    textInputEditTextGenre.setText(model.title)
                    selectGenreId = model.id
                } ?: run {
                    textInputEditTextGenre.setText("")
                    selectGenreId = null
                }
            }
        }
    }

    @CallOnCreate fun observeUpdate() {
        initView {
            viewModel.updateBook.observe(viewLifecycleOwner) {
                statusProgress(false)
                viewModel.error.postValue(null)
                scrollView.fullScroll(ScrollView.FOCUS_UP)
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
                if (selectGenreId == null) {
                    textInputEditTextGenre.setText(model.genre.title)
                    selectGenreId = model.genre.id
                }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_book, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_take_photo -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}