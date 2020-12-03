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
import com.google.gson.JsonObject
import com.keygenqt.mylibrary.base.BaseQuery
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.dao.ModelUserDao
import com.keygenqt.mylibrary.data.db.DbServiceBooks
import com.keygenqt.mylibrary.data.db.DbServiceOther
import com.keygenqt.mylibrary.data.models.ModelRoot
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.hal.API_KEY_JOIN
import com.keygenqt.mylibrary.hal.API_KEY_LOGIN
import com.keygenqt.mylibrary.hal.API_KEY_PASSWORD
import com.keygenqt.mylibrary.utils.API_VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceOther(
    val layer: DbServiceOther,
    private val query: BaseQuery
) {

    suspend fun getUserMe(link: String, response: suspend (ModelUser) -> Unit) {
        layer.getModelUserDao().let { dao ->
            withContext(Dispatchers.IO) {
                query.getAsync<ModelUser>(this, "$link/${layer.userId()}").await().let { model ->
                    dao.insert(model)
                    response.invoke(model)
                }
            }
        }
    }

    suspend fun getRootLinks(response: suspend (ModelRoot) -> Unit) {
        withContext(Dispatchers.IO) {
            query.getAsync<ModelRoot>(this, "/").await().let { model ->
                layer.getModelRootDao().insert(model)
                response.invoke(model)
            }
        }
    }

    suspend fun login(
        email: String,
        password: String,
        response: suspend (ModelUser) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            query.postAsync<ModelUser>(this, layer.getUrlLogin().value,
                JsonObject().apply {
                    addProperty("email", email)
                    addProperty("password", password)
                    addProperty("uid", layer.uid())
                }
            ).await().let { model ->
                layer.getModelUserDao().insert(model)
                response.invoke(model)
            }
        }
    }

    suspend fun join(
        avatar: String,
        nickname: String,
        email: String,
        password: String,
        response: suspend (ModelUser) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            query.postAsync<ModelUser>(this, layer.getUrlJoin().value,
                JsonObject().apply {
                    addProperty("avatar", avatar)
                    addProperty("nickname", nickname)
                    addProperty("email", email)
                    addProperty("password", password)
                    addProperty("uid", layer.uid())
                }
            ).await().let { model ->
                layer.getModelUserDao().insert(model)
                response.invoke(model)
            }
        }
    }

    suspend fun password(
        password: String,
        rpassword: String,
        response: suspend (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            query.postAsync<ModelUser>(this, layer.getUrlPassword().value,
                JsonObject().apply {
                    addProperty("password", password)
                    addProperty("rpassword", rpassword)
                }
            ).await().let {
                response.invoke(true)
            }
        }
    }

    suspend fun updateUser(
        link: String,
        model: ModelUser,
        response: suspend () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            query.putAsync<ModelUser>(this, link, Gson().toJsonTree(model).asJsonObject).await().let {
                layer.getModelUserDao().insert(model)
                response.invoke()
            }
        }
    }
}