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
import com.keygenqt.mylibrary.base.BaseExceptionHandler.Companion.getExceptionHandler
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.utils.API_VERSION

class ListSearchLinks(val self: Link, val next: Link? = null)

class ViewBooks(private val service: ServiceBooks) : ViewModel() {

    private val linkSearch = MutableLiveData(service.db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY))

    val changeSearch = linkSearch.switchMap { link ->
        liveData(getExceptionHandler()) {
            if (link.isFirstPage()) {
                emit(LiveDataEvent(ListSearchLinks(link)))
            }
        }
    }

    val linkSwitch = linkSearch.switchMap { link ->
        liveData(getExceptionHandler()) {
            service.getListSearch(link) { linkNext ->
                emit(ListSearchLinks(link, linkNext))
            }
        }
    }

    val search = liveData(getExceptionHandler()) {
        service.getSearch { search ->
            emit(search)
        }
    }

    fun findSearch(): ModelSearch? {
        return service.findSearch()
    }

    fun findItems(link: Link, ids: List<Long> = emptyList()): List<ModelBook> {
        return service.findItems(link, ids)
    }

    fun updateList(next: Link) {
        linkSearch.postValue(when (next.type) {
            AdapterBooks.SEARCH_FIND_ALL_BY_USER_ID -> next.linkWithParams(hashMapOf("userId" to service.preferences.userId.toString()))
            AdapterBooks.SEARCH_FIND_ALL_BY_SALE -> next.linkWithParams(hashMapOf("sale" to "true"))
            else -> next
        })
    }
}