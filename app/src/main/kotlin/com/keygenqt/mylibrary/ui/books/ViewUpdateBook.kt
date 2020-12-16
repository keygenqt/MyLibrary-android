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

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.keygenqt.mylibrary.base.BaseExceptionHandler
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.hal.Link
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewUpdateBook(private val service: ServiceBooks) : ViewModel() {

    var relation: RelationBook? = null

    val imageLink: MutableLiveData<String> = MutableLiveData()
    val selfLink: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<LiveDataEvent<Boolean>> = MutableLiveData()
    val params: MutableLiveData<ModelBook> = MutableLiveData()
    val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()

    fun getModel(link: Link): RelationBook? {
        return service.layer.findBookByLink(link)
    }

    val changeLink = selfLink.switchMap { link ->
        liveData(BaseExceptionHandler.getExceptionHandler()) {
            emit(Link(link))
        }
    }

    val data = selfLink.switchMap { link ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            service.getView(link) { result ->
                relation = result
                delay(1200)
                emit(LiveDataEvent(relation))
            }
        }
    }

    val updateBook = params.switchMap { book ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            if (book.links.containsKey(API_KEY_SELF)) {
                // update
                service.updateBook(book.links.getValue(API_KEY_SELF).value, book) {
                    emit(LiveDataEvent(true))
                }
            } else {
                // add
                service.addBook(book) {
                    emit(LiveDataEvent(true))
                }
            }
        }
    }

    fun uploadImage(bitmap: Bitmap) {
        GlobalScope.launch(BaseExceptionHandler.getExceptionHandler()) {
            service.uploadImage(bitmap) { link ->
                imageLink.postValue(link)
            }
        }
    }
}