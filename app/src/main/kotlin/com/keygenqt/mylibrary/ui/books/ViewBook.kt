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

package com.keygenqt.mylibrary.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.data.services.ServiceBooks
import kotlinx.coroutines.delay

class ViewBook(private val service: ServiceBooks) : ViewModel() {

    val selfLink: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val data = selfLink.switchMap { link ->
        liveData(BaseExceptionHandler.getExceptionHandler()) {
            service.getView(link) { model ->
                model?.let {
                    emit(model)
                    delay(1200)
                    loading.postValue(false)
                } ?: run {
                    loading.postValue(true)
                }
            }
        }
    }
}