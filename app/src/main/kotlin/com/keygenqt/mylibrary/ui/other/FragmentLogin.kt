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

import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
@FragmentTitle("Login")
class FragmentLogin : BaseFragment(R.layout.fragment_login) {

    private val viewModel: ViewLogin by inject()

    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { activity?.onBackPressed() }
        }
        initView {
            viewModel.login.observe(viewLifecycleOwner) {
                this.hideKeyboard()
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentLocal())
            }
            viewModel.error.observe(viewLifecycleOwner, { throwable ->
                if (throwable is ValidateException) {
                    textInputLayoutEmail.error = null
                    textInputLayoutPassw.error = null
                    throwable.errors.forEach {
                        when (it.field) {
                            "email" -> if (textInputLayoutEmail.error.isNullOrEmpty()) {
                                textInputLayoutEmail.error = it.defaultMessage
                            }
                            "password" -> if (textInputLayoutPassw.error.isNullOrEmpty()) {
                                textInputLayoutPassw.error = it.defaultMessage
                            }
                        }
                    }
                }
            })
            buttonSubmit.setOnClickListener {
                viewModel.params.postValue(hashMapOf(
                    "email" to textInputEditTextEmail.text.toString(),
                    "passw" to textInputEditTextPassw.text.toString()
                ))
            }
            buttonJoin.setOnClickListener {
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentJoin())
            }
        }
    }
}