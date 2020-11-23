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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.BuildConfig
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.view.settingsBlockFeedback
import kotlinx.android.synthetic.main.fragment_about.view.settingsBlockLicenses
import kotlinx.android.synthetic.main.fragment_about.view.settingsBlockRate
import kotlinx.android.synthetic.main.fragment_about.view.settingsTextVersion

@ActionBarEnable
class FragmentAbout : BaseFragment(R.layout.fragment_about) {

    override fun onCreateView() {
        initView {

            settingsTextVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)

            settingsBlockFeedback.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:dev@keygenqt.com")
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_feedback_subject))
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                }
                startActivity(Intent.createChooser(emailIntent, "Send feedback"))
            }
            settingsBlockRate.setOnClickListener { v ->
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${v.context.packageName}")))
                } catch (ex: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${v.context.packageName}")))
                }
            }
            settingsBlockLicenses.setOnClickListener {
                findNavController().navigate(FragmentAboutDirections.actionFragmentAboutToFragmentLicenses())
            }
        }
    }
}