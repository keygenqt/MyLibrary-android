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
import com.keygenqt.mylibrary.BuildConfig
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.response.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.ui.activities.MainActivity
import com.keygenqt.mylibrary.ui.other.FragmentLogin.PARAMS.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.android.ext.android.inject
import java.util.Locale

@ActionBarEnable
class FragmentLogin : BaseFragment(R.layout.fragment_login, R.string.fragment_login_title) {

    private val viewModel: ViewLogin by inject()

    enum class PARAMS {
        EMAIL,
        PASSWORD,
    }

    override fun onCreateView() {
        initView {
            if (BuildConfig.DEBUG) {
                textInputEditTextEmail.setText(R.string.user_email)
                textInputEditTextPassw.setText(R.string.user_passw)
            }
            buttonSubmit.setOnClickListener {
                statusProgress(true)
                viewModel.params.postValue(hashMapOf(
                    EMAIL to textInputEditTextEmail.text.toString(),
                    PASSWORD to textInputEditTextPassw.text.toString()
                ))
            }
            buttonJoin.setOnClickListener {
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentJoin())
            }
        }
    }

    @CallOnCreate fun observeLoading() {
        initView {
            viewModel.login.observe(viewLifecycleOwner) {
                viewModel.error.postValue(null)
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

                statusProgress(false)

                textInputLayoutEmail.isErrorEnabled = false
                textInputLayoutPassw.isErrorEnabled = false

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
                }
            })
        }
    }
}