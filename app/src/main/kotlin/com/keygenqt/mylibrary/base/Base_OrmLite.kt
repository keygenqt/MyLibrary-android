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

package com.keygenqt.mylibrary.base

import android.content.*
import android.database.sqlite.*
import com.j256.ormlite.support.*
import com.keygenqt.db.*

/**
 * BaseOrmLite class for initialization and migration in OrmLite
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
class BaseOrmLite(context: Context, dbName: String, dbVersion: Int) :
    OrmliteBase(context, dbName, dbVersion) {

    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }
}