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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.keygenqt.mylibrary.base.BaseExceptionHandler.Companion.getExceptionHandler
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.models.ModelRoot
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.data.services.BookApi
import com.keygenqt.mylibrary.data.services.OtherService
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class ViewSplash(
    private val db: RoomDatabase,
    private val service: OtherService,
    private val preferences: BaseSharedPreferences
) : ViewModel() {

    val userMe: LiveData<ModelUser> = liveData(getExceptionHandler()) {
        service.getUserMe { user ->
            emit(user)
        }
    }

    val links: LiveData<ModelRoot> = liveData(getExceptionHandler()) {
        service.getRootLinks { links ->
            emit(links)
        }
    }
}