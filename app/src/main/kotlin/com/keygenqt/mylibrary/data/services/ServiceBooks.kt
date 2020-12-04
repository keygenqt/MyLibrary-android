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
import com.keygenqt.mylibrary.base.BaseQuery
import com.keygenqt.mylibrary.data.db.DbServiceBooks
import com.keygenqt.mylibrary.data.hal.ListDataModelBook
import com.keygenqt.mylibrary.data.hal.ListDataModelBookGenre
import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.hal.API_KEY_SEARCH
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.ResponseSuccessful
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceBooks(
    val layer: DbServiceBooks,
    private val query: BaseQuery
) {

    suspend fun getSearch(key: String = ModelBook.API_KEY, response: suspend (ModelSearch) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ModelSearch>(this, "$key/$API_KEY_SEARCH").await().let { model ->
                model.id = key
                layer.saveSearch(model)
                response.invoke(model)
            }
        }
    }

    suspend fun getListSearch(link: Link, response: suspend (Link?) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ListDataModelBook>(this, link.value).await().let { listData ->
                layer.saveListSearch(link, listData)
                response(listData.linkNext)
            }
        }
    }

    suspend fun getView(link: String, response: suspend (ModelBook) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ModelBook>(this, link).await().let { model ->
                model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                    model.genre = query.getAsync<ModelBookGenre>(this, link.value).await()
                }
                model.links[ModelBook.API_KEY_USER]?.let { link ->
                    model.user = query.getAsync<ModelBookUser>(this, link.value).await()
                }
                layer.saveBook(model)
                response.invoke(model)
            }
        }
    }

    suspend fun updateBook(link: String, model: ModelBook, response: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            query.putAsync<ModelUser>(this, link, Gson().toJsonTree(model).asJsonObject).await().let {
                model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                    model.genre = query.getAsync<ModelBookGenre>(this, link.value).await()
                }
                model.links[ModelBook.API_KEY_USER]?.let { link ->
                    model.user = query.getAsync<ModelBookUser>(this, link.value).await()
                }
                layer.saveBook(model)
                response.invoke()
            }
        }
    }

    suspend fun addBook(model: ModelBook, response: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            query.postAsync<ModelUser>(this, layer.getRootLink().value, Gson().toJsonTree(model).asJsonObject).await().let {
                model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                    model.genre = query.getAsync<ModelBookGenre>(this, link.value).await()
                }
                model.links[ModelBook.API_KEY_USER]?.let { link ->
                    model.user = query.getAsync<ModelBookUser>(this, link.value).await()
                }
                layer.addBook(model)
                response.invoke()
            }
        }
    }

    suspend fun getListGenres(link: Link, response: suspend (Link?) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ListDataModelBookGenre>(this, link.value).await().let { listData ->
                layer.saveListGenre(link, listData)
                response(listData.linkNext)
            }
        }
    }

    suspend fun deleteBook(link: Link, response: suspend (ResponseSuccessful) -> Unit) {
        withContext(Dispatchers.IO) {
            query.deleteAsync<ResponseSuccessful>(this, link.value).await().let {
                layer.deleteBook(link)
                response.invoke(it)
            }
        }
    }
}