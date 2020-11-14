package com.keygenqt.mylibrary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keygenqt.mylibrary.data.converters.ListConverter
import com.keygenqt.mylibrary.data.converters.MapConverter
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelRoot

@Database(entities = [
    ModelRoot::class
], version = 1, exportSchema = false)
@TypeConverters(value = [
    ListConverter::class,
    MapConverter::class,
])
abstract class RoomConf : RoomDatabase() {
    abstract fun modelRootDao(): ModelRootDao
}