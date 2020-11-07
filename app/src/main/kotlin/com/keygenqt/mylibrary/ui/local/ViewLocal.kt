package com.keygenqt.mylibrary.ui.local

import androidx.lifecycle.*
import com.keygenqt.mylibrary.interfaces.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_ADD
import com.keygenqt.mylibrary.base.BaseListData.Companion.LIST_DATA_TYPE_SET
import com.keygenqt.mylibrary.data.*
import com.keygenqt.mylibrary.utils.*
import java.util.*
import kotlin.concurrent.*

class ViewLocal : ViewModel(), ViewModelPage {

    val items = MutableLiveData<BaseListData>().apply {
        value = BaseListData(ModelBook.findAll(PAGE_SIZE), LIST_DATA_TYPE_SET)
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateList() {
        updateList(1)
    }

    override fun updateList(page: Int) {
        Timer().schedule(1000) {
            loading.postValue(false)
            items.postValue(BaseListData(ModelBook.getPage(page),
                if (page == 1) LIST_DATA_TYPE_SET else LIST_DATA_TYPE_ADD))
        }
    }
}