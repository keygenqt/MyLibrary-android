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

import android.Manifest
import android.media.ThumbnailUtils
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.databinding.FragmentUpdateBookBinding
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import com.keygenqt.mylibrary.ui.observes.ObserveSelectCover
import com.keygenqt.mylibrary.ui.observes.ObserveSelectGenre
import com.keygenqt.mylibrary.ui.observes.ObserveUpdateBooks
import com.keygenqt.mylibrary.utils.API_IMAGE_BOOK_HEIGHT
import com.keygenqt.mylibrary.utils.API_IMAGE_BOOK_WIDTH
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentUpdateBook : BaseFragment<FragmentUpdateBookBinding>() {

    private val preferences: BaseSharedPreferences by inject()
    private val args: FragmentUpdateBookArgs by navArgs()
    private val viewModel: ViewUpdateBook by inject()

    private val observeSelectGenre: ObserveSelectGenre by activityViewModels()
    private val observeSelectCover: ObserveSelectCover by activityViewModels()
    private val observeUpdateBooks: ObserveUpdateBooks by activityViewModels()

    private var modelGenre: ModelBookGenre? = null
    private var modelCover: String? = null
    private var imageLink: String? = null

    override fun onCreateOptionsMenu(): Int {
        return R.menu.menu_edit_book
    }

    // menu actions
    override fun onOptionsItemSelected(id: Int) {
        TedPermission.with(context)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    when (id) {
                        R.id.action_take_photo -> {
                            takePhoto { bitmap ->
                                bitmap?.let {
                                    statusProgress(true)
                                    viewModel.uploadImage(ThumbnailUtils.extractThumbnail(bitmap, API_IMAGE_BOOK_WIDTH, API_IMAGE_BOOK_HEIGHT))
                                } ?: run {
                                    Toast.makeText(activity, getString(R.string.failed_try_later), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        R.id.action_take_image -> {
                            takeImage { bitmap ->
                                bitmap?.let {
                                    statusProgress(true)
                                    viewModel.uploadImage(ThumbnailUtils.extractThumbnail(bitmap, API_IMAGE_BOOK_WIDTH, API_IMAGE_BOOK_HEIGHT))
                                } ?: run {
                                    Toast.makeText(activity, getString(R.string.failed_try_later), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {

                }
            })
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentUpdateBookBinding {
        return FragmentUpdateBookBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {

        if (args.selfLink.isEmpty()) {
            toolbar {
                title = getString(R.string.fragment_add_book_title)
            }
            viewModel.relation = RelationBook(model = ModelBook())
        }

        // start page
        if (viewModel.selfLink.value == null && args.selfLink.isNotEmpty()) {
            viewModel.selfLink.postValue(args.selfLink)
        }

        bind {
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(viewModel.relation?.model?.apply {
                    genreId = modelGenre?.id ?: 0L
                    image = imageLink
                    coverType = textInputEditTextCover.text.toString()
                    title = textInputEditTextTitle.text.toString()
                    author = textInputEditTextAuthor.text.toString()
                    publisher = textInputEditTextPublisher.text.toString()
                    year = textInputEditTextYear.text.toString()
                    isbn = textInputEditTextISBN.text.toString()
                    numberOfPages = textInputEditTextNumberOfPages.text.toString()
                    description = textInputEditTextDescription.text.toString()
                    sale = switchItemSwap.isChecked
                })
            }
            selectGenre.setOnClickListener {
                root.findNavController().navigate(
                    FragmentUpdateBookDirections.actionFragmentEditBookToFragmentGenres(
                        modelGenre?.id ?: 0
                    )
                )
            }
            selectCover.setOnClickListener {
                root.findNavController().navigate(
                    FragmentUpdateBookDirections.actionFragmentEditBookToFragmentCover(
                        modelCover
                    )
                )
            }
        }
    }

    @OnCreateAfter
    fun observeSelectImage() {
        bind {
            viewModel.imageLink.observe(viewLifecycleOwner) { link ->
                imageLink = link
                statusProgress(false)
                imageBook.visibility = View.VISIBLE
                imageBookError.visibility = View.GONE
                Glide.with(root)
                    .load(link)
                    .placeholder(preferences.resDefaultBook)
                    .error(preferences.resDefaultBook)
                    .into(imageBook)

            }
        }
    }

    @OnCreateAfter
    fun observeSelectGenre() {
        bind {
            observeSelectGenre.selected.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled().let { model ->
                    model?.let {
                        textInputLayoutGenre.isErrorEnabled = false
                        textInputEditTextGenre.setText(model.title)
                        modelGenre = ModelBookGenre(
                            id = model.id,
                            title = model.title,
                            description = model.description
                        )
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun observeSelectCover() {
        bind {
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
        bind {
            viewModel.loading.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(it)
                }
            })
        }
    }

    @OnCreateAfter
    fun updateCache() {
        bind {
            viewModel.changeLink.observe(viewLifecycleOwner) { link ->
                val model = viewModel.getModel(link)
                statusProgressPage(model?.user == null || model.genre == null)
                model?.let {
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter fun updateBook() {
        bind {
            viewModel.updateBook.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(false)
                    if (args.selfLink.isEmpty()) {
                        Toast.makeText(activity, getString(R.string.update_book_added_successfully), Toast.LENGTH_SHORT).show()
                        observeUpdateBooks.update(true)
                        root.findNavController().navigateUp()
                    } else {
                        clearError()
                        scrollView.fullScroll(ScrollView.FOCUS_UP)
                        root.hideKeyboard()
                        body.requestFocus()
                        Toast.makeText(activity, getString(R.string.update_book_updated_successfully), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @OnCreateAfter
    fun updateResponse() {
        bind {
            viewModel.data.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { model ->
                    statusProgressPage(false)
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter
    fun validateImage() {
        bind {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                if (imageLink == null)
                    event?.peekContent()?.let { throwable ->
                        when (throwable) {
                            is ValidateException -> {
                                throwable.errors.forEach {
                                    when (it.field) {
                                        "image" -> {
                                            if (throwable.errors.size == 1) {
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    scrollView.fullScroll(ScrollView.FOCUS_UP)
                                                }, 100)
                                            }
                                            imageBookErrorText.text = it.defaultMessage
                                            imageBookError.visibility = View.VISIBLE
                                        }
                                    }
                                }
                            }
                        }
                    }
            })
        }
    }

    @OnCreateAfter
    fun validate() {
        bind {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->

                    when (throwable) {
                        is HttpException -> {
                            if (throwable.status == 404 || throwable.status == 400) {
                                Toast.makeText(activity, getString(R.string.view_book_get_error), Toast.LENGTH_SHORT).show()
                                root.findNavController().navigateUp()
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
        bind {
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

    private fun updateView(relation: RelationBook) {
        bind {
            modelGenre?.let {
                textInputEditTextGenre.setText(it.title)
            } ?: run {
                modelGenre = relation.genre
                textInputEditTextGenre.setText(relation.genre?.title)
            }
            relation.model.let { model ->

                if (!model.image.isNullOrEmpty()) {
                    imageLink = model.image
                    imageBook.visibility = View.VISIBLE
                    Glide.with(root)
                        .load(model.image)
                        .placeholder(preferences.resDefaultBook)
                        .error(preferences.resDefaultBook)
                        .into(imageBook)
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

                switchItemSwap.isChecked = model.sale
            }
        }
    }
}