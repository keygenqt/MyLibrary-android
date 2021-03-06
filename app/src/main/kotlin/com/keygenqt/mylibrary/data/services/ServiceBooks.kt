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

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.*
import com.google.gson.Gson
import com.keygenqt.mylibrary.BuildConfig
import com.keygenqt.mylibrary.base.BaseQuery
import com.keygenqt.mylibrary.data.db.DbServiceBooks
import com.keygenqt.mylibrary.data.hal.ListDataModelBook
import com.keygenqt.mylibrary.data.hal.ListDataModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelBookUser
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.hal.API_KEY_SEARCH
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.ResponseSuccessful
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ServiceBooks(
    val layer: DbServiceBooks,
    private val query: BaseQuery,
) {

    suspend fun uploadImage(bitmap: Bitmap, response: suspend (String) -> Unit) {
        withContext(Dispatchers.IO) {

            // to bite array
            val stream = ByteArrayOutputStream()
            bitmap.compress(PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val body = byteArray.toRequestBody("image/png".toMediaTypeOrNull(), 0, byteArray.size)
            bitmap.recycle()

            query.postRequestBody<ResponseSuccessful>(this, layer.getUploadImageLink().value, body).await().let { success ->
                if (BuildConfig.DEBUG) {
                    response.invoke(success.message.replace("https://api.mylibraryapp.com/", "http://192.168.1.68:8080/"))
                } else {
                    response.invoke(success.message)
                }
            }
        }
    }

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

    suspend fun getView(link: String, response: suspend (RelationBook) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ModelBook>(this, link).await().let { model ->
                val relationBook = RelationBook(
                    model = model,
                    genre = model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                        query.getAsync<ModelBookGenre>(this, link.value).await()
                    },
                    user = model.links[ModelBook.API_KEY_USER]?.let { link ->
                        query.getAsync<ModelBookUser>(this, link.value).await()
                    },
                )
                layer.saveBook(relationBook)
                response.invoke(relationBook)
            }
        }
    }

    suspend fun updateBook(link: String, model: ModelBook, response: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            query.putAsync<ModelBook>(this, link, Gson().toJsonTree(model).asJsonObject).await().let { model ->
                model?.let {
                    val relationBook = RelationBook(
                        model = model,
                        genre = model.links[ModelBook.API_KEY_GENRE]?.let { link ->
                            query.getAsync<ModelBookGenre>(this, link.value).await()
                        },
                        user = model.links[ModelBook.API_KEY_USER]?.let { link ->
                            query.getAsync<ModelBookUser>(this, link.value).await()
                        },
                    )
                    layer.saveBook(relationBook)
                    response.invoke()
                }
            }
        }
    }

    suspend fun addBook(model: ModelBook, response: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            query.postAsync<ModelBook>(this, layer.getRootLink().value, Gson().toJsonTree(model).asJsonObject).await().let {
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