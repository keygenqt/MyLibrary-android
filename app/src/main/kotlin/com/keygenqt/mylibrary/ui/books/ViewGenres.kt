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
import com.keygenqt.mylibrary.data.dao.ModelRootDao
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.services.ServiceBooks
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.hal.LinkList
import com.keygenqt.mylibrary.utils.API_VERSION

class ViewGenres(private val service: ServiceBooks) : ViewModel() {

    private val linkList: MutableLiveData<LinkList> = MutableLiveData()

    val switchMap = linkList.switchMap { link ->
        liveData(getExceptionHandler()) {
            service.getGenresList(link) { models ->
                emit(models)
            }
        }
    }

    init {
        updateList(service.db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBookGenre.API_KEY))
    }

    fun updateList(link: Link? = null) {
        linkList.postValue(LinkList(
            link = link!!,
            items = linkList.value?.items ?: mutableListOf(),
        ))
    }
}