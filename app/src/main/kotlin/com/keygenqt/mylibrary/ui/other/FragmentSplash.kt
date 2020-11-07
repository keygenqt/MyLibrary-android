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

import android.os.*
import androidx.navigation.fragment.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.*

class FragmentSplash : BaseFragment(R.layout.fragment_splash) {

    override fun isActionBar(): Boolean {
        return false
    }

    override fun onCreateView() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(FragmentSplashDirections.actionFragmentSplashToFragmentLocal())
        }, 1000)
    }
}