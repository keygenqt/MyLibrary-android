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

import com.google.gson.Gson
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelBookDao
import com.keygenqt.mylibrary.data.dao.ModelBookGenreDao
import com.keygenqt.mylibrary.data.dao.ModelSearchDao
import com.keygenqt.mylibrary.data.hal.ListDataModelBook
import com.keygenqt.mylibrary.data.hal.ListDataModelBookGenre
import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.hal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceBooks(
    val db: RoomDatabase,
    val preferences: BaseSharedPreferences,
    private val query: CommonQuery
) {

    suspend fun getSearch(key: String = ModelBook.API_KEY, response: suspend (ModelSearch) -> Unit) {
        db.getDao<ModelSearchDao>().let { dao ->
            dao.getModel(key)?.let { model ->
                response.invoke(model)
            }
            withContext(Dispatchers.IO) {
                query.getAsync<ModelSearch>(this, "$key/$API_KEY_SEARCH").await().let { model ->
                    model.id = key
                    dao.insert(model)
                    response.invoke(model)
                }
            }
        }
    }

    suspend fun getListSearch(linkSearch: LinkListSearch, response: suspend (ListDataModelBook) -> Unit) {
        db.getDao<ModelBookDao>().let { dao ->
            if (linkSearch.isFirstPage()) {
                linkSearch.clear()
                response.invoke(ListDataModelBook().apply {
                    embedded = hashMapOf(ModelBook.API_KEY to dao.getAll(linkSearch.key))
                    links = hashMapOf(API_KEY_SELF to linkSearch.getLinkModel())
                })
            }
            withContext(Dispatchers.IO) {
                query.getAsync<ListDataModelBook>(this, linkSearch.getLink()).await().let { listData ->
                    // insert
                    dao.deleteAll(linkSearch.key)
                    dao.insert(listData.items.map { it.type = linkSearch.key; it }.toList())
                    response.invoke(listData.mergeItems(linkSearch) as ListDataModelBook)
                }
            }
        }
    }

    suspend fun getView(link: String, response: suspend (ModelBook?) -> Unit) {
        db.getDao<ModelBookDao>().let { dao ->
            dao.getModel(link, ModelBook.VIEW_KEY)?.let { model ->
                response.invoke(model)
            } ?: run {
                response.invoke(null)
            }
            withContext(Dispatchers.IO) {
                query.getAsync<ModelBook>(this, link).await().let { model ->
                    model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                        model.genre = query.getAsync<ModelBookGenre>(this, link.value).await()
                    }
                    model.links[ModelBook.API_KEY_USER]?.let { link ->
                        model.user = query.getAsync<ModelBookUser>(this, link.value).await()
                    }
                    dao.insert(model)
                    response.invoke(model)
                }
            }
        }
    }

    suspend fun updateBook(
        link: String,
        model: ModelBook,
        response: suspend () -> Unit
    ) {
        db.getDao<ModelBookDao>().let { dao ->
            withContext(Dispatchers.IO) {
                query.putAsync<ModelUser>(this, link, Gson().toJsonTree(model).asJsonObject).await().let {

                    // update genre
                    model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                        model.genre = query.getAsync<ModelBookGenre>(this, link.value).await()
                    }

                    dao.getAllById(model.id).forEach {
                        model.key = it.key
                        model.type = it.type
                        dao.update(model)
                    }
                    response.invoke()
                }
            }
        }
    }

    suspend fun getGenresList(linkList: LinkList, response: suspend (ListDataModelBookGenre) -> Unit) {
        db.getDao<ModelBookGenreDao>().let { dao ->
            if (linkList.isFirstPage()) {
                linkList.clear()
                response.invoke(ListDataModelBookGenre().apply {
                    embedded = hashMapOf(ModelBookGenre.API_KEY to dao.getAll())
                    links = hashMapOf(API_KEY_SELF to linkList.getLinkModel())
                })
            }
            withContext(Dispatchers.IO) {
                query.getAsync<ListDataModelBookGenre>(this, linkList.getLink()).await().let { listData ->
                    if (linkList.isFirstPage()) {
                        dao.deleteAll()
                    }
                    dao.insert(listData.items)
                    response.invoke(listData.mergeItems(linkList) as ListDataModelBookGenre)
                }
            }
        }
    }

    suspend fun deleteBook(
        link: Link?,
        response: suspend (ResponseSuccessful) -> Unit
    ) {
        link?.let {
            db.getDao<ModelBookDao>().let { dao ->
                withContext(Dispatchers.IO) {
                    query.deleteAsync<ResponseSuccessful>(this, link.value).await().let {
                        dao.getAllByLink(link.value).forEach { model ->
                            model.enabled = false
                            dao.update(model)
                        }
                        response.invoke(it)
                    }
                }
            }
        }
    }
}