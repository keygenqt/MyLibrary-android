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

import com.google.gson.annotations.SerializedName

data class ListData<T>(
    @SerializedName("_embedded")
    var embedded: HashMap<String, List<T>> = hashMapOf(),

    @SerializedName("_links")
    var links: HashMap<String, Link> = hashMapOf(),

    @SerializedName("page")
    var page: Page? = null
) {

    var items: List<T>
        get() {
            embedded.forEach { return it.value }
            return emptyList()
        }
        set(value) {
            embedded[embedded.keys.first()] = value
        }

    val linkFirst: Link?
        get() {
            if (links.containsKey("first")) {
                return links["first"]
            }
            return null
        }

    val linkSelf: Link?
        get() {
            if (links.containsKey("self")) {
                return links["self"]
            }
            return null
        }

    val linkNext: Link?
        get() {
            if (links.containsKey("next")) {
                return links["next"]
            }
            return null
        }

    val linkLast: Link?
        get() {
            if (links.containsKey("last")) {
                return links["last"]
            }
            return null
        }

    val linkProfile: Link?
        get() {
            if (links.containsKey("profile")) {
                return links["profile"]
            }
            return null
        }
}