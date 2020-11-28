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
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.SpawnAnimation
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.data.models.ModelBook
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

    private lateinit var menu: Menu
    private lateinit var book: ModelBook

    override fun onCreateView() {
        initView {

            viewModel.selfLink.postValue(args.selfLink)

            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                viewModel.selfLink.postValue(args.selfLink)
            }
            appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
                refresh.isEnabled = verticalOffset == 0
                refresh.isRefreshing = false
            })
            buttonMessage.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${book.user.email}")
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_feedback_subject))
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                }
                startActivity(Intent.createChooser(emailIntent, "Send from MyLibrary"))
            }
        }
    }

    @CallOnCreate fun observeListData() {
        viewModel.loading.observe(viewLifecycleOwner, { status ->
            initView {
                statusProgressPage(status)
                refresh.isRefreshing = false
            }
        })
        viewModel.throwable.observe(viewLifecycleOwner, { throwable ->
            when (throwable) {
                is HttpException -> {
                    if (throwable.status == 404 || throwable.status == 400) {
                        Toast.makeText(activity, getString(R.string.view_book_get_error), Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            }
        })
        viewModel.data.observe(viewLifecycleOwner) { model ->
            book = model!!
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

                bookPublisherBlock.visibility = if (model.publisher.isEmpty()) View.GONE else View.VISIBLE
                bookISBNBlock.visibility = if (model.isbn.isEmpty()) View.GONE else View.VISIBLE
                bookYearBlock.visibility = if (model.year == "0") View.GONE else View.VISIBLE
                bookPagesBlock.visibility = if (model.numberOfPages == "0") View.GONE else View.VISIBLE

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

                bookSynopsisTitle.visibility = if (model.description.isEmpty()) View.GONE else View.VISIBLE
                bookSynopsis.visibility = if (model.description.isEmpty()) View.GONE else View.VISIBLE
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

                menu.iterator().forEach {
                    when (it.itemId) {
                        R.id.book_menu_edit -> it.isVisible = preferences.userId == model.user.id
                        R.id.book_menu_delete -> it.isVisible = preferences.userId == model.user.id
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu_book_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.book_menu_edit -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.book_menu_delete -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.book_menu_share -> {
                Toast.makeText(activity, R.string.page_coming_soon, Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}