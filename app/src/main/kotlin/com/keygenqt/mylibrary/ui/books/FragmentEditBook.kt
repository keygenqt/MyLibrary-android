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
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.ui.utils.observes.ObserveSelectGenre
import com.keygenqt.mylibrary.ui.utils.observes.ObserveUpdateBook
import kotlinx.android.synthetic.main.fragment_edit_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentEditBook : BaseFragment(R.layout.fragment_edit_book) {

    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewEditBook by inject()

    private val observeSelectGenre: ObserveSelectGenre by activityViewModels()
    private val observeUpdateBook: ObserveUpdateBook by activityViewModels()

    private var selectGenreId: String? = null

    // menu
    override fun onCreateOptionsMenu(): Int {
        return R.menu.menu_edit_book
    }

    override fun onOptionsItemSelected(id: Int) {
        when (id) {
            R.id.action_take_photo -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // bind view
    override fun onCreateView() {

        // start page
        if (viewModel.selfLink.value == null) {
            viewModel.selfLink.postValue(LiveDataEvent(args.selfLink))
        }

        initView {
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(LiveDataEvent(viewModel.book?.apply {
                    genreId = selectGenreId
                    title = textInputEditTextTitle.text.toString()
                    author = textInputEditTextAuthor.text.toString()
                    publisher = textInputEditTextPublisher.text.toString()
                    year = textInputEditTextYear.text.toString()
                    isbn = textInputEditTextISBN.text.toString()
                    numberOfPages = textInputEditTextNumberOfPages.text.toString()
                    description = textInputEditTextDescription.text.toString()
                }))
            }
            selectGenre.setOnClickListener {
                findNavController().navigate(FragmentEditBookDirections.actionFragmentEditBookToFragmentGenres(selectGenreId))
            }
        }
    }

    @InitObserve fun observeSelectGenre() {
        initView {
            observeSelectGenre.selected.observe(viewLifecycleOwner) { event ->
                event?.peekContent().let { model ->
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
    }

    @InitObserve fun updateBook() {
        initView {
            viewModel.updateBook.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(false)
                    viewModel.error.postValue(null)
                    scrollView.fullScroll(ScrollView.FOCUS_UP)
                    this.hideKeyboard()
                    body.requestFocus()
                    Toast.makeText(activity, getString(R.string.edit_book_updated_successfully), Toast.LENGTH_SHORT).show()

                    // call change
                    observeUpdateBook.change(viewModel.book)
                }
            }
        }
    }

    @InitObserve fun loading() {
        initView {
            viewModel.loading.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(it)
                }
            })
        }
    }

    @InitObserve fun initData() {
        initView {
            viewModel.data.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { model ->
                    textInputEditTextTitle.setText(model.title)
                    textInputEditTextAuthor.setText(model.author)
                    textInputEditTextPublisher.setText(model.publisher)
                    textInputEditTextYear.setText(model.year)
                    textInputEditTextISBN.setText(model.isbn)
                    textInputEditTextNumberOfPages.setText(model.numberOfPages)
                    textInputEditTextDescription.setText(model.description)
                    textInputEditTextGenre.setText(model.genre.title)
                    selectGenreId = model.genre.id
                }
            }
        }
    }

    @InitObserve fun validate() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->

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
                }
            })
        }
    }
}