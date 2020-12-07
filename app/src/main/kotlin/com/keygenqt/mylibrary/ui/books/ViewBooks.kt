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
import com.keygenqt.mylibrary.base.ListLinks
import com.keygenqt.mylibrary.base.LiveDataEvent
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.Link

class ViewBooks(private val service: ServiceBooks) : ViewModel() {

    var searchValue: String? = null

    private val linkSearch = MutableLiveData(service.layer.getRootLink("/search/findAll"))

    val changeLink = linkSearch.switchMap { link ->
        liveData(getExceptionHandler()) {
            if (searchValue == null && link.isFirstPage()) {
                emit(LiveDataEvent(ListLinks(link)))
            }
        }
    }

    val linkSwitch = linkSearch.switchMap { link ->
        liveData(getExceptionHandler()) {
            service.getListSearch(link) { linkNext ->
                emit(ListLinks(link, linkNext))
            }
        }
    }

    val search = liveData(getExceptionHandler()) {
        service.getSearch { search ->
            emit(search)
        }
    }

    fun findSearch(): ModelSearch? {
        return service.layer.findSearch()
    }

    fun findItemsLimit(link: Link, limit: Int): List<ModelBook> {
        return service.layer.findItems(link = link, limit = limit)
    }

    fun findItems(link: Link, ids: List<Long> = emptyList()): List<ModelBook> {
        return service.layer.findItems(link = link, exclude = ids)
    }

    fun updateSearch(next: Link, search: String?) {
        searchValue = search
        updateList(next)
    }

    fun updateList(next: Link) {
        val link = if (next.getSearch() != searchValue) next.linkWithSearch(searchValue) else next
        if (link.isFirstPage()) {
            service.layer.clearSearchBookHistory()
        }
        linkSearch.postValue(when (link.type) {
            AdapterBooks.SEARCH_FIND_ALL_BY_USER_ID -> link.linkWithParams(hashMapOf("userId" to service.layer.userId().toString()))
            AdapterBooks.SEARCH_FIND_ALL_BY_SALE -> link.linkWithParams(hashMapOf("sale" to "true"))
            else -> link
        })
    }
}