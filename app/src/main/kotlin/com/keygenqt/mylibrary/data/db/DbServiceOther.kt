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
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.dao.ModelUserDao
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.hal.*
import com.keygenqt.mylibrary.utils.API_VERSION

class DbServiceOther(
    val db: RoomDatabase,
    val preferences: BaseSharedPreferences,
) {
    fun userId(): Long? {
        return preferences.userId
    }

    fun uid(): String {
        return preferences.uid
    }

    fun setBaseUserData(id: Long, token: String) {
        preferences.userId = id
        preferences.token = token
    }

    fun getModelUserDao(): ModelUserDao {
        return db.getDao()
    }

    fun getModelRootDao(): ModelRootDao {
        return db.getDao()
    }

    fun getUrlMessageToken(): Link {
        return getModelRootDao().findModel(API_VERSION).getLink(API_KEY_MESSAGE_TOKEN)
    }

    fun getUrlUser(): Link {
        return getModelRootDao().findModel(API_VERSION).getLink(ModelUser.API_KEY)
    }

    fun getUrlLogin(): Link {
        return getModelRootDao().findModel(API_VERSION).getLink(API_KEY_LOGIN)
    }

    fun getUrlJoin(): Link {
        return getModelRootDao().findModel(API_VERSION).getLink(API_KEY_JOIN)
    }

    fun getUrlPassword(): Link {
        return getModelRootDao().findModel(API_VERSION).getLink(API_KEY_PASSWORD)
    }

    fun getUserMe(): ModelUser? {
        db.getDao<ModelUserDao>().let { dao ->
            preferences.userId?.let { userId ->
                dao.findModel(userId).let { model ->
                    return model
                }
            }
        }
        return null
    }
}