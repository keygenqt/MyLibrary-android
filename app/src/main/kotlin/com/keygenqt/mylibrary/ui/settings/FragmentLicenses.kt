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
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.databinding.FragmentLicensesBinding
import com.keygenqt.mylibrary.databinding.ViewFragmentLicensesItemBinding
import com.keygenqt.mylibrary.ui.books.FragmentBooksDirections
import com.keygenqt.mylibrary.ui.other.FragmentSplashDirections
import org.json.JSONArray
import org.json.JSONObject

@ActionBarEnable
class FragmentLicenses : BaseFragment<FragmentLicensesBinding>() {

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentLicensesBinding {
        return FragmentLicensesBinding.inflate(inflater, container, false)
    }

    @UpStack
    fun stack(uri: Uri, nav: NavController) {
        Log.e("kkk", uri.toString())
        nav.navigate(FragmentSplashDirections.actionFragmentSplashToUserApp())
        nav.navigate(FragmentBooksDirections.actionFragmentBooksToFragmentSettings())
        nav.navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentAbout())
    }

    @SuppressLint("InflateParams")
    override fun onCreateView() {
        bind {
            containerLicenses.removeAllViews()

            loadJsonArray(JSONArray().apply {

                // add freevector
                put(JSONObject().apply {
                    put("libraryName", "FreeVector.com")
                    put(
                        "license",
                        "This file is distributed under the <a href='https://www.freevector.com/standard-license'>Our Standard License</a> license"
                    )
                    put("url", "https://www.freevector.com")
                    put("urlName", "www.freevector.com")
                    put("licenseUrl", "https://www.freevector.com/cute-cartoon-faces-19020")
                    put("licenseUrlName", "FreeVector.com")
                })
            })

            val body = JSONObject(root.context.resources.assets.open("licenses.json").bufferedReader().use { it.readText() })

            loadJsonArray(body.getJSONArray("libraries"))
        }
    }

    private fun loadJsonArray(licenses: JSONArray) {
        bind {
            for (i in 0 until licenses.length()) {
                val item = licenses.getJSONObject(i)
                if (item.has("libraryName") && item.has("license") && item.has("licenseUrl")) {
                    (root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                        .inflate(R.layout.view_fragment_licenses_item, null)?.let { view ->
                            if (item.has("url")) {
                                containerLicenses.addView(ViewFragmentLicensesItemBinding.bind(view).apply {
                                    licenceTitle.text = item.getString("libraryName")
                                    licenceDesc.text = Html.fromHtml(
                                        getString(
                                            R.string.licenses_desc_website,
                                            item.getString("license"),
                                            item.getString("url"),
                                            if (item.has("urlName")) item.getString("urlName") else item.getString(
                                                "url"
                                            ),
                                            item.getString("licenseUrl"),
                                            if (item.has("licenseUrlName")) item.getString("licenseUrlName") else item.getString(
                                                "licenseUrl"
                                            )
                                        ),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                    )
                                    licenceDesc.movementMethod = LinkMovementMethod.getInstance()
                                }.root)
                            } else {
                                containerLicenses.addView(ViewFragmentLicensesItemBinding.bind(view).apply {
                                    licenceDesc.text = Html.fromHtml(
                                        getString(
                                            R.string.licenses_desc,
                                            item.getString("license"),
                                            item.getString("licenseUrl"),
                                            item.getString("licenseUrl")
                                        ),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                    )
                                    licenceDesc.movementMethod = LinkMovementMethod.getInstance()
                                }.root)
                            }
                        }
                }
            }
        }
    }
}