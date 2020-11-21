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
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.services.OtherService

class ViewPassword(
    private val db: RoomDatabase,
    private val service: OtherService,
    private val preferences: BaseSharedPreferences
) : ViewModel() {

    val params: MutableLiveData<FragmentPassword.RequestPassword> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()

    val password = params.switchMap {
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            service.password(it) { model ->
                emit(model)
            }
        }
    }
}