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

package com.keygenqt.mylibrary.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.keygenqt.db.dao
import com.keygenqt.mylibrary.utils.PAGE_SIZE
import java.util.*

@DatabaseTable(tableName = "ModelBook")
class ModelBook(
    @DatabaseField
    var title: String = "",

    @DatabaseField
    var author: String = "",

    @DatabaseField
    var description: String = "",

    @DatabaseField
    var publisher: String = "",

    @DatabaseField
    var year: String = "",

    @DatabaseField
    var numberOfPages: String = "",

    @DatabaseField
    var coverType: String = "",

    @DatabaseField
    var image: String = "",

    @DatabaseField(foreign = true)
    var user: ModelUser = ModelUser(),
) {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    var uid: Int = 0

    companion object
}

fun ModelBook.Companion.findAll(limit: Long = 0): List<ModelBook> {
    return try {
        val q = ModelBook.dao().queryBuilder()
        if (limit > 0) {
            q.limit(limit)
        }
        val models = ModelBook.dao().query(q.prepare())
        for (model in models) {
            model?.let {
                ModelUser.dao().refresh(model.user)
            }
        }
        return models
    } catch (ex: NoSuchElementException) {
        arrayListOf()
    }
}

fun ModelBook.Companion.getPage(page: Int, pageSize: Long = PAGE_SIZE): List<ModelBook> {
    return try {
        val q = ModelBook.dao().queryBuilder().offset(((page - 1) * pageSize)).limit(pageSize)
        val models = ModelBook.dao().query(q.prepare())
        for (model in models) {
            model?.let {
                ModelUser.dao().refresh(model.user)
            }
        }
        return models
    } catch (ex: NoSuchElementException) {
        arrayListOf()
    }
}