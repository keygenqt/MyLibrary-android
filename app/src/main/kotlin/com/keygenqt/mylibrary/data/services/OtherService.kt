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
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.dao.ModelUserDao
import com.keygenqt.mylibrary.data.models.ModelRoot
import com.keygenqt.mylibrary.data.models.ModelUser

class OtherService(
    private val api: OtherApi,
    private val db: RoomDatabase,
    private val preferences: BaseSharedPreferences
) {

    suspend fun join(
        avatar: String,
        nickname: String,
        email: String,
        passw: String,
        response: suspend (ModelUser) -> Unit
    ) {
        api.join(avatar, nickname, email, passw, preferences.uid).checkResponse { model ->
            db.getDao<ModelUserDao>()?.let {
                it.deleteAll()
                it.insert(model)
            }
            response.invoke(model)
        }
    }

    suspend fun login(
        email: String,
        passw: String,
        response: suspend (ModelUser) -> Unit
    ) {
        api.login(email, passw, preferences.uid).checkResponse { model ->
            db.getDao<ModelUserDao>()?.let {
                it.deleteAll()
                it.insert(model)
            }
            response.invoke(model)
        }
    }

    suspend fun updateUser(link: String, model: ModelUser, response: suspend () -> Unit) {
        api.updateUser(link, model).checkResponse {
            db.getDao<ModelUserDao>()?.let {
                it.deleteAll()
                it.insert(model)
            }
            response.invoke()
        }
    }

    suspend fun getRootLinks(response: suspend (ModelRoot) -> Unit) {
        api.getRootLinks().checkResponse { model ->
            db.getDao<ModelRootDao>()?.insert(model)
            response.invoke(model)
        }
    }

    suspend fun getUserMe(response: suspend (ModelUser) -> Unit) {
        api.getUserMe().checkResponse { model ->
            db.getDao<ModelUserDao>()?.let {
                it.deleteAll()
                it.insert(model)
            }
            response.invoke(model)
        }
    }
}