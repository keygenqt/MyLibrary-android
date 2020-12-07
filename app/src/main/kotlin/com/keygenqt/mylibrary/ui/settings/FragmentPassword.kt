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
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.databinding.FragmentPasswordBinding
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentPassword : BaseFragment<FragmentPasswordBinding>() {

    private val viewModel: ViewPassword by inject()

    class RequestPassword(var password: String, var rpassword: String)

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentPasswordBinding {
        return FragmentPasswordBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        bind {
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(LiveDataEvent(RequestPassword(
                    textInputEditTextPassword.text.toString(),
                    textInputEditTextRPassword.text.toString()
                )))
            }
        }
    }

    @OnCreateAfter fun afterSubmit() {
        bind {
            viewModel.password.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    clearError()
                    root.hideKeyboard()
                    body.requestFocus()
                    textInputEditTextPassword.setText("")
                    textInputEditTextRPassword.setText("")
                    Toast.makeText(activity, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @OnCreateAfter fun error() {
        bind {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->
                    statusProgress(false)

                    clearError()

                    if (throwable is ValidateException) {
                        throwable.errors.forEach {
                            when (it.field) {
                                "password" ->
                                    if (textInputLayoutPassword.error.isNullOrEmpty()) {
                                        textInputLayoutPassword.error = it.defaultMessage
                                    }
                                "rpassword" ->
                                    if (textInputLayoutRPassword.error.isNullOrEmpty()) {
                                        textInputLayoutRPassword.error = it.defaultMessage
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
            textInputLayoutPassword.isErrorEnabled = false
            textInputLayoutRPassword.isErrorEnabled = false
        }
    }
}