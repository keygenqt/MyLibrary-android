package com.keygenqt.mylibrary.data.converters

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter fun toList(value: String): List<String> {
        return value.split(",")
    }
}