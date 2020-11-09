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

package com.keygenqt.mylibrary.ui.online

import androidx.lifecycle.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_ADD
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_SET
import com.keygenqt.mylibrary.data.services.*
import com.keygenqt.mylibrary.interfaces.*

class ViewOnline(private val service: BookService) : ViewModel(), ViewModelPage {

    val items = MutableLiveData<BaseListData>().apply {
        //        value = BaseListData(ModelBook.findAll(PAGE_SIZE), LIST_DATA_TYPE_SET)
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateList() {
        updateList(1)
    }

    override fun updateList(page: Int) {
        service.getList { models ->
            loading.postValue(false)
            items.postValue(BaseListData(models, if (page == 1) LIST_DATA_TYPE_SET else LIST_DATA_TYPE_ADD))
        }
    }
}