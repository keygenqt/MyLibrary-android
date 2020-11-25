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

package com.keygenqt.mylibrary.data.services

import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.response.CheckResponse.Companion.checkResponse
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelBookDao
import com.keygenqt.mylibrary.data.dao.ModelSearchDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.hal.API_KEY_MODEL_BOOK
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.ListData

class BookService(
    private val api: BookApi,
    private val db: RoomDatabase,
    private val preferences: BaseSharedPreferences
) {
    suspend fun getList(link: Link?, response: suspend (ListData<ModelBook>) -> Unit) {
        link?.let {
            api.getList(it.link).checkResponse { data ->
                db.getDao<ModelBookDao>().let { dao ->
                    if (it.isFirstPage()) {
                        dao.deleteAll()
                    }
                    data.items.forEach { model -> dao.insert(model) }
                    data.items = dao.getAll()
                    response.invoke(data)
                }
            }
        }
    }

    suspend fun getView(link: String, response: suspend (ModelBook) -> Unit) {
        api.getView(link).checkResponse { model ->
            db.getDao<ModelBookDao>().insert(model)
            response.invoke(model)
        }
    }

    suspend fun getSearch(id: String = API_KEY_MODEL_BOOK, response: suspend (ModelSearch) -> Unit = {}) {
        api.getSearch(id).checkResponse { model ->
            model.id = id
            db.getDao<ModelSearchDao>().insert(model)
            response.invoke(model)
        }
    }
}