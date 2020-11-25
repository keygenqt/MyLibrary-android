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
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelBookDao
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.BookService
import com.keygenqt.mylibrary.extensions.toListData
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewBooks(
    private val db: RoomDatabase,
    private val service: BookService,
    private val preferences: BaseSharedPreferences
) : ViewModel() {

    private val self = db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY)

    val link: MutableLiveData<Link> = MutableLiveData()

    val switchMap = link.switchMap {
        liveData(getExceptionHandler()) {
            if (link.value == self) {
                emit(db.getDao<ModelBookDao>().getAll(20).toListData(ModelBook.API_KEY, self))
            }
            service.getList(link.value) { response ->
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
        updateList()
    }

    fun updateList() {
        link.postValue(db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY))
    }
}