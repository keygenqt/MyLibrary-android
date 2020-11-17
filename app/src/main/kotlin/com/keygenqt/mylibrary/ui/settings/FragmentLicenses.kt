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

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_licenses.view.containerLicenses
import kotlinx.android.synthetic.main.view_fragment_licenses_item.view.licenceDesc
import kotlinx.android.synthetic.main.view_fragment_licenses_item.view.licenceTitle

@ActionBarEnable
@FragmentTitle("Licenses")
class FragmentLicenses : BaseFragment(R.layout.fragment_licenses) {

    @SuppressLint("InflateParams")
    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            containerLicenses.removeAllViews()
            linkedMapOf(
                R.string.retrofit_title to R.string.retrofit_desc,
                R.string.okhttp_title to R.string.okhttp_desc,
                R.string.kotlin_title to R.string.kotlin_desc,
            ).forEach { item ->
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    .inflate(R.layout.view_fragment_licenses_item, null)?.let { view ->
                        view.licenceTitle.text = getString(item.key)
                        view.licenceDesc.text = Html.fromHtml(getString(item.value), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        view.licenceDesc.movementMethod = LinkMovementMethod.getInstance()
                        containerLicenses.addView(view)
                    }
            }
        }
    }
}