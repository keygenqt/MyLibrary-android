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

package com.keygenqt.mylibrary.data.models

import com.google.gson.annotations.*
import com.keygenqt.mylibrary.hal.*

class ModelBook(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("author")
    var author: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("publisher")
    var publisher: String = "",

    @SerializedName("year")
    var year: String = "",

    @SerializedName("numberOfPages")
    var numberOfPages: String = "",

    @SerializedName("coverType")
    var coverType: String = "",

    @SerializedName("image")
    var image: String = "",

    @SerializedName("_links")
    var links: HashMap<String, Link> = hashMapOf()
) {
    companion object
}