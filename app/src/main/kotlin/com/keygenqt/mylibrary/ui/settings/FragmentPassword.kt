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
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.response.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_password.view.*
import kotlinx.android.synthetic.main.fragment_password.view.buttonSubmit
import org.koin.android.ext.android.inject

@ActionBarEnable
@FragmentTitle("Change Password")
class FragmentPassword : BaseFragment(R.layout.fragment_password) {

    private val viewModel: ViewPassword by inject()

    class RequestPassword(var password: String, var rpassword: String)

    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            buttonSubmit.setOnClickListener {
                viewModel.params.postValue(RequestPassword(
                    textInputEditTextPassword.text.toString(),
                    textInputEditTextRPassword.text.toString()
                ))
            }
        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.password.observe(viewLifecycleOwner) {
                viewModel.error.postValue(null)
                this.hideKeyboard()
                body.requestFocus()
                textInputEditTextPassword.setText("")
                textInputEditTextRPassword.setText("")
                Toast.makeText(activity, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @CallOnCreate fun observeError() {
        initView {
            viewModel.error.observe(viewLifecycleOwner, { throwable ->

                textInputLayoutPassword.error = null
                textInputLayoutRPassword.error = null

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
                }
            })
        }
    }
}