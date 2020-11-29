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

import androidx.lifecycle.*
import com.keygenqt.mylibrary.base.BaseExceptionHandler.Companion.getExceptionHandler
import com.keygenqt.mylibrary.base.ListSearchAdapter
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.LinkListSearch
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewBooks(private val service: ServiceBooks) : ViewModel() {

    private val linkSearch: MutableLiveData<LiveDataEvent<LinkListSearch>> = MutableLiveData()

    val linkSearchSwitch = linkSearch.switchMap { event ->
        liveData(getExceptionHandler()) {
            event?.peekContent()?.let { link ->
                service.getListSearch(link) { models ->
                    emit(LiveDataEvent(models))
                }
            }

        }
    }

    val search: LiveData<LiveDataEvent<ModelSearch>> = liveData(getExceptionHandler()) {
        service.getSearch { search ->
            emit(LiveDataEvent(search))
        }
    }

    init {
        updateList(ListSearchAdapter.SEARCH_SELF)
    }

    fun updateList(key: String, link: Link? = null) {
        if (key == ListSearchAdapter.SEARCH_SELF) {
            linkSearch.postValue(LiveDataEvent(LinkListSearch(
                key = key,
                link = service.db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY),
                items = mutableListOf()
            )))
        } else {
            linkSearch.postValue(LiveDataEvent(LinkListSearch(
                key = key,
                items = linkSearch.value?.peekContent()?.items ?: mutableListOf(),
                link = link!!.linkWithParams(
                    when (key) {
                        AdapterBooks.SEARCH_FIND_ALL_BY_USER_ID -> hashMapOf("userId" to service.preferences.userId)
                        AdapterBooks.SEARCH_FIND_ALL_BY_SALE -> hashMapOf("sale" to "true")
                        else -> hashMapOf()
                    }
                )
            )))
        }
    }
}