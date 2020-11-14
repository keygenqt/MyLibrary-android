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

import com.google.gson.annotations.*

data class ListData<T>(
    @SerializedName("_embedded")
    var embedded: HashMap<String, List<T>> = hashMapOf(),

    @SerializedName("_links")
    var links: HashMap<String, Link> = hashMapOf(),

    @SerializedName("page")
    var page: Page? = null
) {

    val items: List<T>
        get() {
            embedded.forEach { return it.value }
            return emptyList()
        }

    val linkFirst: String?
        get() {
            if (links.containsKey("first")) {
                return links["first"]!!.link
            }
            return null
        }

    val linkSelf: String?
        get() {
            if (links.containsKey("self")) {
                return links["self"]!!.link
            }
            return null
        }

    val linkNext: String?
        get() {
            if (links.containsKey("next")) {
                return links["next"]!!.link
            }
            return null
        }

    val linkLast: String?
        get() {
            if (links.containsKey("last")) {
                return links["last"]!!.link
            }
            return null
        }

    val linkProfile: String?
        get() {
            if (links.containsKey("profile")) {
                return links["profile"]!!.link
            }
            return null
        }
}