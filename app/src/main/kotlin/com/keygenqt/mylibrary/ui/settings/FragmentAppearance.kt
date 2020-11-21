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

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_appearance.view.constraintLayoutItemDarkTheme
import kotlinx.android.synthetic.main.fragment_appearance.view.constraintLayoutItemGrayTheme
import kotlinx.android.synthetic.main.fragment_appearance.view.switchItemDarkTheme
import kotlinx.android.synthetic.main.fragment_appearance.view.switchItemGrayTheme
import org.koin.android.ext.android.inject

@ActionBarEnable
@FragmentTitle("Appearance")
class FragmentAppearance : BaseFragment(R.layout.fragment_appearance) {

    private val sharedPreferences: BaseSharedPreferences by inject()

    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {

            switchItemDarkTheme.isChecked = sharedPreferences.darkTheme
            constraintLayoutItemDarkTheme.setOnClickListener {
                switchItemGrayTheme.isChecked = false
                sharedPreferences.grayTheme = false
                switchItemDarkTheme.isChecked = !switchItemDarkTheme.isChecked
                sharedPreferences.darkTheme = switchItemDarkTheme.isChecked
                updateTheme()
            }

            switchItemGrayTheme.isChecked = sharedPreferences.grayTheme
            constraintLayoutItemGrayTheme.setOnClickListener {
                switchItemDarkTheme.isChecked = false
                sharedPreferences.darkTheme = false
                switchItemGrayTheme.isChecked = !switchItemGrayTheme.isChecked
                sharedPreferences.grayTheme = switchItemGrayTheme.isChecked
                updateTheme()
            }
        }
    }

    private fun updateTheme() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("changeTheme", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            requireActivity().startActivity(intent)
        }, 200)
    }
}