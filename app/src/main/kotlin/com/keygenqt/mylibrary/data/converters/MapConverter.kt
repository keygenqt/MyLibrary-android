package com.keygenqt.mylibrary.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keygenqt.mylibrary.hal.Link

class MapConverter {
    @TypeConverter fun fromLinks(value: Map<String, Link>): String {
        return Gson().toJson(value)
    }

    @TypeConverter fun toLinks(value: String): Map<String, Link> {
        return Gson().fromJson(value, object : TypeToken<Map<String, Link>>() {}.type)
    }
}