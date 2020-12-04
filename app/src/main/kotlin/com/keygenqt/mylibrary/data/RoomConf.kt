package com.keygenqt.mylibrary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keygenqt.mylibrary.data.converters.ListConverter
import com.keygenqt.mylibrary.data.converters.MapConverter
import com.keygenqt.mylibrary.data.dao.*
import com.keygenqt.mylibrary.data.models.*

@Database(entities = [
    ModelRoot::class,
    ModelUser::class,
    ModelBook::class,
    ModelSearchBook::class,
    ModelBookGenre::class,
    ModelSearch::class,
], version = 40, exportSchema = false)
@TypeConverters(value = [
    ListConverter::class,
    MapConverter::class,
])
abstract class RoomConf : RoomDatabase() {
    abstract fun modelRootDao(): ModelRootDao
    abstract fun modelUserDao(): ModelUserDao
    abstract fun modelBookDao(): ModelBookDao
    abstract fun modelSearchDao(): ModelSearchDao
    abstract fun modelBookGenreDao(): ModelBookGenreDao
    abstract fun modelSearchBookDao(): ModelSearchBookDao
}