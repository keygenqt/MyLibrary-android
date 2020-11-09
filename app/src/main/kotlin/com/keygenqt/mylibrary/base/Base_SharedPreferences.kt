package com.keygenqt.mylibrary.base

import android.content.*

class BaseSharedPreferences(private val preferences: SharedPreferences) {

    var token: String?
        get() {
            return "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJKV1QiLCJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sImlhdCI6MTYwNDkwNjcwOSwiZXhwIjoxNjA3NDk4NzA5fQ.UmhzmAFtcV8IXXfpOEmWFy0YgS9dDbny_lwW_SPpBmNK3mLXEPD1Lp4gOK0h9kq55TZPqAKhwd_WJB6izuLe5g"
            // @todo
            // return preferences.getString("token", null)
        }
        set(value) {
            preferences.edit().putString("token", value).apply()
        }

}