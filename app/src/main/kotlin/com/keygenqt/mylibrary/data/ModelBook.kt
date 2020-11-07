package com.keygenqt.mylibrary.data

import com.j256.ormlite.field.*
import com.j256.ormlite.table.*
import com.keygenqt.db.dao
import com.keygenqt.mylibrary.utils.*
import java.util.*

@DatabaseTable(tableName = "ModelBook")
class ModelBook(
    @DatabaseField
    var image: String = "",
    @DatabaseField
    var name: String = "",
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