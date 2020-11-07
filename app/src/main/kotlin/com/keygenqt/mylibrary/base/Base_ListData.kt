package com.keygenqt.mylibrary.base

/**
 * Class BaseListData using in ViewModel for set or add adapters items
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
data class BaseListData(val items: List<Any>, val type: Int = LIST_DATA_TYPE_SET) {
    companion object {
        const val LIST_DATA_TYPE_ADD = 1
        const val LIST_DATA_TYPE_SET = 2
    }
}