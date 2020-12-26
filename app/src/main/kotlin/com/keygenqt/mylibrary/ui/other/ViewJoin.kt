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

package com.keygenqt.mylibrary.ui.other

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.base.BaseFirebaseMessaging
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.data.services.ServiceOther
import com.keygenqt.mylibrary.ui.other.FragmentJoin.*
import com.keygenqt.mylibrary.ui.other.FragmentJoin.PARAMS.*

class ViewJoin(private val service: ServiceOther) : ViewModel() {

    val params: MutableLiveData<LiveDataEvent<HashMap<PARAMS, String>>> = MutableLiveData()
    val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()

    val join = params.switchMap { event ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            event?.peekContentHandled()?.let {
                service.join(it[AVATAR]!!, it[NICKNAME]!!, it[EMAIL]!!, it[PASSWORD]!!) { model ->
                    service.layer.setBaseUserData(model.id, model.token)
                    service.getRootLinks { links ->
                        service.getUserMe(links.links[ModelUser.API_KEY]?.value!!) {
                            registerMessage()
                            emit(LiveDataEvent(model))
                        }
                    }
                }
            }
        }
    }

    private fun registerMessage() {
        BaseFirebaseMessaging.getToken { token ->
            service.registerMessage(token) {
                Log.d("ViewLogin", "Refreshed token: $token")
            }
        }
    }
}