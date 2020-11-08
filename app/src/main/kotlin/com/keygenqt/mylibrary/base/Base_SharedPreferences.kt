package com.keygenqt.mylibrary.base

import android.content.*

class BaseSharedPreferences(private val preferences: SharedPreferences) {

    var token: String?
        get() {
            return "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJKV1QiLCJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sImlhdCI6MTYwNDgzNjM4MSwiZXhwIjoxNjA3NDI4MzgxfQ.8guzAGriGmsUCK9XRHXD5FWOXx9z-nviR-TLkILvanbNuV8uJBFNYA-U-OYC7G3V_3_STPA6B-yQ-bQWazlQbQ"
            // @todo
            // return preferences.getString("token", null)
        }
        set(value) {
            preferences.edit().putString("token", value).apply()
        }

}