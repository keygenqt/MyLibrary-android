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
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import com.keygenqt.mylibrary.ui.utils.observes.ObserveSelectCover
import com.keygenqt.mylibrary.ui.utils.observes.ObserveSelectGenre
import kotlinx.android.synthetic.main.fragment_update_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentUpdateBook : BaseFragment(R.layout.fragment_update_book) {

    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewUpdateBook by inject()

    private val observeSelectGenre: ObserveSelectGenre by activityViewModels()
    private val observeSelectCover: ObserveSelectCover by activityViewModels()

    private var modelGenre: ModelBookGenre? = null
    private var modelCover: String? = null

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

        if (args.selfLink.isEmpty()) {
            initToolbar {
                title = getString(R.string.fragment_add_book_title)
            }
            viewModel.book = ModelBook()
        }

        // start page
        if (viewModel.selfLink.value == null && args.selfLink.isNotEmpty()) {
            viewModel.selfLink.postValue(args.selfLink)
        }

        initView {
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(viewModel.book?.apply {
                    genreId = modelGenre?.id
                    coverType = textInputEditTextCover.text.toString()
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
                findNavController().navigate(FragmentUpdateBookDirections.actionFragmentEditBookToFragmentGenres(modelGenre?.id ?: 0))
            }
            selectCover.setOnClickListener {
                findNavController().navigate(FragmentUpdateBookDirections.actionFragmentEditBookToFragmentCover(modelCover))
            }
        }
    }

    @OnCreateAfter
    fun observeSelectGenre() {
        initView {
            observeSelectGenre.selected.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled().let { model ->
                    model?.let {
                        textInputLayoutGenre.isErrorEnabled = false
                        textInputEditTextGenre.setText(model.title)
                        modelGenre = model
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun observeSelectCover() {
        initView {
            observeSelectCover.selected.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled().let { model ->
                    model?.let {
                        textInputLayoutCover.isErrorEnabled = false
                        textInputEditTextCover.setText(model)
                        modelCover = model
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun loading() {
        initView {
            viewModel.loading.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(it)
                }
            })
        }
    }

    @OnCreateAfter
    fun updateCache() {
        initView {
            viewModel.changeLink.observe(viewLifecycleOwner) { link ->
                val model = viewModel.getModel(link)
                statusProgressPage(model == null || model.genre.id == 0L)
                model?.let {
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter fun updateBook() {
        initView {
            viewModel.updateBook.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(false)
                    if (args.selfLink.isEmpty()) {
                        Toast.makeText(activity, getString(R.string.update_book_added_successfully), Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    } else {
                        clearError()
                        scrollView.fullScroll(ScrollView.FOCUS_UP)
                        this.hideKeyboard()
                        body.requestFocus()
                        Toast.makeText(activity, getString(R.string.update_book_updated_successfully), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun updateResponse() {
        initView {
            viewModel.data.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { model ->
                    statusProgressPage(false)
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter
    fun validate() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->

                    when (throwable) {
                        is HttpException -> {
                            if (throwable.status == 404 || throwable.status == 400) {
                                Toast.makeText(activity, getString(R.string.view_book_get_error), Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                        }
                        is ValidateException -> {

                            statusProgress(false)

                            clearError()

                            throwable.errors.forEach {
                                when (it.field) {
                                    "genreId" ->
                                        if (textInputLayoutGenre.error.isNullOrEmpty()) {
                                            textInputLayoutGenre.error = it.defaultMessage
                                        }
                                    "coverType" ->
                                        if (textInputLayoutCover.error.isNullOrEmpty()) {
                                            textInputLayoutCover.error = it.defaultMessage
                                        }
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
                            textInputLayoutBlock.requestFocusTextInputLayoutError(scrollView)
                        }
                    }
                }
            })
        }
    }

    private fun clearError() {
        initView {
            textInputLayoutGenre.isErrorEnabled = false
            textInputLayoutCover.isErrorEnabled = false
            textInputLayoutTitle.isErrorEnabled = false
            textInputLayoutAuthor.isErrorEnabled = false
            textInputLayoutPublisher.isErrorEnabled = false
            textInputLayoutYear.isErrorEnabled = false
            textInputLayoutISBN.isErrorEnabled = false
            textInputLayoutNumberOfPages.isErrorEnabled = false
            textInputLayoutDescription.isErrorEnabled = false
        }
    }

    private fun updateView(model: ModelBook) {
        initView {
            modelGenre?.let {
                textInputEditTextGenre.setText(it.title)
            } ?: run {
                modelGenre = model.genre
                textInputEditTextGenre.setText(model.genre.title)
            }
            modelCover?.let {
                textInputEditTextCover.setText(modelCover)
            } ?: run {
                modelCover = model.coverType
                textInputEditTextCover.setText(model.coverType)
            }
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