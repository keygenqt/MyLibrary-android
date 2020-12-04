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

package com.keygenqt.mylibrary.base

import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.hal.Link

open class BaseModel {

    @SerializedName("_links")
    var links: Map<String, Link> = hashMapOf()

    open fun baseId(): Long {
        return 0
    }

    fun getLink(key: String): Link {
        if (links.containsKey(key)) {
            return links.getValue(key)
        }
        throw RuntimeException("API key $key not found")
    }

    var selfLink: String = ""
        get() {
            if (field.isEmpty() && links.containsKey(API_KEY_SELF)) {
                return links.getValue(API_KEY_SELF).value
            }
            return field
        }
}