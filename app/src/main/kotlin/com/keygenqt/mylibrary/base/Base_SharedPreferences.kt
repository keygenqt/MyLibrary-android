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

import android.content.*
import android.util.Log
import java.util.*

class BaseSharedPreferences(private val preferences: SharedPreferences) {

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

    val uid: String
        get() {
            val value = preferences.getString("uid", null)
            if (value == null) {
                preferences.edit().putString("uid", UUID.randomUUID().toString()).apply()
            }
            return preferences.getString("uid", null) ?: ""
        }

}