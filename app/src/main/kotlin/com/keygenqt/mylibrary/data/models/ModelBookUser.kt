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
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.base.BaseModel

data class ModelBookUser(

    @PrimaryKey
    @SerializedName("id")
    var id: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("nickname")
    var nickname: String = "",

    @SerializedName("image")
    var image: String = "",

    @SerializedName("website")
    var website: String = "",

    @SerializedName("location")
    var location: String = "",

    @SerializedName("bio")
    var bio: String = "",

    @SerializedName("avatar")
    var avatar: String = "",

    ) : BaseModel() {
    companion object {
        const val API_KEY = "user"
    }
}