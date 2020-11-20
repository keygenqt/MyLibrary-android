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

package com.keygenqt.mylibrary.ui.other

import android.content.Intent
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.response.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.ui.activities.MainActivity
import com.keygenqt.mylibrary.ui.other.FragmentJoin.PARAMS.*
import com.keygenqt.mylibrary.ui.settings.utils.DotIndicatorPagerAdapter
import com.keygenqt.mylibrary.ui.settings.utils.ZoomOutPageTransformer
import com.keygenqt.mylibrary.utils.AVATARS
import kotlinx.android.synthetic.main.fragment_edit_profile.view.dotsIndicator
import kotlinx.android.synthetic.main.fragment_edit_profile.view.viewPager
import kotlinx.android.synthetic.main.fragment_join.view.*
import kotlinx.android.synthetic.main.fragment_login.view.textInputLayoutEmail
import kotlinx.android.synthetic.main.fragment_login.view.textInputLayoutPassw
import org.koin.android.ext.android.inject
import java.util.Locale

@ActionBarEnable
@FragmentTitle("Join")
class FragmentJoin : BaseFragment(R.layout.fragment_join) {

    private val viewModel: ViewJoin by inject()

    private var keyAvatar = "avatar_0"

    enum class PARAMS {
        AVATAR,
        NICKNAME,
        EMAIL,
        PASSWORD,
    }

    override fun onCreateView() {
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
                        keyAvatar = "avatar_$position"
                    }
                })
            })

            buttonSubmit.setOnClickListener {
                viewModel.params.postValue(hashMapOf(
                    AVATAR to keyAvatar,
                    NICKNAME to textInputEditTextNickname.text.toString(),
                    EMAIL to textInputEditTextEmail.text.toString(),
                    PASSWORD to textInputEditTextPassw.text.toString()
                ))
            }
        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.join.observe(viewLifecycleOwner) {
                this.hideKeyboard()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                context.startActivity(intent)
            }
        }
    }

    @CallOnCreate fun observeError() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { throwable ->
                if (throwable is ValidateException) {
                    textInputLayoutNickname.error = null
                    textInputLayoutEmail.error = null
                    textInputLayoutPassw.error = null
                    throwable.errors.forEach {
                        when (it.field) {
                            NICKNAME.name.toLowerCase(Locale.ROOT) ->
                                if (textInputLayoutNickname.error.isNullOrEmpty()) {
                                    textInputLayoutNickname.error = it.defaultMessage
                                }
                            EMAIL.name.toLowerCase(Locale.ROOT) ->
                                if (textInputLayoutEmail.error.isNullOrEmpty()) {
                                    textInputLayoutEmail.error = it.defaultMessage
                                }
                            PASSWORD.name.toLowerCase(Locale.ROOT) ->
                                if (textInputLayoutPassw.error.isNullOrEmpty()) {
                                    textInputLayoutPassw.error = it.defaultMessage
                                }
                        }
                    }
                }
            })
        }
    }
}