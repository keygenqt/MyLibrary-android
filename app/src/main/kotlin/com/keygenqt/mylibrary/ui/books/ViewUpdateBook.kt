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
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.utils.API_VERSION
import kotlinx.coroutines.delay

class ViewUpdateBook(private val service: ServiceBooks) : ViewModel() {

    var book: ModelBook? = null

    val selfLink: MutableLiveData<LiveDataEvent<String>> = MutableLiveData()
    val loading: MutableLiveData<LiveDataEvent<Boolean>> = MutableLiveData()
    val params: MutableLiveData<LiveDataEvent<ModelBook?>> = MutableLiveData()
    val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()

    val data = selfLink.switchMap { event ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            event.peekContent()?.let { link ->
                service.getView(link) { model ->
                    book = model
                    model?.let {
                        emit(LiveDataEvent(model))
                        if (loading.value?.peekContent() == true) {
                            delay(1200)
                        }
                        loading.postValue(LiveDataEvent(false))
                    } ?: run {
                        loading.postValue(LiveDataEvent(true))
                    }
                }
            }
        }
    }

    val updateBook = params.switchMap { event ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            event?.peekContentHandled()?.let { book ->
                if (book.links.containsKey(API_KEY_SELF)) {
                    // update
                    service.updateBook(book.links.getValue(API_KEY_SELF).value, book) {
                        emit(LiveDataEvent(true))
                    }
                } else {
                    // add
//                    service.addBook(service.db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY).value, book) {
//                        emit(LiveDataEvent(true))
//                    }
                }
            }
        }
    }
}