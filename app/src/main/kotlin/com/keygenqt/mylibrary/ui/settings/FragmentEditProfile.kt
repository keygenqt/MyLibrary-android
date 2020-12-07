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

package com.keygenqt.mylibrary.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.databinding.FragmentEditProfileBinding
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import com.keygenqt.mylibrary.ui.settings.utils.DotIndicatorPagerAdapter
import com.keygenqt.mylibrary.ui.settings.utils.ZoomOutPageTransformer
import com.keygenqt.mylibrary.utils.AVATARS
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentEditProfile : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel: ViewEditProfile by inject()

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        statusProgress(true)
        bind {
            dotsIndicator.setViewPager(viewPager.apply {
                adapter = DotIndicatorPagerAdapter(AVATARS)
                setPageTransformer(true, ZoomOutPageTransformer())
                addOnPageChangeListener(object : OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        viewModel.user?.apply {
                            avatar = "avatar_$position"
                        }
                    }
                })
            })
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(LiveDataEvent(viewModel.user?.apply {
                    nickname = textInputEditTextNickname.text.toString()
                    website = textInputEditTextWebsite.text.toString()
                    location = textInputEditTextLocation.text.toString()
                    bio = textInputEditTextBio.text.toString()
                }))
            }
        }
    }

    @OnCreateAfter fun observeUpdate() {
        bind {
            viewModel.updateUser.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    statusProgress(false)
                    clearError()
                    root.hideKeyboard()
                    body.requestFocus()
                    Toast.makeText(activity, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @OnCreateAfter fun observeUserMe() {
        bind {
            viewModel.userMe.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { model ->
                    statusProgress(viewModel.user == null)
                    viewPager.currentItem = model.avatar.replace("avatar_", "").toInt()
                    textInputEditTextNickname.setText(model.nickname)
                    textInputEditTextWebsite.setText(model.website)
                    textInputEditTextLocation.setText(model.location)
                    textInputEditTextBio.setText(model.bio)
                }
            })
        }
    }

    @OnCreateAfter fun observeError() {
        bind {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->

                    statusProgress(false)

                    clearError()

                    if (throwable is ValidateException) {
                        throwable.errors.forEach {
                            when (it.field) {
                                "nickname" ->
                                    if (textInputLayoutNickname.error.isNullOrEmpty()) {
                                        textInputLayoutNickname.error = it.defaultMessage
                                    }
                                "website" ->
                                    if (textInputLayoutWebsite.error.isNullOrEmpty()) {
                                        textInputLayoutWebsite.error = it.defaultMessage
                                    }
                                "location" ->
                                    if (textInputLayoutLocation.error.isNullOrEmpty()) {
                                        textInputLayoutLocation.error = it.defaultMessage
                                    }
                                "bio" ->
                                    if (textInputLayoutBio.error.isNullOrEmpty()) {
                                        textInputLayoutBio.error = it.defaultMessage
                                    }
                            }
                        }
                        textInputLayoutBlock.requestFocusTextInputLayoutError(scrollView)
                    }
                }
            })
        }
    }

    private fun clearError() {
        bind {
            textInputLayoutNickname.isErrorEnabled = false
            textInputLayoutWebsite.isErrorEnabled = false
            textInputLayoutLocation.isErrorEnabled = false
            textInputLayoutBio.isErrorEnabled = false
        }
    }
}