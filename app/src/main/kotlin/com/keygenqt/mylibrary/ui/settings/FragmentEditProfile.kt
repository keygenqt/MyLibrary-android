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

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.response.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.ui.settings.utils.DotIndicatorPagerAdapter
import com.keygenqt.mylibrary.ui.settings.utils.ZoomOutPageTransformer
import com.keygenqt.mylibrary.utils.AVATARS
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
@FragmentTitle("Edit Profile")
class FragmentEditProfile : BaseFragment(R.layout.fragment_edit_profile) {

    private val viewModel: ViewEditProfile by inject()

    override fun onCreateView() {
        statusProgress(true)
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            dotsIndicator.setViewPager(viewPager.apply {
                adapter = DotIndicatorPagerAdapter(AVATARS)
                setPageTransformer(true, ZoomOutPageTransformer())
                addOnPageChangeListener(object : OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        viewModel.user!!.avatar = "avatar_$position"
                    }
                })
            })
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(viewModel.user?.apply {
                    nickname = textInputEditTextNickname.text.toString()
                    website = textInputEditTextWebsite.text.toString()
                    location = textInputEditTextLocation.text.toString()
                    bio = textInputEditTextBio.text.toString()
                })
            }
        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.updateUser.observe(viewLifecycleOwner) {
                statusProgress(false)
                viewModel.error.postValue(null)
                this.hideKeyboard()
                body.requestFocus()
                Toast.makeText(activity, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @CallOnCreate fun observeUserMe() {
        initView {
            viewModel.userMe.observe(viewLifecycleOwner, { model ->
                statusProgress(viewModel.user == null)
                viewModel.user = model
                viewPager.currentItem = model.avatar.replace("avatar_", "").toInt()
                textInputEditTextNickname.setText(model.nickname)
                textInputEditTextWebsite.setText(model.website)
                textInputEditTextLocation.setText(model.location)
                textInputEditTextBio.setText(model.bio)
            })
        }
    }

    @CallOnCreate fun observeError() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { throwable ->

                statusProgress(false)

                textInputLayoutNickname.isErrorEnabled = false
                textInputLayoutWebsite.isErrorEnabled = false
                textInputLayoutLocation.isErrorEnabled = false
                textInputLayoutBio.isErrorEnabled = false

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
                }
            })
        }
    }
}