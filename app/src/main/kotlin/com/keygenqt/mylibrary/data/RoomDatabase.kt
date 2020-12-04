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

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import java.lang.RuntimeException
import kotlin.reflect.full.declaredFunctions

class RoomDatabase(val context: Context) {
    val db: RoomConf = Room.databaseBuilder(
        context,
        RoomConf::class.java,
        "RoomDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    inline fun <reified T> getDao(): T {
        val className = T::class.qualifiedName!!
        T::class.java.annotations.firstOrNull { it !is Dao }?.let {
            db::class.declaredFunctions.forEach { method ->
                if (className == method.returnType.toString()) {
                    return method.call(db) as T
                }
            }
        }
        throw RuntimeException("Not found dao!")
    }
}