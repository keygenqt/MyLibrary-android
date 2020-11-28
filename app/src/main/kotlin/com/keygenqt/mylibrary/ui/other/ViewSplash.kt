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
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.data.models.ModelRoot
import com.keygenqt.mylibrary.data.models.ModelUser
import com.keygenqt.mylibrary.data.services.ServiceOther
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewSplash(private val service: ServiceOther) : ViewModel() {

    lateinit var modelRoot: ModelRoot

    val userMe: LiveData<ModelUser> = liveData(getExceptionHandler()) {
        service.getUserMe(modelRoot.links[ModelUser.API_KEY]?.value!!) { user ->
            emit(user)
        }
    }

    val links: LiveData<ModelRoot> = liveData(getExceptionHandler()) {
        service.getRootLinks { links ->
            modelRoot = links
            if (links.role == ModelRoot.API_ROLE_ANONYMOUS) {
                throw HttpException(
                    datetime = SimpleDateFormat("d MMM yyyy", Locale.US).format(Date()),
                    status = 403,
                    error = "Access is denied",
                    message = "Access is denied",
                )
            }
            emit(links)
        }
    }
}