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

import android.content.SharedPreferences
import com.keygenqt.mylibrary.R
import java.util.UUID

class BaseSharedPreferences(private val preferences: SharedPreferences) {

    var locale: String?
        get() {
            return preferences.getString("locale", null)
        }
        set(value) {
            if (value == null) {
                preferences.edit().remove("locale").apply()
            } else {
                preferences.edit().putString("locale", value).apply()
            }
        }

    var userId: Long?
        get() {
            val value = preferences.getLong("userId", 0)
            return if (value == 0L) null else value
        }
        set(value) {
            if (value == null) {
                preferences.edit().remove("userId").apply()
            } else {
                preferences.edit().putLong("userId", value).apply()
            }
        }

    var token: String?
        get() {
            return preferences.getString("token", null)
        }
        set(value) {
            if (value == null) {
                preferences.edit().remove("token").apply()
            } else {
                preferences.edit().putString("token", value).apply()
            }
        }

    var tokenMessage: String?
        get() {
            return preferences.getString("tokenMessage", null)
        }
        set(value) {
            if (value == null) {
                preferences.edit().remove("tokenMessage").apply()
            } else {
                preferences.edit().putString("tokenMessage", value).apply()
            }
        }

    var darkTheme: Boolean
        get() {
            return preferences.getBoolean("darkTheme", false)
        }
        set(value) {
            preferences.edit().putBoolean("darkTheme", value).apply()
        }

    var grayTheme: Boolean
        get() {
            return preferences.getBoolean("grayTheme", false)
        }
        set(value) {
            preferences.edit().putBoolean("grayTheme", value).apply()
        }

    val uid: String
        get() {
            val value = preferences.getString("uid", null)
            if (value == null) {
                preferences.edit().putString("uid", UUID.randomUUID().toString()).apply()
            }
            return preferences.getString("uid", null) ?: ""
        }

    val resDefaultBook: Int
        get() {
            return if (darkTheme || grayTheme) R.drawable.img_default_book_dark else R.drawable.img_default_book
        }

    val resDefaultUser: Int
        get() {
            return if (darkTheme || grayTheme) R.drawable.img_default_user_dark else R.drawable.img_default_user
        }

}