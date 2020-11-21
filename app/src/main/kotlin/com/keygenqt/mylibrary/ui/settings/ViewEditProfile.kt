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

package com.keygenqt.mylibrary.ui.settings

import androidx.lifecycle.*
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.dao.ModelUserDao
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.data.services.OtherService
import com.keygenqt.mylibrary.hal.API_KEY_MODEL_USERS
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewEditProfile(
    private val db: RoomDatabase,
    private val service: OtherService,
    private val preferences: BaseSharedPreferences
) : ViewModel() {

    var user: ModelUser? = null

    private val linkModel = db.getDao<ModelRootDao>()!!.getModel(API_VERSION).getLink(API_KEY_MODEL_USERS)

    val params: MutableLiveData<ModelUser> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()

    val userMe: LiveData<ModelUser> = liveData(BaseExceptionHandler.getExceptionHandler()) {
        emit(db.getDao<ModelUserDao>()!!.getModel())
        service.getUserMe(linkModel.link) { model ->
            emit(model)
        }
    }

    val updateUser = params.switchMap {
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            user?.let {
                service.updateUser(it.links.getValue(API_KEY_SELF).link, it) {
                    emit(true)
                }
            }
        }
    }
}