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
import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.base.ListSearchAdapter
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelBookDao
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.BookService
import com.keygenqt.mylibrary.extensions.toListData
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.LinkSearch
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewBooks(
    private val db: RoomDatabase,
    private val service: BookService,
    private val preferences: BaseSharedPreferences
) : ViewModel() {

    private val linkSearch: MutableLiveData<LinkSearch> = MutableLiveData()

    val switchMap = linkSearch.switchMap {
        liveData(getExceptionHandler()) {
            if (it.isFirstPage()) {
                emit(db.getDao<ModelBookDao>().getAll(it.key).toListData(ModelBook.API_KEY, it.getLinkModel()))
            }
            service.getList(it) { response ->
                emit(response)
            }
        }
    }

    val search: LiveData<ModelSearch> = liveData(getExceptionHandler()) {
        service.getSearch { search ->
            emit(search)
        }
    }

    init {
        updateList(ListSearchAdapter.SEARCH_SELF)
    }

    fun updateList(key: String, link: Link? = null) {
        if (key == ListSearchAdapter.SEARCH_SELF) {
            linkSearch.postValue(LinkSearch(
                key = key,
                link = db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY),
                items = mutableListOf()
            ))
        } else {
            linkSearch.postValue(LinkSearch(
                key = key,
                items = linkSearch.value?.items ?: mutableListOf(),
                link = link!!.linkWithParams(
                    when (key) {
                        AdapterBooks.SEARCH_FIND_ALL_BY_USER_ID -> hashMapOf("userId" to preferences.userId)
                        AdapterBooks.SEARCH_FIND_ALL_BY_SALE -> hashMapOf("sale" to "true")
                        else -> hashMapOf()
                    }
                )
            ))
        }
    }
}