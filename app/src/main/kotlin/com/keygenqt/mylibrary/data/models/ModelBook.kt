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
import com.keygenqt.mylibrary.BuildConfig
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseModel
import com.keygenqt.mylibrary.hal.API_KEY_SELF

@Entity(tableName = "ModelBook")
data class ModelBook(

    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0,

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

    @SerializedName("genreId")
    var genreId: Long = 0,

    @SerializedName("userId")
    var userId: Long = 0

) : BaseModel() {

    @SerializedName("image")
    var image: String? = null
    get() {
        // temporary
        if (BuildConfig.DEBUG) {
            return field?.replace("https://mylibraryapp.com/", "http://192.168.1.68:8080/")
        }
        return field
    }

    override fun baseId(): Long {
        return id
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