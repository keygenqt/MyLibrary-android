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