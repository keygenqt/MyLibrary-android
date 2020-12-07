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

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject

@ActionBarEnable
class FragmentSettings : BaseFragment<FragmentSettingsBinding>() {

    private val sharedPreferences: BaseSharedPreferences by inject()

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {
        bind {
            settingsBlockAppearance.setOnClickListener {
                findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentAppearance())
            }
            settingsBlockEditProfile.setOnClickListener {
                findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentEditProfile())
            }
            settingsBlockPassword.setOnClickListener {
                findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentPassword())
            }
            settingsBlockAbout.setOnClickListener {
                findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentAbout())
            }
            settingsBlockLogout.setOnClickListener {
                AlertDialog.Builder(context)
                    .setMessage("Are you sure to log out?")
                    .setPositiveButton("Yes") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        sharedPreferences.token = null
                        findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToGuestApp())
                    }
                    .setNegativeButton("No") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()

            }
        }
    }
}