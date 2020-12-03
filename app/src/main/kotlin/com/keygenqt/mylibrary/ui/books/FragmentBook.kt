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

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.SpawnAnimation
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.ui.utils.observes.ObserveUpdateBook
import com.keygenqt.mylibrary.ui.utils.observes.ObserveUpdateBooks
import kotlinx.android.synthetic.main.activity_main.view.toolbar
import kotlinx.android.synthetic.main.common_fragment_list.view.refresh
import kotlinx.android.synthetic.main.fragment_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
@SpawnAnimation
class FragmentBook : BaseFragment(R.layout.fragment_book) {

    private val preferences: BaseSharedPreferences by inject()
    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewBook by inject()

    private val observeUpdateBook: ObserveUpdateBook by activityViewModels()
    private val observeUpdateBooks: ObserveUpdateBooks by activityViewModels()

    private var menu: Menu? = null

    override fun isSpawnAnimation(): Boolean {
        return viewModel.book == null
    }

    // menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu_book_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(id: Int) {
        when (id) {
            R.id.book_menu_edit -> {
                viewModel.book?.let {
                    findNavController().navigate(FragmentBookDirections.actionFragmentBookToFragmentEditBook(it.selfLink))
                }
            }
            R.id.book_menu_delete -> {
                viewModel.delete()
            }
            R.id.book_menu_share -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // bind view
    override fun onCreateView() {
        initView {

            // start page
            if (viewModel.selfLink.value == null) {
                viewModel.selfLink.postValue(LiveDataEvent(args.selfLink))
            }

            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                viewModel.selfLink.postValue(LiveDataEvent(args.selfLink))
            }
            appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
                refresh.isEnabled = verticalOffset == 0
                refresh.isRefreshing = false
            })
            buttonMessage.setOnClickListener {
                viewModel.book?.let {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${it.user.email}")
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_feedback_subject))
                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    }
                    startActivity(Intent.createChooser(emailIntent, "Send from MyLibrary"))
                }
            }
        }
    }

    @OnCreateAfter fun observeUpdateBook() {
        initView {
            observeUpdateBook.change.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let { model ->
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter fun loading() {
        initView {
            viewModel.loading.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let {
                    statusProgressPage(it)
                    refresh.isRefreshing = false
                }
            })
        }
    }

    @OnCreateAfter fun initData() {
        initView {
            viewModel.data.observe(viewLifecycleOwner) { event ->
                event?.peekContent()?.let { model ->
                    updateView(model)
                }
            }
        }
    }

    @OnCreateAfter fun error() {
        viewModel.error.observe(viewLifecycleOwner, { event ->
            event?.peekContentHandled()?.let { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.status == 404 || throwable.status == 400) {
                            Toast.makeText(activity, getString(R.string.view_book_get_error), Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        })
    }

    @OnCreateAfter fun delete() {
        viewModel.delete.observe(viewLifecycleOwner, { event ->
            event?.peekContentHandled()?.let { result ->
                if (result.status == 200) {
                    observeUpdateBooks.change(viewModel.book)
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun updateView(model: ModelBook) {
        initToolbar {
            toolbar.title = model.title
        }
        initView {
            Glide.with(this)
                .load(model.image)
                .placeholder(preferences.resDefaultBook)
                .error(preferences.resDefaultBook)
                .into(bookImage)

            bookTitle.text = model.title
            bookAuthor.text = model.author

            bookPublisherBlock.visibility = if (model.publisher.isNullOrEmpty()) View.GONE else View.VISIBLE
            bookISBNBlock.visibility = if (model.isbn.isNullOrEmpty()) View.GONE else View.VISIBLE
            bookYearBlock.visibility = if (model.year.isNullOrEmpty()) View.GONE else View.VISIBLE
            bookPagesBlock.visibility = if (model.numberOfPages.isNullOrEmpty()) View.GONE else View.VISIBLE

            bookPublisher.text = model.publisher
            bookISBN.text = model.isbn
            bookYear.text = model.year
            bookPages.text = model.numberOfPages

            model.getCoverType(context)?.let {
                bookCover.text = it
                bookCoverBlock.visibility = View.VISIBLE
            } ?: run {
                bookCoverBlock.visibility = View.GONE
            }

            bookSynopsisBlock.visibility = if (model.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            bookSynopsis.text = model.description

            bookGenre.text = getString(R.string.view_book_genre, model.genre.title)
            bookGenreDesc.text = model.genre.description

            Glide.with(this)
                .load(if (model.user.image.isEmpty()) model.user.avatarRes else model.user.image)
                .placeholder(preferences.resDefaultUser)
                .error(preferences.resDefaultUser)
                .into(userAvatar)

            userName.visibility = if (model.user.nickname.isEmpty()) View.GONE else View.VISIBLE
            userBio.visibility = if (model.user.bio == null || model.user.bio!!.isEmpty()) View.GONE else View.VISIBLE
            userName.text = model.user.nickname
            userBio.text = model.user.bio

            menu?.iterator()?.forEach {
                when (it.itemId) {
                    R.id.book_menu_edit -> it.isVisible = preferences.userId == model.user.id
                    R.id.book_menu_delete -> it.isVisible = preferences.userId == model.user.id
                }
            }
        }
    }
}