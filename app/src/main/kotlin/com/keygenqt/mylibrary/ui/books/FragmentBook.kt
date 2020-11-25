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

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import kotlinx.android.synthetic.main.common_fragment_list.view.refresh
import kotlinx.android.synthetic.main.fragment_book.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentBook : BaseFragment(R.layout.fragment_book) {

    private val sharedPreferences: BaseSharedPreferences by inject()
    private val args: FragmentBookArgs by navArgs()
    private val viewModel: ViewBook by inject()

    override fun onCreateView() {
        initView {

            viewModel.userId.postValue(args.modelId)

            refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            refresh.setOnRefreshListener {
                viewModel.userId.postValue(args.modelId)
            }
            appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
                refresh.isEnabled = verticalOffset == 0
                refresh.isRefreshing = false
            })
            buttonMessage.setOnClickListener {
                Log.e("TAG", "button_message")
            }

        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.loading.observe(viewLifecycleOwner, { status ->
                refresh.isRefreshing = status
            })
        }
    }

    @CallOnCreate fun observeListData() {
        initView {
            viewModel.data.observe(viewLifecycleOwner) { model ->
                model?.let {
                    Glide.with(this)
                        .load(model.image)
                        .placeholder(if (sharedPreferences.darkTheme) R.drawable.img_default_book_dark else R.drawable.img_default_book)
                        .error(if (sharedPreferences.darkTheme) R.drawable.img_default_book_dark else R.drawable.img_default_book)
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

                    bookSynopsis.text = model.description
                    bookGenre.text = "Christian Self Help | Military History"

                    userAvatar
                    userName.text = "Nickname"
                    userBio.text =
                        "A nickname is a substitute for the proper name of a familiar person, place or thing. Commonly used to express affection, it is a form of endearment and amusement. In rarer cases, it can also be used to express defamation of character, particularly by school bullies."

                } ?: run {
                    Toast.makeText(activity, getString(R.string.view_book_get_error), Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_book_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.book_menu_edit -> {
                Log.e("TAG", "book_menu_edit")
                return true
            }
            R.id.book_menu_delete -> {
                Log.e("TAG", "book_menu_delete")
                return true
            }
            R.id.book_menu_share -> {
                Log.e("TAG", "book_menu_share")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}