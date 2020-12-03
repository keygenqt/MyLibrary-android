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

package com.keygenqt.mylibrary.data.db

import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.*
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.utils.API_VERSION

class DbServiceBooks(
    val db: RoomDatabase,
    val preferences: BaseSharedPreferences
) {
    fun userId(): Long? {
        return preferences.userId
    }

    fun getModelBookDao(): ModelBookDao {
        return db.getDao()
    }
    
    fun getModelSearchDao(): ModelSearchDao {
        return db.getDao()
    }

    fun getModelBookGenreDao(): ModelBookGenreDao {
        return db.getDao()
    }

    fun getModelSearchBookDao(): ModelSearchBookDao {
        return db.getDao()
    }

    fun getRootLink(): Link {
        return db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY)
    }

    fun findSearch(): ModelSearch? {
        db.getDao<ModelSearchDao>().let { dao ->
            return dao.findModels(ModelBook.API_KEY)
        }
    }

    fun findItems(link: Link, ids: List<Long>): List<ModelBook> {
        db.getDao<ModelSearchBookDao>().let { dao ->
            return dao.findSearchModels(link.linkClearPageable.value, ids).map { it.search }
        }
    }

    fun findBookByLink(link: Link): ModelBook? {
        getModelBookDao().findModel(link.value)?.let { model ->
            return model
        }
        return null
    }
}