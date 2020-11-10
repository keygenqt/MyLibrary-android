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

class BaseSharedPreferences(private val preferences: SharedPreferences) {

    var token: String?
        get() {
            return "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJKV1QiLCJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sImlhdCI6MTYwNDk0NjcyMywiZXhwIjoxNjA3NTM4NzIzfQ.ns5cVjGJjtx-9ZtxbuddSuNmJxI4hrZ7c6wCIN1GnilJekMXWt7Nv0HJW48RBZmK3F6ZP4dBTsQiDJcSEWyHhg"
            // @todo
            // return preferences.getString("token", null)
        }
        set(value) {
            preferences.edit().putString("token", value).apply()
        }

}