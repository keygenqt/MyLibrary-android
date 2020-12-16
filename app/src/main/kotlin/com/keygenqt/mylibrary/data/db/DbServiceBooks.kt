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
import com.keygenqt.mylibrary.data.models.*
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.hal.API_KEY_UPLOAD_IMAGE
import com.keygenqt.mylibrary.hal.Link
import com.keygenqt.mylibrary.utils.API_VERSION

class DbServiceBooks(
    val db: RoomDatabase,
    val preferences: BaseSharedPreferences,
) {
    fun userId(): Long? {
        return preferences.userId
    }

    fun getLinkViewBook(id: String): Link {
        return Link(
            db.getDao<ModelRootDao>().findModel(API_VERSION).getLink(ModelBook.API_KEY).value + "/$id"
        )
    }

    fun getRootLink(path: String = ""): Link {
        return Link(
            db.getDao<ModelRootDao>().findModel(API_VERSION).getLink(ModelBook.API_KEY).value + path
        )
    }

    fun getGenresLink(): Link {
        return db.getDao<ModelRootDao>().findModel(API_VERSION).getLink(ModelBookGenre.API_KEY)
    }

    fun getUploadImageLink(): Link {
        return db.getDao<ModelRootDao>().findModel(API_VERSION).getLink(API_KEY_UPLOAD_IMAGE)
    }

    fun findSearch(): ModelSearch? {
        db.getDao<ModelSearchDao>().let { dao ->
            return dao.findModels(ModelBook.API_KEY)
        }
    }

    fun findItems(link: Link, exclude: List<Long> = emptyList(), limit: Int = 1000): List<ModelBook> {
        db.getDao<ModelSearchBookDao>().let { dao ->
            return dao.findSearchModels(link.linkClearPageable.value, limit)
                .map { it.search }.filter { !exclude.contains(it.id) }
        }
    }

    fun findItemsGenres(exclude: List<Long> = emptyList(), limit: Int = 1000): List<ModelListGenre> {
        db.getDao<ModelListGenreDao>().let { dao ->
            return dao.findModels(limit).filter { !exclude.contains(it.id) }
        }
    }

    fun findBookByLink(link: Link): RelationBook? {
        db.getDao<ModelBookDao>().findModelByLink(link.value)?.let { model ->
            return model
        }
        return null
    }

    fun saveSearch(model: ModelSearch) {
        db.getDao<ModelSearchDao>().insert(model)
    }

    fun saveListGenre(link: Link, list: ListDataModelBookGenre) {
        db.getDao<ModelListGenreDao>().let { dao ->
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

    fun saveBook(relation: RelationBook) {
        db.getDao<ModelBookDao>().insert(relation.model)
        relation.genre?.let {
            db.getDao<ModelBookGenreDao>().insert(it)
        }
        relation.user?.let {
            db.getDao<ModelBookUserDao>().insert(it)
        }
    }

    fun deleteBook(link: Link) {
        db.getDao<ModelBookDao>().let { dao ->
            dao.findModelByLink(link.value)?.let { it ->
                it.genre?.let { genre ->
                    db.getDao<ModelBookGenreDao>().deleteById(genre.id)
                }
                it.user?.let { user ->
                    db.getDao<ModelBookUserDao>().deleteById(user.id)
                }
                db.getDao<ModelBookDao>().deleteById(it.model.id)
            }
        }
        db.getDao<ModelSearchBookDao>().deleteByLink(link.value)
    }

    fun clearSearchBookHistory() {
        db.getDao<ModelSearchBookDao>().clearSearchBookHistory()
    }
}