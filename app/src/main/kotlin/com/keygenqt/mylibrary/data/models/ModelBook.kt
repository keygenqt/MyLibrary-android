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

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseModel

@Entity(tableName = "ModelBook")
data class ModelBook(

    @PrimaryKey
    var key: String = "",

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

    @SerializedName("isbn")
    var isbn: String = "",

    @SerializedName("sale")
    var sale: Boolean = false,

    @SerializedName("coverType")
    var coverType: String = "",

    @SerializedName("image")
    var image: String = ""

) : BaseModel() {

    var type: String = ""
        set(value) {
            key = "$id-$value"
            field = value
        }

    companion object {
        const val API_KEY = "books"

        const val COVER_TYPE_SOFT = "soft"
        const val COVER_TYPE_SOLID = "solid"
        const val COVER_TYPE_UNKNOWN = "unknown"
    }

    fun getCoverType(context: Context): String? {
        return when (coverType) {
            COVER_TYPE_SOFT -> context.getString(R.string.view_book_type_soft)
            COVER_TYPE_SOLID -> context.getString(R.string.view_book_type_solid)
            else -> null
        }
    }
}