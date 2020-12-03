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

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.base.BaseModel

@Entity(tableName = "ModelUser")
data class ModelUser(

    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("email")
    var email: String = "",

    @SerializedName("nickname")
    var nickname: String = "",

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("avatar")
    var avatar: String = "",

    @SerializedName("website")
    @Ignore var website: String? = null,

    @SerializedName("location")
    @Ignore var location: String? = null,

    @SerializedName("bio")
    @Ignore var bio: String? = null,

    @SerializedName("token")
    @Ignore var token: String = ""

) : BaseModel() {
    companion object {
        const val API_KEY = "users"
    }
}