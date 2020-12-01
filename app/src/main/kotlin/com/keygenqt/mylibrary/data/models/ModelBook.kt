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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseModel
import com.keygenqt.mylibrary.hal.API_KEY_SELF

@Entity(tableName = "ModelBook")
data class ModelBook(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("author")
    var author: String? = null,

    @SerializedName("publisher")
    var publisher: String? = null,

    @SerializedName("isbn")
    var isbn: String? = null,

    @SerializedName("year")
    var year: String? = null,

    @SerializedName("numberOfPages")
    var numberOfPages: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("sale")
    var sale: Boolean = false,

    @SerializedName("coverType")
    var coverType: String = "",

    @SerializedName("enabled")
    var enabled: Boolean = true,

    @Embedded(prefix = "genre_")
    var genre: ModelBookGenre = ModelBookGenre(),

    @Embedded(prefix = "user_")
    var user: ModelBookUser = ModelBookUser(),

    @Ignore
    var genreId: String? = null

) : BaseModel() {

    @PrimaryKey
    var key: String = ""
        get() {
            if (field.isEmpty()) {
                return "$id-$type"
            }
            return field
        }

    var selfLink: String = ""
        get() {
            if (field.isEmpty() && links.containsKey(API_KEY_SELF)) {
                return links.getValue(API_KEY_SELF).value
            }
            return field
        }

    var type: String = VIEW_KEY
        set(value) {
            if (key.isEmpty()) {
                key = "$id-$value"
            }
            field = value
        }

    companion object {
        const val API_KEY = "books"
        const val API_KEY_GENRE = "genre"
        const val API_KEY_USER = "user"

        const val VIEW_KEY = "view"

        const val COVER_TYPE_SOFT = "Soft"
        const val COVER_TYPE_SOLID = "Solid"
        const val COVER_TYPE_OTHER = "Other"
    }

    fun getCoverType(context: Context): String? {
        return when (coverType) {
            COVER_TYPE_SOFT -> context.getString(R.string.view_book_type_soft)
            COVER_TYPE_SOLID -> context.getString(R.string.view_book_type_solid)
            else -> null
        }
    }
}