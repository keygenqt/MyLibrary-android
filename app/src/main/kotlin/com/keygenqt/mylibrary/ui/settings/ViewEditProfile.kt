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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.data.services.ServiceOther
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewEditProfile(private val service: ServiceOther) : ViewModel() {

    var user: ModelUser? = null

    private val linkModel = service.layer.getUrlUser()

    val params: MutableLiveData<LiveDataEvent<ModelUser?>> = MutableLiveData()
    val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()

    val userMe = liveData(BaseExceptionHandler.getExceptionHandler()) {
        service.getUserMe(linkModel.value) { model ->
            user = model
            emit(LiveDataEvent(model))
        }
    }

    val updateUser = params.switchMap { event ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            event?.peekContentHandled()?.let { model ->
                service.updateUser(model.links.getValue(API_KEY_SELF).value, model) {
                    emit(LiveDataEvent(true))
                }
            }
        }
    }
}