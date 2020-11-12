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

package com.keygenqt.mylibrary.ui.local

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.keygenqt.mylibrary.base.BaseResponseError.Companion.getExceptionHandler
import com.keygenqt.mylibrary.data.services.BookService
import com.keygenqt.mylibrary.interfaces.ViewModelPage

class ViewLocal(private val service: BookService) : ViewModel(), ViewModelPage {

    private val linkFirstDefault = "http://192.168.1.68:8080/books"

    override val link: MutableLiveData<String?> = MutableLiveData()
    override val loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        link.postValue(null)
    }

    val listData = link.switchMap {
        liveData(getExceptionHandler()) {
            loading.postValue(true)
            service.getList(link.value ?: linkFirstDefault) { listData ->
                loading.postValue(false)
                emit(listData)
            }
        }
    }
}