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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.BuildConfig
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.base.exceptions.ValidateException
import com.keygenqt.mylibrary.databinding.FragmentLoginBinding
import com.keygenqt.mylibrary.extensions.requestFocusTextInputLayoutError
import com.keygenqt.mylibrary.ui.other.FragmentLogin.PARAMS.*
import org.koin.android.ext.android.inject
import java.util.Locale

@ActionBarEnable
class FragmentLogin : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: ViewLogin by inject()

    enum class PARAMS {
        EMAIL,
        PASSWORD,
    }

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        bind {
            if (BuildConfig.DEBUG) {
                textInputEditTextEmail.setText(R.string.user_email)
                textInputEditTextPassw.setText(R.string.user_passw)
            }
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(LiveDataEvent(hashMapOf(
                    EMAIL to textInputEditTextEmail.text.toString(),
                    PASSWORD to textInputEditTextPassw.text.toString()
                )))
            }
            buttonJoin.setOnClickListener {
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentJoin())
            }
        }
    }

    @OnCreateAfter fun afterSubmit() {
        bind {
            viewModel.login.observe(viewLifecycleOwner) { event ->
                event?.peekContentHandled()?.let {
                    clearError()
                    viewModel.error.postValue(null)
                    findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToUserApp())
                }
            }
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
        bind {
            textInputLayoutEmail.isErrorEnabled = false
            textInputLayoutPassw.isErrorEnabled = false
        }
    }
}