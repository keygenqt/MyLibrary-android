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
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.databinding.FragmentSplashBinding
import com.keygenqt.mylibrary.extensions.navigateUri
import org.koin.android.ext.android.inject
import java.net.ConnectException
import java.util.Locale

class FragmentSplash : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: ViewSplash by inject()
    private val args: FragmentSplashArgs by navArgs()
    private val preferences: BaseSharedPreferences by inject()

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {

        preferences.locale?.let {
            if (preferences.locale != Locale.getDefault().toLanguageTag()) {
                Toast.makeText(activity, getString(R.string.language_app_update), Toast.LENGTH_SHORT).show()
            }
        }

        val defaultInit = {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.links.observe(viewLifecycleOwner, {
                    viewModel.userMe.observe(viewLifecycleOwner, {
                        activity?.intent?.let { intent ->
                            if (intent.action == "android.intent.action.DEEP_LINK") {
                                // open if bg notification
                                findNavController().navigateUri(requireContext(), intent)
                            } else {
                                findNavController().navigate(FragmentSplashDirections.actionFragmentSplashToUserApp())
                            }
                        }
                    })
                })
            }, 1000)
        }

        when (val intent = activity?.intent) {
            is Intent -> {
                when {
                    !args.uri.isNullOrEmpty() -> {
                        // open if not foreground notification
                        findNavController().navigateUri(requireContext(), args.uri!!)
                    }
                    intent.hasExtra("changeTheme") -> {
                        findNavController().createDeepLink().setDestination(R.id.FragmentAppearance).createPendingIntent().send()
                    }
                    else -> {
                        defaultInit.invoke()
                    }
                }
            }
        }

        preferences.locale = Locale.getDefault().toLanguageTag()
    }

    @OnCreateAfter
    fun error() {
        bind {
            viewModel.error.observe(viewLifecycleOwner, { event ->
                event?.peekContentHandled()?.let { throwable ->
                    when (throwable) {
                        is ConnectException -> {
                            progressBarSplash.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }
}