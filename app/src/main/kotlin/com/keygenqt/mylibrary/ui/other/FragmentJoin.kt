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

import android.widget.ScrollView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import com.keygenqt.mylibrary.ui.other.FragmentJoin.PARAMS.*
import com.keygenqt.mylibrary.ui.settings.utils.DotIndicatorPagerAdapter
import com.keygenqt.mylibrary.ui.settings.utils.ZoomOutPageTransformer
import com.keygenqt.mylibrary.utils.AVATARS
import kotlinx.android.synthetic.main.fragment_join.view.*
import org.koin.android.ext.android.inject
import java.util.Locale

@ActionBarEnable
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
                statusProgress(true)
                viewModel.params.postValue(LiveDataEvent(hashMapOf(
                    AVATAR to keyAvatar,
                    NICKNAME to textInputEditTextNickname.text.toString(),
                    EMAIL to textInputEditTextEmail.text.toString(),
                    PASSWORD to textInputEditTextPassw.text.toString()
                )))
            }
        }
    }

    @InitObserve fun afterSubmit() {
        initView {
            viewModel.join.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    clearError()
                    findNavController().navigate(FragmentJoinDirections.actionFragmentJoinToUserApp())
                }
            }
        }
    }

    @InitObserve fun validate() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->
                    statusProgress(false)

                    clearError()

                    if (throwable is ValidateException) {
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
                        textInputLayoutBlock.requestFocusTextInputLayoutError(scrollView)
                    }
                }
            })
        }
    }

    private fun clearError() {
        initView {
            textInputLayoutNickname.isErrorEnabled = false
            textInputLayoutEmail.isErrorEnabled = false
            textInputLayoutPassw.isErrorEnabled = false
        }
    }
}