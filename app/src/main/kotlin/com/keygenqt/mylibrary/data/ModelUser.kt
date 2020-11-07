package com.keygenqt.mylibrary.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "ModelUser")
class ModelUser(
    @DatabaseField
    var email: String = "",
) {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    var uid: Int = 0

    companion object
}