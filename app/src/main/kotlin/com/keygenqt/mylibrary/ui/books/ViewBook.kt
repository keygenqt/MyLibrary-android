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
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.API_KEY_SELF
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.ResponseSuccessful
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewBook(private val service: ServiceBooks) : ViewModel() {

    var relation: RelationBook? = null

    val selfLink: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<LiveDataEvent<Boolean>> = MutableLiveData()
    val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()
    val delete: MutableLiveData<LiveDataEvent<ResponseSuccessful>> = MutableLiveData()

    fun getModel(link: Link): RelationBook? {
        return service.layer.findBookByLink(link)
    }

    fun getLinkViewBook(id: String): String {
        return service.layer.getLinkViewBook(id).value
    }

    val changeLink = selfLink.switchMap { link ->
        liveData(BaseExceptionHandler.getExceptionHandler()) {
            emit(Link(link))
        }
    }

    val data = selfLink.switchMap { link ->
        liveData(BaseExceptionHandler.getExceptionHandler(error)) {
            service.getView(link) { response ->
                relation = response
                delay(1200)
                emit(LiveDataEvent(relation))
            }
        }
    }

    fun delete() {
        GlobalScope.launch(BaseExceptionHandler.getExceptionHandler()) {
            relation?.model?.let { book ->
                book.enabled = false
                service.deleteBook(book.links[API_KEY_SELF] ?: error("API_KEY_SELF not found")) { result ->
                    delete.postValue(LiveDataEvent(result))
                }
            }
        }
    }
}