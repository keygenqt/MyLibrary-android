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

package com.keygenqt.mylibrary.ui.local

import androidx.lifecycle.*
import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.data.services.*
import com.keygenqt.mylibrary.hal.*
import com.keygenqt.mylibrary.interfaces.*

class ViewLocal(private val service: BookService) : ViewModel(), ViewModelPage {

    private val linkFirst = "http://192.168.1.68:8080/books"

    val items = MutableLiveData<ListData<ModelBook>>().apply {
        // value = BaseListData(ModelBook.findAll(PAGE_SIZE), LIST_DATA_TYPE_SET)
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateList(linkNext: String?) {
        service.getList(linkNext ?: linkFirst) { listData ->
            loading.postValue(false)
            items.postValue(listData)
        }
    }
}