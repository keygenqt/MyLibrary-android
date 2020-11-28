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

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseModel

data class ModelBookUser(

    @PrimaryKey
    @SerializedName("id")
    var id: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("nickname")
    var nickname: String = "",

    @SerializedName("website")
    var website: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("bio")
    var bio: String? = null,

    @SerializedName("image")
    var image: String = "",

    @SerializedName("avatar")
    var avatar: String = ""

) : BaseModel() {

    val avatarRes: Int
        get() {
            when (avatar) {
                AVATAR_HAPPY -> return R.drawable.avatar_0
                AVATAR_SURPRISED -> return R.drawable.avatar_1
                AVATAR_TIRED -> return R.drawable.avatar_2
                AVATAR_UPSET -> return R.drawable.avatar_3
                AVATAR_OVERWHELMED -> return R.drawable.avatar_4
                AVATAR_DEER -> return R.drawable.avatar_5
                AVATAR_ENAMORED -> return R.drawable.avatar_6
                AVATAR_BIRDIE -> return R.drawable.avatar_7
                AVATAR_WHAT -> return R.drawable.avatar_8
                AVATAR_SHOCKED -> return R.drawable.avatar_9
                AVATAR_TOUCHED -> return R.drawable.avatar_10
                AVATAR_ANGRY -> return R.drawable.avatar_11
                AVATAR_ZOMBIE -> return R.drawable.avatar_12
                AVATAR_PLAYFUL -> return R.drawable.avatar_13
                AVATAR_SLEEPY -> return R.drawable.avatar_14
            }
            return 0
        }

    companion object {
        const val API_KEY = "user"

        const val AVATAR_HAPPY = "avatar_0"
        const val AVATAR_SURPRISED = "avatar_1"
        const val AVATAR_TIRED = "avatar_2"
        const val AVATAR_UPSET = "avatar_3"
        const val AVATAR_OVERWHELMED = "avatar_4"
        const val AVATAR_DEER = "avatar_5"
        const val AVATAR_ENAMORED = "avatar_6"
        const val AVATAR_BIRDIE = "avatar_7"
        const val AVATAR_WHAT = "avatar_8"
        const val AVATAR_SHOCKED = "avatar_9"
        const val AVATAR_TOUCHED = "avatar_10"
        const val AVATAR_ANGRY = "avatar_11"
        const val AVATAR_ZOMBIE = "avatar_12"
        const val AVATAR_PLAYFUL = "avatar_13"
        const val AVATAR_SLEEPY = "avatar_14"
    }
}