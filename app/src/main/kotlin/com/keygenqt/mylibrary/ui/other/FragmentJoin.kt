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
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.response.ValidateException
import com.keygenqt.mylibrary.extensions.hideKeyboard
import com.keygenqt.mylibrary.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_join.view.*
import org.koin.android.ext.android.inject

@ActionBarEnable
@FragmentTitle("Join")
class FragmentJoin : BaseFragment(R.layout.fragment_join) {

    private val viewModel: ViewJoin by inject()

    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            viewModel.join.observe(viewLifecycleOwner) {
                this.hideKeyboard()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                context.startActivity(intent)
            }
            viewModel.error.observe(viewLifecycleOwner, { throwable ->
                if (throwable is ValidateException) {
                    textInputLayoutNickname.error = null
                    textInputLayoutEmail.error = null
                    textInputLayoutPassw.error = null
                    throwable.errors.forEach {
                        when (it.field) {
                            "nickname" -> if (textInputLayoutNickname.error.isNullOrEmpty()) {
                                textInputLayoutNickname.error = it.defaultMessage
                            }
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
                    "nickname" to textInputEditTextNickname.text.toString(),
                    "email" to textInputEditTextEmail.text.toString(),
                    "passw" to textInputEditTextPassw.text.toString()
                ))
            }
        }
    }
}