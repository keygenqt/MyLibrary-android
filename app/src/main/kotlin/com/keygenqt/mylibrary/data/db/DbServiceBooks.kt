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

package com.keygenqt.mylibrary.data.db

import com.keygenqt.mylibrary.base.BaseSharedPreferences
import com.keygenqt.mylibrary.data.RoomDatabase
import com.keygenqt.mylibrary.data.dao.*
import com.keygenqt.mylibrary.data.hal.ListDataModelBook
import com.keygenqt.mylibrary.data.hal.ListDataModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelSearch
import com.keygenqt.mylibrary.data.models.ModelSearchBook
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.utils.API_VERSION

class DbServiceBooks(
    val db: RoomDatabase,
    val preferences: BaseSharedPreferences
) {
    fun userId(): Long? {
        return preferences.userId
    }

    fun getRootLink(path: String = ""): Link {
        return Link(
            db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBook.API_KEY).value + path
        )
    }

    fun getGenresLink(): Link {
        return db.getDao<ModelRootDao>().getModel(API_VERSION).getLink(ModelBookGenre.API_KEY)
    }

    fun findSearch(): ModelSearch? {
        db.getDao<ModelSearchDao>().let { dao ->
            return dao.findModels(ModelBook.API_KEY)
        }
    }

    fun findItems(link: Link, ids: List<Long>): List<ModelBook> {
        db.getDao<ModelSearchBookDao>().let { dao ->
            return dao.findSearchModels(link.linkClearPageable.value, ids).map { it.search }
        }
    }

    fun findItemsGenres(ids: List<Long>): List<ModelBookGenre> {
        db.getDao<ModelBookGenreDao>().let { dao ->
            return dao.findModels(ids)
        }
    }

    fun findBookByLink(link: Link): ModelBook? {
        db.getDao<ModelBookDao>().findModelByLink(link.value)?.let { model ->
            return model
        }
        return null
    }

    fun saveSearch(model: ModelSearch) {
        db.getDao<ModelSearchDao>().insert(model)
    }

    fun saveListGenre(link: Link, list: ListDataModelBookGenre) {
        db.getDao<ModelBookGenreDao>().let { dao ->
            if (link.isFirstPage()) {
                dao.deleteAll()
            }
            dao.insert(*list.items.toTypedArray())
        }
    }

    fun saveListSearch(link: Link, list: ListDataModelBook) {
        db.getDao<ModelBookDao>().let { daoBook ->
            db.getDao<ModelSearchBookDao>().let { daoSearch ->
                if (link.isFirstPage()) {
                    daoSearch.deleteByPath(link.linkClearPageable.value)
                }
                daoBook.insert(*list.items.toTypedArray())
                daoSearch.insert(*(list.items.map { model ->
                    ModelSearchBook(
                        id = link.linkClearPageable.value + model.id,
                        path = link.linkClearPageable.value,
                        modelId = model.id,
                        selfLink = model.selfLink
                    )
                }.toTypedArray()))
            }
        }
    }

    fun addBook(model: ModelBook) {
        db.getDao<ModelBookDao>().insert(model)
        db.getDao<ModelSearchBookDao>().let { daoSearch ->
            daoSearch.findLinks().forEach { link ->
                if (!model.sale && link.contains("sale=true")) {
                    return@forEach
                }
                daoSearch.insert(
                    ModelSearchBook(
                        id = link + model.id,
                        path = link,
                        modelId = model.id,
                        selfLink = model.selfLink
                    )
                )
            }
        }
    }

    fun saveBook(model: ModelBook) {
        db.getDao<ModelBookDao>().update(model)
    }

    fun deleteBook(link: Link) {
        db.getDao<ModelBookDao>().deleteByLink(link.value)
        db.getDao<ModelSearchBookDao>().deleteByLink(link.value)
    }
}