package com.keygenqt.mylibrary.base

import android.content.*

class BaseSharedPreferences(private val preferences: SharedPreferences) {

    var token: String?
        get() {
            return preferences.getString("token", null)
        }
        set(value) {
            preferences.edit().putString("token", value).apply()
        }

}