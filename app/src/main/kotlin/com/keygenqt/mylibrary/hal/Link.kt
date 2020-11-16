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

data class Link(
    @SerializedName("href")
    private var href: String = ""
) {
    val link: String
        get() {
            return href.substringBefore("{")
        }

    val params: List<String>
        get() {
            return href.substringAfter("{").replace("}", "").replace("?", "").split(",")
        }

    fun linkWithParams(params: HashMap<String, String>): String {
        val uri = Uri.parse(link).buildUpon()
        this.params.forEach {
            if (params.containsKey(it)) {
                uri.appendQueryParameter(it, params[it])
            }
        }
        return uri.toString()
    }
}