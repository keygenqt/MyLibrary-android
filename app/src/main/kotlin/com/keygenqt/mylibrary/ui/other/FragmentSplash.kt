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

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.HttpException
import org.koin.android.ext.android.inject
import java.net.ConnectException

class FragmentSplash : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: ViewSplash by inject()

    override fun onCreateView() {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.links.observe(viewLifecycleOwner, {
                findNavController().navigate(FragmentSplashDirections.actionFragmentSplashToFragmentLocal())
            })
            viewModel.error.observe(viewLifecycleOwner, { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.code == 403) {
                            findNavController().navigate(FragmentSplashDirections.actionFragmentSplashToFragmentLogin())
                        }
                    }
                }
                Log.e("TAG", throwable.message ?: "")
            })
        }, 700)
    }
}