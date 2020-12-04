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
    ModelBookUser::class,
    ModelSearch::class,
    ModelListGenre::class,
], version = 45, exportSchema = false)
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
    abstract fun modelBookUserDao(): ModelBookUserDao
    abstract fun modelSearchBookDao(): ModelSearchBookDao
    abstract fun modelListGenreDao(): ModelListGenreDao
}