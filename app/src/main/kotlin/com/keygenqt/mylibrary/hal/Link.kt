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

package com.keygenqt.mylibrary.hal

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.*

data class Link(
    @SerializedName("href")
    var href: String = ""
) {
    val type: String
        get() {
            return if (linkClearPageable.value.contains("$API_KEY_SEARCH/")) {
                linkClearPageable.value.substringAfterLast("$API_KEY_SEARCH/")
            } else {
                "root"
            }
        }

    val value: String
        get() {
            return href.substringBefore("{")
        }

    val linkClear: Link
        get() {
            val uri = Uri.parse(value)
            val uriClear = uri.buildUpon().clearQuery()
            return Link(uriClear.toString())
        }

    val linkClearPageable: Link
        get() {
            val uri = Uri.parse(value)
            val uriClear = uri.buildUpon().clearQuery()
            uri.queryParameterNames.forEach {
                if (it != "page" && it != "size") {
                    uriClear.appendQueryParameter(it, uri.getQueryParameter(it))
                }
            }
            return Link(uriClear.toString())
        }

    val params: List<String>
        get() {
            return href.substringAfter("{").replace("}", "").replace("?", "").split(",")
        }

    fun linkWithParams(params: HashMap<String, out String?>): Link {
        val uri = Uri.parse(value).buildUpon()
        params.forEach {
            uri.appendQueryParameter(it.key, it.value)
        }
        return Link(uri.toString())
    }

    fun isSearch(): Boolean {
        return href.contains(API_KEY_SEARCH)
    }

    fun isFirstPage(): Boolean {
        return !href.contains("""page=[1-9]+""".toRegex())
    }

    fun isPage(page: Int): Boolean {
        return !href.contains("page=$page")
    }
}