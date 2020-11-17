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
import com.keygenqt.mylibrary.base.response.BaseResponseError.Companion.getExceptionHandler
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.services.BookService
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.interfaces.ViewModelPage
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewBooks(private val service: BookService, db: RoomDatabase) : ViewModel(), ViewModelPage {

    private val linkModel = db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY)

    override val link: MutableLiveData<Link?> = MutableLiveData()
    override val loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        link.postValue(null)
    }

    val listData = link.switchMap {
        liveData(getExceptionHandler()) {
            loading.postValue(link.value?.isFirstPage() == true)
            service.getList(link.value?.link ?: linkModel.link) { listData ->
                loading.postValue(false)
                emit(listData)
            }
        }
    }

    val search: LiveData<SearchModelBooks> = liveData(getExceptionHandler()) {
        service.getSearch { links ->
            emit(links)
        }
    }
}